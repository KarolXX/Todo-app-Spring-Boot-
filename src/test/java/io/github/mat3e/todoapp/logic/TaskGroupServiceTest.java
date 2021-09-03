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
    @DisplayName("should throw IllegalStateException when group with given id is undone and it's tasks include at least one undone")
    void toggleUndoneGroup_undoneTaskExists_throwsIllegalStateException() {
        //given
        TaskGroupRepository mockTaskGroupRepository = taskGroupRepositoryReturning(false);
        //and
        TaskRepository mockTaskRepository = taskRepositoryReturning(true);
        //system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Do all the tasks first");
    }

    @Test
    @DisplayName("should throw IllegalStateException when group with given id is done and all the tasks are done")
    void toggleDoneGroup_noUndoneTaskExists_throwsIllegalStateException() {
        // given
        TaskGroupRepository mockTaskGroupRepository = taskGroupRepositoryReturning(true);
        // and
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        // system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("All tasks in the group are done");
    }

    @Test
    @DisplayName("should toggle group when no undone task exists and group with given id exists")
    void toggleGroup_noUndoneTaskExists_And_groupExists_toggleGroup() {
        //given
        boolean isGroupDone = false;
        var mockGroupRepository = taskGroupRepositoryReturning(isGroupDone);
        //and
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        //system under test
        var toTest = new TaskGroupService(mockGroupRepository, mockTaskRepository);

        //when
        toTest.toggleGroup(1);

        //then
        assertThat(mockGroupRepository.findById(1).get()
                .isDone()).isEqualTo(!isGroupDone);
    }

    private TaskRepository taskRepositoryReturning(final boolean result) {
        TaskRepository mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt()))
                .thenReturn(result);
        return mockTaskRepository;
    }

    private TaskGroupRepository taskGroupRepositoryReturning(final boolean isDone) {
        TaskGroup group = new TaskGroup();
        group.setDone(isDone);

        TaskGroupRepository mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(group));

        return mockTaskGroupRepository;
    }
}