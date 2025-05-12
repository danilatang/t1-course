package org.example.t1coursetask1.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.t1coursetask1.constants.TaskStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDtoRequest {
    private String title;

    private String description;

    private TaskStatus status;
}
