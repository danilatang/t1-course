package org.example.t1coursetask1.repositories;

import org.example.t1coursetask1.models.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
