package org.example.t1coursetask1.controllers;

import lombok.RequiredArgsConstructor;
import org.example.t1coursetask1.dto.request.TaskDtoRequest;
import org.example.t1coursetask1.dto.response.TaskDtoResponse;
import org.example.t1coursetask1.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDtoResponse createTask(@RequestBody TaskDtoRequest taskDtoRequest) {
        return taskService.createTask(taskDtoRequest);
    }

    @GetMapping("/{id}")
    public TaskDtoResponse getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public TaskDtoResponse updateTask(@PathVariable String id, @RequestBody TaskDtoRequest taskDtoRequest) {
        return taskService.updateTask(id, taskDtoRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable String id) {
        taskService.deleteUser(id);
    }

    @GetMapping
    public List<TaskDtoResponse> getTasks() {
        return taskService.getTasks();
    }
}
