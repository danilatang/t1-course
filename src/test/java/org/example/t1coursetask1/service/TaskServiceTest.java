package org.example.t1coursetask1.service;

import org.example.t1coursetask1.constants.TaskStatus;
import org.example.t1coursetask1.dto.request.TaskDtoRequest;
import org.example.t1coursetask1.dto.response.TaskDtoResponse;
import org.example.t1coursetask1.kafka.producer.TaskProducerEvent;
import org.example.t1coursetask1.mapper.TaskMapper;
import org.example.t1coursetask1.models.TaskEntity;
import org.example.t1coursetask1.repositories.TaskRepository;
import org.example.t1coursetask1.services.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskProducerEvent taskProducerEvent;

    private TaskEntity setTestTaskEntity() {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setTitle("titleTest");
        taskEntity.setDescription("descriptionTest");
        taskEntity.setUserId("userTest");
        taskEntity.setStatus(TaskStatus.NEW);
        return taskEntity;
    }

    private TaskDtoRequest setTestTaskDtoRequest() {
        TaskDtoRequest taskDtoRequest = new TaskDtoRequest();
        taskDtoRequest.setTitle("titleTest");
        taskDtoRequest.setDescription("descriptionTest");
        taskDtoRequest.setStatus(TaskStatus.NEW);
        return taskDtoRequest;
    }

    private TaskDtoResponse setTestTaskDtoResponse() {
        TaskDtoResponse taskDtoResponse = new TaskDtoResponse();
        taskDtoResponse.setId(1L);
        taskDtoResponse.setTitle("titleTest");
        taskDtoResponse.setDescription("descriptionTest");
        taskDtoResponse.setUserId("userTest");
        taskDtoResponse.setStatus(TaskStatus.NEW);
        return taskDtoResponse;
    }

    @Test
    void createTask_success() {
        TaskEntity taskEntity = setTestTaskEntity();
        TaskDtoRequest taskDtoRequest = setTestTaskDtoRequest();
        TaskDtoResponse taskDtoResponse = setTestTaskDtoResponse();

        when(taskMapper.toTaskEntity(taskDtoRequest)).thenReturn(taskEntity);
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
        when(taskMapper.toTaskDtoResponse(taskEntity)).thenReturn(taskDtoResponse);

        TaskDtoResponse response = taskService.createTask(taskDtoRequest);

        assertEquals(response.getId(), taskDtoResponse.getId());
        assertEquals(response.getTitle(), taskDtoResponse.getTitle());
        assertEquals(response.getDescription(), taskDtoResponse.getDescription());
        assertEquals(response.getUserId(), taskDtoResponse.getUserId());
        assertEquals(response.getStatus(), taskDtoResponse.getStatus());

        verify(taskMapper).toTaskEntity(taskDtoRequest);
        verify(taskRepository).save(taskEntity);
        verify(taskMapper).toTaskDtoResponse(taskEntity);
    }

    @Test
    void getTask_success() {
        TaskEntity taskEntity = setTestTaskEntity();
        TaskDtoResponse taskDtoResponse = setTestTaskDtoResponse();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskMapper.toTaskDtoResponse(taskEntity)).thenReturn(taskDtoResponse);

        TaskDtoResponse response = taskService.getTaskById("1");

        assertEquals(response.getId(), taskDtoResponse.getId());
        assertEquals(response.getTitle(), taskDtoResponse.getTitle());
        assertEquals(response.getDescription(), taskDtoResponse.getDescription());
        assertEquals(response.getUserId(), taskDtoResponse.getUserId());
        assertEquals(response.getStatus(), taskDtoResponse.getStatus());

        verify(taskRepository).findById(1L);
        verify(taskMapper).toTaskDtoResponse(taskEntity);
    }

    @Test
    void getTask_notFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskService.getTaskById("1"));

        verify(taskRepository).findById(1L);
    }

    @Test
    void updateTask_success() {
        TaskEntity taskEntityExisting = setTestTaskEntity();
        TaskEntity taskEntityUpdating = new TaskEntity(1L, "newTitle", "newDesc", "userTest", TaskStatus.NEW);
        TaskDtoRequest taskDtoRequestUpdate = new TaskDtoRequest("newTitle", "newDesc", TaskStatus.COMPLETED);
        TaskDtoResponse taskDtoResponse = setTestTaskDtoResponse();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntityExisting));
        when(taskMapper.toTaskEntityFromPutTaskDto(taskEntityExisting, taskDtoRequestUpdate)).thenReturn(taskEntityUpdating);
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskEntityUpdating);
        when(taskMapper.toTaskDtoResponse(taskEntityUpdating)).thenReturn(taskDtoResponse);

        TaskDtoResponse response = taskService.updateTask("1", taskDtoRequestUpdate);

        assertEquals(response.getId(), taskDtoResponse.getId());
        assertEquals(response.getTitle(), taskDtoResponse.getTitle());
        assertEquals(response.getDescription(), taskDtoResponse.getDescription());
        assertEquals(response.getUserId(), taskDtoResponse.getUserId());

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(taskEntityUpdating);
        verify(taskProducerEvent).produce(response);
    }

    @Test
    void updateTask_notFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskService.updateTask("1", setTestTaskDtoRequest()));

        verify(taskRepository).findById(1L);
    }

    @Test
    void getAllTasks_success() {
        TaskEntity taskEntity = setTestTaskEntity();
        TaskDtoResponse taskDtoResponse = setTestTaskDtoResponse();

        when(taskRepository.findAll()).thenReturn(List.of(taskEntity));
        when(taskMapper.toTaskDtoResponse(taskEntity)).thenReturn(taskDtoResponse);

        List<TaskDtoResponse> response = taskService.getTasks();

        assertEquals(1, response.size());
        assertEquals(response.get(0).getId(), taskDtoResponse.getId());
        assertEquals(response.get(0).getTitle(), taskDtoResponse.getTitle());
        assertEquals(response.get(0).getDescription(), taskDtoResponse.getDescription());
        assertEquals(response.get(0).getUserId(), taskDtoResponse.getUserId());

        verify(taskRepository).findAll();
        verify(taskMapper).toTaskDtoResponse(taskEntity);
    }

    @Test
    void deleteTask_success() {
        TaskEntity taskEntity = setTestTaskEntity();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

        taskService.deleteUser("1");

        verify(taskRepository).findById(1L);
        verify(taskRepository).delete(taskEntity);
    }

    @Test
    void deleteTask_notFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskService.deleteUser("1"));

        verify(taskRepository).findById(1L);
    }
}