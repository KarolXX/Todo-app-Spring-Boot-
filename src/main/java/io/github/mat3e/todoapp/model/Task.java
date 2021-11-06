package io.github.mat3e.todoapp.model;

import io.github.mat3e.todoapp.model.events.TaskEvent;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) //random id while working with flyway
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int id;
    @NotBlank(message = "Task description must be not null")
    private String description;
    private boolean done;
    private LocalDateTime deadline;
    @Embedded
    private Audit audit = new Audit();

    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;

    protected Task() {
    }

    public Task(String description, LocalDateTime deadline) {
        this(description, deadline, null);
    }

    public Task(String description, LocalDateTime deadline, TaskGroup taskGroup) {
        this.description = description;
        this.deadline = deadline;
        if(taskGroup != null) {
            this.group = taskGroup;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public TaskEvent toggle() {
        this.done = !this.done;
        return TaskEvent.changed(this);
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    TaskGroup getGroup() {
        return group;
    }

    void setGroup(final TaskGroup group) { this.group = group; }

    public void partialUpdate(final Task source) {
        // TODO why we use properties(e.g. source.description) not getters (e.g. source.getDescription()) ?
        // EDIT: Maybe bcs we fetch these properties from JSON not from Java Object
        // but i doubt because i think objectmapper(then JSON is deserialized) works first when updateTask() is called (look at controller)
        this.description = source.description;
        this.done = source.done;
        this.deadline = source.deadline;
    }

}
