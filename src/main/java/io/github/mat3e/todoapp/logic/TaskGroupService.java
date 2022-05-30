package io.github.mat3e.todoapp.logic;

import io.github.mat3e.todoapp.model.Project;
import io.github.mat3e.todoapp.model.TaskGroup;
import io.github.mat3e.todoapp.model.projection.GroupWriteModel;
import io.github.mat3e.todoapp.model.TaskGroupRepository;
import io.github.mat3e.todoapp.model.TaskRepository;
import io.github.mat3e.todoapp.model.projection.GroupReadModel;
//import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

//@Service (I've used @Bean in LogicConfiguration class)
public class TaskGroupService {
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    public TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel group) {
        return createGroup(group, null);
    }

    GroupReadModel createGroup(GroupWriteModel group, Project project) {
        TaskGroup result = repository.save(group.toGroup(project));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAllGroups() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void toggleGroup(int groupId) {
        var result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Task group with given id not found"));
        if(!result.isDone()) {
            if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId))
                throw new IllegalStateException("Group has undone tasks. Do all the tasks first");
        }
        else {
            if(!taskRepository.existsByDoneIsFalseAndGroup_Id(groupId))
                throw new IllegalStateException("All tasks in the group are done");
        }
        result.setDone(!result.isDone());
    }
}
