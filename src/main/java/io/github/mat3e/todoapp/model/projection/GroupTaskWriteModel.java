package io.github.mat3e.todoapp.model.projection;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskGroup;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class GroupTaskWriteModel {
    /*
    when creating a task in a group, the request body will consist of
    description and deadline (it will be in JSON)
     with this class JSON will be serialized into GroupTaskWriteModel object.
     (now note about GroupWriteModel.toGroup() ) : to save it in DB we need Group and Set<Task>
     */
    @NotBlank(message = "Task's description must be not null")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deadline;

    public GroupTaskWriteModel() {
    }

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

    public Task toTask(final TaskGroup group) {
        return new Task(description, deadline, group);
    }
}
