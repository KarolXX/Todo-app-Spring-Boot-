package io.github.mat3e.todoapp.logic;

import io.github.mat3e.todoapp.model.TaskGroup;
import io.github.mat3e.todoapp.model.TaskGroupRepository;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.*;

class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when undone task exists in group with given id")
    void toggleGroup_undoneTaskExists_throwsIllegalStateException() {
        //given
        TaskRepository mockTaskRepository = taskRepositoryReturning(true);
        //system under test
        var toTest = new TaskGroupService(null, mockTaskRepository);

        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Group has undone tasks");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when no undone task exists and no group with given id")
    void toggleGroup_noUndoneTaskExists_noGroups_throwsIllegalArgumentException() {
        //given
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        //system under test
        var toTest = new TaskGroupService(mockGroupRepository, mockTaskRepository);

        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("group with given id");
    }

    @Test
    @DisplayName("should toggle group when no undone task exsists and group with given id exists")
    void toggleGroup_noUndoneTaskExists_And_groupExists_toggleGroup() {
        //given
        var group = new TaskGroup();
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.findById(anyInt()))
                .thenReturn(Optional.of(group));
        var doneBeforeToggle = group.isDone();
        //and
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        //system under test
        var toTest = new TaskGroupService(mockGroupRepository, mockTaskRepository);

        //when
        toTest.toggleGroup(1);
        //then
        assertThat(group.isDone()).isEqualTo(!doneBeforeToggle);
    }

    private TaskRepository taskRepositoryReturning(final boolean result) {
        TaskRepository mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
        return mockTaskRepository;
    }
}