package io.github.mat3e.todoapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mat3e.todoapp.model.Task;
import io.github.mat3e.todoapp.model.TaskRepository;
import net.minidev.json.parser.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class TaskControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository repo;

    @Test
    void httpGet_returnsGivenTask() throws Exception {
        //given
        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();

        //when + then
        mockMvc.perform(get("/tasks/" + id))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void httpGet_returnsAllTasks() throws Exception {
        //when + then
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void httpPost_createsTask_returnsCreatedTask() throws Exception {
        //given
        //Task task = new Task("testing POST method", LocalDateTime.now()); I would can use it and asJsonString method to set request body (content())-NOT WORK!!! but below is alternative way
        String id = String.valueOf(repo.findAll().size());
        String jsonString = new JSONObject()
                .put("id", id)
                .put("description", "testing POST method")
                .put("done", false)
                .put("deadline", "2021-04-20T12:43:20")
                .toString();
        System.out.println(jsonString);

        //when + then
        mockMvc.perform(post("/tasks")
           .content(jsonString)
           .contentType(MediaType.APPLICATION_JSON)
           .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

   @Test
   void httpPut_updatesTask() throws Exception {
       //given
       Task task = new Task("testing PUT", LocalDateTime.parse("2002-06-18T12:43:11"));
       int id = repo.save(task).getId();
       //and
       String jsonString = new JSONObject()
               .put("id", String.valueOf(id))
               .put("done", false)
               .put("description", "updated testing PUT")
               .put("deadline", "2021-04-20T12:43:20")
               .toString();

       //when + then
       mockMvc.perform(put("/tasks/" + id)
               .content(jsonString)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
   }

    @Test
    void httpPut_createsTask_returnsCreatedTask() throws Exception {
        // given
        String description = "testing PUT method";
        String jsonString = new JSONObject()
                .put("description", description)
                .put("deadline", "2021-04-20T12:43:20")
                .toString();
        // and
        int anotherId = repo.findAll().size() + 1;

        // when + then
        mockMvc.perform(put("/tasks/" + anotherId)
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }









    //NOT WORK!!!
//   private static String asJsonString(final Task task) {
//       try {
//           return new ObjectMapper().writeValueAsString(task);
//       } catch (JsonProcessingException e) {
//           throw new RuntimeException();
//       }
//   }
}
