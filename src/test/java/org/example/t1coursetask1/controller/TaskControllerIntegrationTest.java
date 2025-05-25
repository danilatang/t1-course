package org.example.t1coursetask1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.t1coursetask1.config.TestContainerConfig;
import org.example.t1coursetask1.constants.TaskStatus;
import org.example.t1coursetask1.dto.request.TaskDtoRequest;
import org.example.t1coursetask1.dto.response.TaskDtoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest extends TestContainerConfig {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskDtoRequest setTestTaskDtoRequest() {
        TaskDtoRequest taskDtoRequest = new TaskDtoRequest();
        taskDtoRequest.setTitle("titleTest");
        taskDtoRequest.setDescription("descriptionTest");
        taskDtoRequest.setStatus(TaskStatus.NEW);
        return taskDtoRequest;
    }

    @Test
    void createTask_shouldReturnCreated() throws Exception {
        TaskDtoRequest request = setTestTaskDtoRequest();

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("titleTest"))
                .andExpect(jsonPath("$.description").value("descriptionTest"))
                .andExpect(jsonPath("$.userId").value("user"))
                .andExpect(jsonPath("$.status").value(TaskStatus.NEW.toString()));
    }

    @Test
    void getTasks_shouldReturnList() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());
    }

    @Test
    void getTaskById_shouldReturnTask() throws Exception {
        TaskDtoRequest request = setTestTaskDtoRequest();
        TaskDtoResponse taskDtoResponse;

        MvcResult createResult = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        taskDtoResponse = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), TaskDtoResponse.class);

        mockMvc.perform(get("/tasks/" + taskDtoResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskDtoResponse.getId()))
                .andExpect(jsonPath("$.title").value(taskDtoResponse.getTitle()))
                .andExpect(jsonPath("$.description").value(taskDtoResponse.getDescription()))
                .andExpect(jsonPath("$.userId").value(taskDtoResponse.getUserId()));
    }

    @Test
    void getTaskById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/tasks/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTask_shouldReturnUpdated() throws Exception {
        TaskDtoRequest request = setTestTaskDtoRequest();
        String response = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        long id = objectMapper.readTree(response).get("id").asLong();

        TaskDtoRequest update = new TaskDtoRequest("updated", "desc", TaskStatus.RUNNING);
        mockMvc.perform(put("/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("updated"));
    }

    @Test
    void updateTask_shouldReturnNotFound() throws Exception {
        long id = 9999L;
        mockMvc.perform(put("/tasks/" + id))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTask_shouldReturnNoContent() throws Exception {
        TaskDtoRequest request = setTestTaskDtoRequest();
        String response = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/tasks/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/tasks/9999"))
                .andExpect(status().isNotFound());
    }
}
