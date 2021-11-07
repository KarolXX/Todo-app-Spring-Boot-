package io.github.mat3e.todoapp.reports;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
class ReportController {
    private final TaskRepository taskRepository;
    private final PersistedTaskEventRepository eventRepository;

    ReportController(final TaskRepository taskRepository, final PersistedTaskEventRepository eventRepository) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/count/{id}")
    ResponseEntity<TaskWithCount> getTaskWithEventsCount(@PathVariable int id) {
        return taskRepository.findById(id)
                .map(task -> new TaskWithCount(task, eventRepository.findByTaskId(id).size()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private static class TaskWithCount {
        public Task task;
        public int count;

        public TaskWithCount(Task task, int count) {
            this.task = task;
            this.count = count;
        }
    }
}
