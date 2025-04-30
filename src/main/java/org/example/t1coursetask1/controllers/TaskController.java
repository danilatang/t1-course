package org.example.t1coursetask1.controllers;

import lombok.RequiredArgsConstructor;
import org.example.t1coursetask1.DTO.request.TaskDtoRequest;
import org.example.t1coursetask1.DTO.response.TaskDtoResponse;
import org.example.t1coursetask1.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDtoResponse> createTask(@RequestBody TaskDtoRequest taskDtoRequest) {
        return new ResponseEntity<>(taskService.createTask(taskDtoRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDtoResponse> getTaskById(@PathVariable String id) {
        return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDtoResponse> updateTask(@PathVariable String id, @RequestBody TaskDtoRequest taskDtoRequest) {
        return new ResponseEntity<>(taskService.updateTask(id, taskDtoRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        taskService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TaskDtoResponse>> getTasks() {
        return new ResponseEntity<>(taskService.getTasks(), HttpStatus.OK);
    }
}
