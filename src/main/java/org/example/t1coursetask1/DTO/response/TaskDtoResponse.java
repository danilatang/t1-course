package org.example.t1coursetask1.DTO.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDtoResponse {
    private Long id;

    private String title;

    private String description;

    private String userId;
}
