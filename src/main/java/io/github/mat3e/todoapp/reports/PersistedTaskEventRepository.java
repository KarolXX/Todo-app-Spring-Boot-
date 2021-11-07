package io.github.mat3e.todoapp.reports;

import io.github.mat3e.todoapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface PersistedTaskEventRepository extends JpaRepository<PersistedTaskEvent, Integer> {
    List<PersistedTaskEvent> findByTaskId(Integer id);
}
