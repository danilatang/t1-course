package org.example.t1coursetask1.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.example.t1coursetask1.constants.TaskStatus;

@Getter
@Setter
public class TaskDtoResponse {
    private Long id;

    private String title;

    private String description;

    private String userId;

    private TaskStatus status;
}
