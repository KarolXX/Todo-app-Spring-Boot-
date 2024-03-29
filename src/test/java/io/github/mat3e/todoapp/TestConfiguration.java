package io.github.mat3e.todoapp;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.*;

@Configuration
class TestConfiguration {
    @Bean
    @Primary
    @Profile("!integration")
    DataSource e2eTestDataSource() {
        var result = new DriverManagerDataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        result.setDriverClassName("org.h2.Driver");
        return result;
    }

    @Bean
    @Primary
    @Profile("integration") //TODO why "integration"??? it's just name of profile. it's just another way to declare our profile. before we did it in the application.properties file
    TaskRepository testRepo() {
        return new TaskRepository() {
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
            public Task save(Task entity) {
                int key = tasks.size() + 1;

                //set id inside entity (default id is the same as default int - 0)
                try {
                    var field = Task.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, key);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                //put entity inside map
                tasks.put(key, entity);
                return tasks.get(key);
            }

            // TODO - complete overriding
            @Override
            public Page<Task> findAll(Pageable page) {
                return null;
            }

            @Override
            public List<Task> findByDone(boolean done) {
                return null;
            }

            @Override
            public List<Task> findTasksByGroup_Id(Integer groupId) {
                return null;
            }

            @Override
            public List<Task> findTasksByDoneIsFalseAndDeadlineIsNullOrDoneIsFalseAndDeadlineIsLessThanEqualOrderByDeadline(LocalDateTime date) {
                return null;
            }

            @Override
            public boolean existsByDoneIsFalseAndGroup_Id(Integer groupId) {
                return false;
            }

        };
    }
}
