package io.github.mat3e.todoapp.logic;

import io.github.mat3e.todoapp.model.TaskGroup;
import io.github.mat3e.todoapp.model.projection.GroupWriteModel;
import io.github.mat3e.todoapp.model.TaskGroupRepository;
import io.github.mat3e.todoapp.model.TaskRepository;
import io.github.mat3e.todoapp.model.projection.GroupReadModel;
//import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//@Service
public class TaskGroupService {
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    public TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel group) {
        TaskGroup result = repository.save(group.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId))
            throw new IllegalStateException("Group has undone tasks. Done all the tasks first");
        var result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Task group with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }
}
