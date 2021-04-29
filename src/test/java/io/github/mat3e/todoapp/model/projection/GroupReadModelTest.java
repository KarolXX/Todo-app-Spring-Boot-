package io.github.mat3e.todoapp.model.projection;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelTest {
    @Test
    @DisplayName("should create null deadline for group when no task deadline")
    void constructor_noDeadlines_createsNullDeadline() {
        // given
        var source = new TaskGroup();
        source.setDescription("foobar");
        source.setTasks(Set.of(new Task("example", null)));

        // when
        var result = new GroupReadModel(source);

        // then
        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }
}