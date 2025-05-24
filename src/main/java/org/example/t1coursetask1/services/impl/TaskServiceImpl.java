package org.example.t1coursetask1.services.impl;

import com.example.annotation.HttpLogging;
import lombok.RequiredArgsConstructor;
import org.example.t1coursetask1.constants.TaskStatus;
import org.example.t1coursetask1.dto.request.TaskDtoRequest;
import org.example.t1coursetask1.dto.response.TaskDtoResponse;
import org.example.t1coursetask1.kafka.producer.TaskProducerEvent;
import org.example.t1coursetask1.mapper.TaskMapper;
import org.example.t1coursetask1.models.TaskEntity;
import org.example.t1coursetask1.repositories.TaskRepository;
import org.example.t1coursetask1.services.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    private final TaskProducerEvent taskProducerEvent;

    @Transactional
    @HttpLogging
    public TaskDtoResponse createTask(TaskDtoRequest taskDtoRequest) {
        TaskEntity task = taskMapper.toTaskEntity(taskDtoRequest);
        task.setUserId("user");
        task.setStatus(TaskStatus.NEW);
        taskRepository.save(task);

        return taskMapper.toTaskDtoResponse(task);
    }

    @HttpLogging
    public TaskDtoResponse getTaskById(String taskId) {
        return taskMapper.toTaskDtoResponse(taskRepository.findById(Long.valueOf(taskId)).orElseThrow());
    }

    @Transactional
    @HttpLogging
    public TaskDtoResponse updateTask(String taskId, TaskDtoRequest taskDtoRequest) {
        TaskEntity existingTask = taskRepository.findById(Long.valueOf(taskId))
                .orElseThrow();
        TaskEntity task = taskMapper.toTaskEntityFromPutTaskDto(existingTask, taskDtoRequest);
        if (taskDtoRequest.getStatus() != null && taskDtoRequest.getStatus() != task.getStatus()) {
            task.setStatus(taskDtoRequest.getStatus());
            taskRepository.save(task);
            taskProducerEvent.produce(taskMapper.toTaskDtoResponse(task));
        }
        return taskMapper.toTaskDtoResponse(task);
    }

    @HttpLogging
    public List<TaskDtoResponse> getTasks() {
        List<TaskEntity> tasks = taskRepository.findAll();
        return tasks.stream().map(taskMapper::toTaskDtoResponse).toList();
    }

    @HttpLogging
    public void deleteUser(String id) {
        TaskEntity task = taskRepository.findById(Long.valueOf(id)).orElseThrow();
        taskRepository.delete(task);
    }
}
