package io.github.mat3e.todoapp.reports;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    private final TaskRepository taskRepository;
    private final PersistedTaskEventRepository eventRepository;

    public ReportService(TaskRepository taskRepository, PersistedTaskEventRepository eventRepository) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
    }
    
    TaskWithEventCount getTaskWithEventCount(int id) {
        return taskRepository.findById(id)
                .map(task -> new TaskWithEventCount(task, eventRepository.findByTaskId(id).size()))
                .orElseThrow(() -> new IllegalArgumentException("No task with given id"));
    }

    ResponseEntity<?> isTaskDoneBeforeDeadline(int id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No task with given id"));

        List<PersistedTaskEvent> events = eventRepository.findByTaskId(id);
        if (events.isEmpty())
            return ResponseEntity.ok("Task is undone");
        // sort list to get event with 0 index - the latest event
        Collections.sort(events);
        PersistedTaskEvent lastEvent = events.get(0);

        if (lastEvent.name.equals("TaskUndone"))
            return ResponseEntity.ok("Task is undone");

        if (task.getDeadline() == null || lastEvent.occurrence.isBefore(task.getDeadline()))
            return ResponseEntity.ok(true);
        else
            return ResponseEntity.ok(false);
    }

    public static class TaskWithEventCount {
        public Task task;
        public int count;

        public TaskWithEventCount(Task task, int count) {
            this.task = task;
            this.count = count;
        }
    }
}
