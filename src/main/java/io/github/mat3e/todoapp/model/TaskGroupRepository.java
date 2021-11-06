package io.github.mat3e.todoapp.model;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {
    List<TaskGroup> findAll();

    Optional<TaskGroup> findById(Integer id);

    TaskGroup save(TaskGroup entity);

    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);

    //Optional<TaskGroup> findByDoneIsFalseAndAndProject_Id(Integer projectId);

    void deleteById(Integer id);

    boolean existsByDescription(String description);
}
