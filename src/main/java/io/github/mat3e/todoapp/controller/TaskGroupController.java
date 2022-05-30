package io.github.mat3e.todoapp.controller;

import io.github.mat3e.todoapp.logic.TaskGroupService;
import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskGroupRepository;
import io.github.mat3e.todoapp.model.TaskRepository;
import io.github.mat3e.todoapp.model.projection.GroupReadModel;
import io.github.mat3e.todoapp.model.projection.GroupTaskWriteModel;
import io.github.mat3e.todoapp.model.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@IllegalExceptionProcessing
@RequestMapping("/groups")
public class TaskGroupController {
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private TaskGroupService service;
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    public TaskGroupController(final TaskGroupService service, final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.service = service;
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String getGroupTemplate(Model model) {
        model.addAttribute("group", new GroupWriteModel());
        return "groups";
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    String addGroup(
            @ModelAttribute("group") @Valid GroupWriteModel current,
            BindingResult bindingResult,
            Model model
    ) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("msg", "You can't add such group"); //FIXME Can I use there "msg" if it is in another model?    YES YOU CAN
            return "groups";
        }
        service.createGroup(current);
        model.addAttribute("group", new GroupWriteModel());
        model.addAttribute("groups", getGroups());
        model.addAttribute("msg", "The group has been added");
        return "groups";
    }

    @PostMapping(
            params = "removeTask",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE
    )
    String removeGroupTask(
            @ModelAttribute("group") GroupWriteModel current,
            @RequestParam("removeTask") int id
    ) {
        current.getTasks().remove(id);
        return "groups";
    }

    @PostMapping(
            params = "addTask",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE
    )
    String addGroupTask(@ModelAttribute("group") GroupWriteModel current) {
        current.getTasks().add(new GroupTaskWriteModel());
        return "groups";
    }

    @PostMapping(value = "/{id}", params = "toggle")
    String toggleGroup(
            @PathVariable int id,
            Model model,
            @ModelAttribute("group") GroupWriteModel current
    ) {
        try {
            service.toggleGroup(id);
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("msg", e.getMessage());
        }
        return "groups";
    }

    @ModelAttribute("groups")
    List<GroupReadModel> getGroups() {
        return service.readAllGroups();
    }

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.warn("Exposing all the groups!");
        return ResponseEntity.ok(service.readAllGroups());
    }

    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Task>> readAllTasksByGroupId(@PathVariable int id) {
        logger.warn("Exposing all the tasks from given group");
        return ResponseEntity.ok(taskRepository.findTasksByGroup_Id(id));
    }

    @ResponseBody
    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id) {
        logger.info("group toggling");
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel source) {
        logger.info("creating new group");
        var result = service.createGroup(source);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @ResponseBody
    @PostMapping(value = "/{id}/tasks", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createGroupTask(@PathVariable int id, @RequestBody GroupTaskWriteModel groupTask) {
        logger.info("create task in specified group");
        var group = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Group with given id not found"));
        var result = taskRepository.save(groupTask.toTask(group));
        return ResponseEntity.created(URI.create("")).body(result);
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteGroupTask(@PathVariable Integer id) {
        logger.warn("TaskGroup has been deleted");
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


