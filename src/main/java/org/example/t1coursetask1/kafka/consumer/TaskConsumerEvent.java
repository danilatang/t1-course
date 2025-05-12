package org.example.t1coursetask1.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.t1coursetask1.dto.response.TaskDtoResponse;
import org.example.t1coursetask1.services.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskConsumerEvent {
    private final NotificationService notificationService;

    @KafkaListener(topics = "${spring.kafka.template.default-topic}", containerFactory = "kafkaListenerContainerFactory")
    public void consume(List<TaskDtoResponse> messages, Acknowledgment acknowledgment) {
        try {
            messages.forEach(taskDto -> {
                log.info("Task is updating: ID = {}, Status = {}", taskDto.getId(), taskDto.getStatus());
                notificationService.sendMessage(taskDto);
            });
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error while processing a batch of messages, skipping acknowledgment. Reason: {}", e.getMessage());
        }

    }
}
