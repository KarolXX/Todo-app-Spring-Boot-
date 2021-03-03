package io.github.mat3e.todoapp.logic;

import io.github.mat3e.todoapp.TaskConfigurationProperties;
import io.github.mat3e.todoapp.model.*;
import io.github.mat3e.todoapp.model.projection.GroupReadModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties properties;

    public ProjectService(ProjectRepository repository, TaskGroupRepository taskGroupRepository, TaskConfigurationProperties properties) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.properties = properties;
    }

    public List<Project> readAllProjects() {
        return repository.findAll();
    }

    public Project createProject(Project project) {
        return repository.save(project);
    }

    public GroupReadModel createGroupByProject(int projectId, LocalDateTime deadline) throws Exception {
        if (!taskGroupRepository.existsByDoneIsFalseAndAndProject_Id(projectId) && properties.getTemplate().isAllowMultipleTasks())
            throw new IllegalStateException("only one undone group from project is allowed");

        var project = repository.findById(projectId).orElseThrow(() -> new Exception());

        TaskGroup result = new TaskGroup();
        result.setDescription(project.getDescription());
        result.setProject(project);
        var resultTasks = project.getSteps().stream()
                .map(step -> {
                    LocalDateTime taskDeadline = deadline.plusDays(step.getDaysToDeadline());
                    return new Task(step.getDescription(), taskDeadline);
                }).collect(Collectors.toSet());
        result.setTasks(resultTasks);

        taskGroupRepository.save(result);

        return new GroupReadModel(result);
    }
}
