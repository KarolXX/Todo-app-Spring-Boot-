package io.github.mat3e.todoapp.reports;

import io.github.mat3e.todoapp.model.events.TaskEvent;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "task_events")
class PersistedTaskEvent implements Comparable<PersistedTaskEvent>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int taskId;
    LocalDateTime occurrence;
    String name;

    protected PersistedTaskEvent() {
    }

    PersistedTaskEvent(TaskEvent source) {
        taskId = source.getTaskId();
        name = source.getClass().getSimpleName();
        occurrence = LocalDateTime.ofInstant(source.getOccurrence(), ZoneId.systemDefault());
    }

    @Override
    public int compareTo(PersistedTaskEvent o) {
        if(this.occurrence.isAfter(o.occurrence))
            return -1;
        else if(this.occurrence.equals(o.occurrence))
            return 0;
        else
            return 1;
    }
}
