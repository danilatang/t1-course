package org.example.t1coursetask1.service;

import org.example.t1coursetask1.mapper.TaskMapper;
import org.example.t1coursetask1.repositories.TaskRepository;
import org.example.t1coursetask1.services.impl.TaskServiceImpl;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TaskServiceTest {
    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);
}
