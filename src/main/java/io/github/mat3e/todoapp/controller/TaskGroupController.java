package io.github.mat3e.todoapp.controller;

import io.github.mat3e.todoapp.logic.TaskGroupService;
import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskGroup;
import io.github.mat3e.todoapp.model.TaskRepository;
import io.github.mat3e.todoapp.model.projection.GroupReadModel;
import io.github.mat3e.todoapp.model.projection.GroupTaskWriteModel;
import io.github.mat3e.todoapp.model.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/groups")
public class TaskGroupController {
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private TaskGroupService service;
    private TaskRepository taskRepository;

    public TaskGroupController(final TaskGroupService service, final TaskRepository taskRepository) {
        this.service = service;
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
    String removeTask(
            @ModelAttribute("group") GroupWriteModel current,
            @RequestParam("removeTask") int id
    ) {
        current.getTasks().remove(id);
        return "groups";
    }

    @PostMapping(
            params = "addTask",
            //consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE
    )
    String addTask(@ModelAttribute("group") GroupWriteModel current) {
        current.getTasks().add(new GroupTaskWriteModel());
        return "groups";
    }

    @ModelAttribute("groups")
    List<GroupReadModel> getGroups() {
        return service.readAll();
    }


    @ResponseBody
    //TODO REMEMBER I spent all day troubleshooting the problem - my controller was returning JSON instead of HTML
    // bcs in allow annotation I had parameter :   params = {"!sort", "!page", "!size"}
    // so every GET to this address without these params was related to this method...
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.warn("Exposing all the groups!");
        return ResponseEntity.ok(service.readAll());
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

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> illegalArgumentHandler(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<?> illegalStateHandler(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}


