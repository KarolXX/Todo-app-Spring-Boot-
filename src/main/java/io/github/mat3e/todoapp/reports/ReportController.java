package io.github.mat3e.todoapp.reports;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskRepository;
import io.github.mat3e.todoapp.model.events.TaskEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reports")
class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
    private final ReportService service;

    ReportController(final ReportService service) {
        this.service = service;
    }

    @GetMapping("/count/{id}")
    ResponseEntity<ReportService.TaskWithEventCount> getTaskWithEventCount(@PathVariable int id) {
        logger.info("Exposing task with with the measure of events");
        ReportService.TaskWithEventCount result = service.getTaskWithEventCount(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/done/{id}")
    ResponseEntity<?> isTaskDoneBeforeDeadline(@PathVariable int id) {
        logger.info("checking if task was done before deadline");
        return service.isTaskDoneBeforeDeadline(id);
    }
}
