package io.github.mat3e.todoapp.controller;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerE2ETestGetTaskById {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TaskRepository repo;

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
}
