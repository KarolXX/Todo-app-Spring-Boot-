package io.github.mat3e.todoapp.model.events;

import io.github.mat3e.todoapp.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent {
    TaskDone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
