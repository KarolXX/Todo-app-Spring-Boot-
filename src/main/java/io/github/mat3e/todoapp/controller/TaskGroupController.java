package io.github.mat3e.todoapp.controller;

import io.github.mat3e.todoapp.logic.TaskGroupService;
import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskGroup;
import io.github.mat3e.todoapp.model.TaskRepository;
import io.github.mat3e.todoapp.model.projection.GroupReadModel;
import io.github.mat3e.todoapp.model.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
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

    @GetMapping(params = {"!sort", "!page", "!size"})
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.warn("Exposing all the groups!");
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/{id}/tasks")
    ResponseEntity<List<Task>> readAllTasksByGroupId(@PathVariable int id) {
        logger.warn("Exposing all the tasks from given group");
        return ResponseEntity.ok(taskRepository.findTasksByGroup_Id(id));
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id) {
        logger.info("group toggling");
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
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
