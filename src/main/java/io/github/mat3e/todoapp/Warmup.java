package io.github.mat3e.todoapp;

import io.github.mat3e.todoapp.logic.TaskGroupService;
import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskGroupRepository;
import io.github.mat3e.todoapp.model.projection.GroupTaskWriteModel;
import io.github.mat3e.todoapp.model.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class Warmup implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(Warmup.class);
    private final TaskGroupRepository groupRepository;
    private final TaskGroupService groupService;

    public Warmup(final TaskGroupRepository groupRepository, TaskGroupService groupService) {
        this.groupRepository = groupRepository;
        this.groupService = groupService;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        logger.info("An application context has been initialized");
        final String description = "Spring events";
        if(!groupRepository.existsByDescription(description)) {
            logger.info("Base group adding");
            GroupWriteModel baseGroup = new GroupWriteModel();
            baseGroup.setDescription(description);
            baseGroup.setTasks(List.of(
                    new GroupTaskWriteModel("ContextRefreshedEvent", null),
                    new GroupTaskWriteModel("ContextClosedEvent", null),
                    new GroupTaskWriteModel("ContextStoppedEvent", null),
                    new GroupTaskWriteModel("ContextStartedEvent", null)
            ));
            groupService.createGroup(baseGroup);
        }
    }
}
