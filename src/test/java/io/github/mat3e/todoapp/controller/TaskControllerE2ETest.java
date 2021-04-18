package io.github.mat3e.todoapp.controller;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    TaskRepository repo;

    @Test
    void httpGet_returnsAllTasks() {
        //given
        int initialSize = repo.findAll().size();
        repo.save(new Task("foo", LocalDateTime.now()));
        repo.save(new Task("bar", LocalDateTime.now()));

        //when
        Task[] result = testRestTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

        //then
        assertThat(result).hasSize(initialSize + 2);
    }

    @Test
    void httpGet_returnsTaskById() {
        //given
        String description = "foo";
        LocalDateTime deadline = LocalDateTime.parse("2021-03-10T20:42:30.734581");
        int id = repo.save(new Task(description, deadline)).getId();

        //when
        var result = testRestTemplate.getForObject("http://localhost:" + port + "/tasks/" + id , Task.class);

        //then
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getDeadline()).isEqualTo(deadline);
    }

    @Test
    void httpPost_createsTask_returnsCreatedTask() {
        //given
        String description = "POST E2E";
        Task task = new Task(description, LocalDateTime.now());

        //when
        var result = testRestTemplate.postForEntity("http://localhost:" + port + "/tasks", task, Task.class);

        //then
        assertThat(result.getBody().getDescription()).isEqualTo(description);
    }

    @Test
    void httpPut_updatesTask() {
        // given
        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();
        // and
        String updatedDescription = "bar";
        Task updatedTask = new Task(updatedDescription, LocalDateTime.now());

        // when
        testRestTemplate.put("http://localhost:" + port + "/tasks/{id}", updatedTask, String.valueOf(id));

        // then
        var result = repo.findById(id).get();
        assertThat(result.getDescription()).isEqualTo(updatedDescription);
    }

    // Test for case when the client can create a new resource or update an existing one via PUT
    @Test
    void httpPut_createsTask_returnsTask() {
        // given
        Integer anotherId = repo.findAll().size() + 1;
        // and
        String name = "created via PUT";
        Task task = new Task(name, LocalDateTime.now());
        // and

        // when
        testRestTemplate.put("http://localhost:" + port + "/tasks/{id}", task, anotherId);

        // then
        var result = repo.findById(anotherId)
                .orElseThrow(() -> new NoSuchElementException("no such element in DB stored in memory"));
        assertThat(result.getDescription()).isEqualTo(name);
    }
}