package io.github.mat3e.todoapp.controller;

import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class TaskControllerIntegrationTestGetAllTasks {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository repo;

    @Test
    void httpGet_integrationTest_returnsAllTasks() throws Exception {
        //when + then
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
