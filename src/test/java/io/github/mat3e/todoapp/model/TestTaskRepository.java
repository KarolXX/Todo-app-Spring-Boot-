package io.github.mat3e.todoapp.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public class TestTaskRepository implements TaskRepository {
    private Map<Integer, Task> tasks = new HashMap<>();

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Optional<Task> findById(Integer id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public boolean existsById(Integer id) {
        return tasks.containsKey(id);
    }

    @Override
    public boolean existsByDoneIsFalseAndGroup_Id(Integer groupId) {
        return false;
    }

    @Override
    public Task save(Task entity) {
        return tasks.put(tasks.size() + 1, entity);
    }

    @Override
    public Page<Task> findAll(Pageable page) {
        return null;
    }

    @Override
    public List<Task> findByDone(boolean done) {
        return null;
    }
}
