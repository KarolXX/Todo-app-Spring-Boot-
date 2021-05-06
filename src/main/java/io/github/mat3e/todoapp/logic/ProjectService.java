package io.github.mat3e.todoapp.logic;

import io.github.mat3e.todoapp.TaskConfigurationProperties;
import io.github.mat3e.todoapp.model.Project;
import io.github.mat3e.todoapp.model.ProjectRepository;
import io.github.mat3e.todoapp.model.ProjectStep;
import io.github.mat3e.todoapp.model.TaskGroupRepository;
import io.github.mat3e.todoapp.model.projection.GroupReadModel;
import io.github.mat3e.todoapp.model.projection.GroupTaskWriteModel;
import io.github.mat3e.todoapp.model.projection.GroupWriteModel;
import io.github.mat3e.todoapp.model.projection.ProjectWriteAndReadModel;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//@Service //(I've used @Bean in LogicConfiguration class)
public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;
    private TaskGroupService taskGroupService;

    public ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository, TaskGroupService taskGroupService, final TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskGroupService = taskGroupService;
        this.config = config;
    }

    public List<ProjectWriteAndReadModel> readAll() {
//        var result = repository.findAll()
//                .stream()
//                .flatMap(project -> project.getSteps().stream())
//                .collect(Collectors.toList())
//                .stream()
//                .sorted(Comparator.comparing(ProjectStep::getDaysToDeadline))
//                .collect(Collectors.toList());
        return repository.findAll()
                .stream()
                .map(ProjectWriteAndReadModel::new)
                .collect(Collectors.toList()).stream()
                .map(projectWriteAndReadModel -> {
                    ProjectWriteAndReadModel result = projectWriteAndReadModel;

                    var resultSteps = result.getSteps()
                        .stream()
                        .sorted(Comparator.comparing(ProjectStep::getDaysToDeadline))
                        .collect(Collectors.toList());

                    result.setSteps(resultSteps);
                    return result;
                }).collect(Collectors.toList());
    }

    public Project save(final ProjectWriteAndReadModel toSave) {
        return repository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        GroupReadModel result = repository.findById(projectId)
                .map(project -> {
                    //service.createGroup()
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> {
                                        var task = new GroupTaskWriteModel();
                                        task.setDescription(projectStep.getDescription());
                                        task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                        return task;
                                    }
                                    ).collect(Collectors.toList())
                    );
                    return taskGroupService.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return result;
    }
}
