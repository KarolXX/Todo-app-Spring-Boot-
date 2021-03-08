package io.github.mat3e.todoapp.logic;

import io.github.mat3e.todoapp.TaskConfigurationProperties;
import io.github.mat3e.todoapp.model.ProjectRepository;
import io.github.mat3e.todoapp.model.TaskGroupRepository;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogicConfiguration {
    @Bean
    ProjectService projectService(
            final ProjectRepository repository,
            final TaskGroupRepository taskGroupRepository,
            final TaskConfigurationProperties config
    ) {
        return new ProjectService(repository, taskGroupRepository, config);
    }

    @Bean
    TaskGroupService taskGroupService(
            final TaskGroupRepository taskGroupRepository,
            @Qualifier("sqlTaskRepository") final TaskRepository taskRepository
            ) {
        return new TaskGroupService(taskGroupRepository, taskRepository);
    }
}
