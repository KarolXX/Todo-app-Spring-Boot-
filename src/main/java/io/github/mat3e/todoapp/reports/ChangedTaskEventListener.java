package io.github.mat3e.todoapp.reports;

import io.github.mat3e.todoapp.model.events.TaskDone;
import io.github.mat3e.todoapp.model.events.TaskEvent;
import io.github.mat3e.todoapp.model.events.TaskUndone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ChangedTaskEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ChangedTaskEventListener.class);
    private final PersistedTaskEventRepository repository;

    public ChangedTaskEventListener(PersistedTaskEventRepository repository) {
        this.repository = repository;
    }

    @EventListener
    public void on(TaskDone event) {
        onChanged(event);
    }

    @EventListener
    public void on(TaskUndone event) {
        onChanged(event);
    }

    private void onChanged(final TaskEvent event) {
        logger.info("Got " + event);
        repository.save(new PersistedTaskEvent(event));
    }
}
