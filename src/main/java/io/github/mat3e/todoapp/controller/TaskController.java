package io.github.mat3e.todoapp.controller;

import io.github.mat3e.todoapp.logic.TaskService;
import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/tasks")
class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;
    private final TaskService service;
    private final ApplicationEventPublisher eventPublisher;

    TaskController(final TaskRepository repository, final TaskService service, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.service = service;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping
    ResponseEntity<Task> createNewTask(@RequestBody @Valid Task newTask) {
        logger.warn("Creating new task!");
        var result = repository.save(newTask);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping(params = {"!sort", "!page", "!size"})
    CompletableFuture<ResponseEntity<List<Task>>> readAllTasks() {
        logger.warn("Exposing all the todos!!!");
        return service.findAllTasksAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findByDone(state));
    }

    @GetMapping("/search/today")
    ResponseEntity<List<Task>> readTodayTasks() {
        LocalDateTime today = LocalDate.now().atTime(23, 59, 59, 999);
        return ResponseEntity.ok(
                repository.findTasksByDoneIsFalseAndDeadlineIsNullOrDoneIsFalseAndDeadlineIsLessThanEqualOrderByDeadline(today)
        );
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id) {
        logger.warn("exposing specified todo");
        return repository.findById(id).
                map(ResponseEntity::ok).
                orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (repository.existsById(id)) {
            repository.findById(id)
                    .ifPresent(task -> task.partialUpdate(toUpdate));
            return ResponseEntity.noContent().build();
        } else {
            var result = repository.save(toUpdate);
            return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
        }
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        if (!repository.existsById(id))
            return ResponseEntity.notFound().build();
        repository.findById(id)
                .map(Task::toggle)
                .ifPresent(eventPublisher::publishEvent);
        return ResponseEntity.noContent().build();
    }
}
