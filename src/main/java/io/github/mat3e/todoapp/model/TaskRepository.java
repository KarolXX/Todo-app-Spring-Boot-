package io.github.mat3e.todoapp.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    Optional<Task> findById(Integer id);

    boolean existsById(Integer id);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    List<Task> findTasksByGroup_Id(Integer groupId);

    List<Task> findTasksByDoneIsFalseAndDeadlineIsNullOrDoneIsFalseAndDeadlineIsLessThanEqualOrderByDeadline(LocalDateTime date);

    Task save(Task entity);

    Page<Task> findAll(Pageable page);

    List<Task> findByDone(@Param("state") boolean done);
}
