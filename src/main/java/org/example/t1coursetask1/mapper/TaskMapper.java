package org.example.t1coursetask1.mapper;

import org.example.t1coursetask1.dto.request.TaskDtoRequest;
import org.example.t1coursetask1.dto.response.TaskDtoResponse;
import org.example.t1coursetask1.models.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "status", ignore = true)
    TaskEntity toTaskEntity(TaskDtoRequest createTaskDtoRequest);

    TaskDtoResponse toTaskDtoResponse(TaskEntity taskEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "status", ignore = true)
    TaskEntity toTaskEntityFromPutTaskDto(@MappingTarget TaskEntity taskEntity, TaskDtoRequest createTaskDtoRequest);
}
