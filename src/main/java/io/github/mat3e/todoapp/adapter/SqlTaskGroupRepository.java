package io.github.mat3e.todoapp.adapter;

import io.github.mat3e.todoapp.model.TaskGroup;
import io.github.mat3e.todoapp.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {
    @Override
    @Query("select distinct g from TaskGroup g join fetch g.tasks") //"from" is the shortcut for "select * from"
    List<TaskGroup> findAll();

    @Override
    boolean existsByDoneIsFalseAndAndProject_Id(Integer projectId);

//    @Override
//    Optional<TaskGroup> findByDoneIsFalseAndAndProject_Id(Integer projectId);
}
