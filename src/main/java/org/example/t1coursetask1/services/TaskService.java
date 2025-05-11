package org.example.t1coursetask1.services;

import org.example.t1coursetask1.dto.request.TaskDtoRequest;
import org.example.t1coursetask1.dto.response.TaskDtoResponse;
import java.util.List;

public interface TaskService {
    TaskDtoResponse createTask(TaskDtoRequest taskDtoRequest);

    TaskDtoResponse getTaskById(String taskId);

    TaskDtoResponse updateTask(String taskId, TaskDtoRequest taskDtoRequest);

    List<TaskDtoResponse> getTasks();

    void deleteUser(String id);
}
