package io.github.mat3e.todoapp.model.projection;

import io.github.mat3e.todoapp.model.Task;

import java.time.LocalDateTime;

public class GroupTaskWriteModel {
    /*
    when creating a task in a group, the request body will consist of
    description and deadline (it will be in JSON)
     with this class JSON will be serialized into GroupTaskWriteModel object.
     (now note about GroupWriteModel.toGroup() ) : to save it in DB we need Group and Set<Task>
     */
    private String description;
    private LocalDateTime deadline;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Task toTask() {
        return new Task(description, deadline);
    }
}
