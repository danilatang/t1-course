package org.example.t1coursetask1.service;

import org.example.t1coursetask1.config.TestContainerConfig;
import org.example.t1coursetask1.constants.TaskStatus;
import org.example.t1coursetask1.dto.request.TaskDtoRequest;
import org.example.t1coursetask1.dto.response.TaskDtoResponse;
import org.example.t1coursetask1.repositories.TaskRepository;
import org.example.t1coursetask1.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TaskServiceIntegrationTest extends TestContainerConfig {
    @Autowired
    private TaskService taskService;

    @Autowired
    TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void createTask_shouldReturnCreatedStatusAndBody() throws Exception {
        TaskDtoRequest request = new TaskDtoRequest();
        request.setTitle("Title");
        request.setDescription("Description");
        request.setStatus(TaskStatus.NEW);

        TaskDtoResponse response = taskService.createTask(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertTrue(taskRepository.findById(response.getId()).isPresent());
    }
}
