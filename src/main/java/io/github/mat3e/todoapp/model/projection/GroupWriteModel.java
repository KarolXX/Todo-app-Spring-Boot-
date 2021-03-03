package io.github.mat3e.todoapp.model.projection;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskGroup;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupWriteModel {
    private String description;
    private Set<GroupTaskWriteModel> tasks;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<GroupTaskWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<GroupTaskWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TaskGroup toGroup() {
        //my version of GroupWriteModel(88)
//        Set<Task> realTasks = tasks.stream().map(GroupTaskWriteModel::toTask).collect(Collectors.toSet());
//        return new TaskGroup(description, realTasks);

        var result = new TaskGroup();
        result.setDescription(description);
        result.setTasks(
                tasks.stream()
                      .map(GroupTaskWriteModel::toTask)
                        .collect(Collectors.toSet()));
        return result;
    }
}
