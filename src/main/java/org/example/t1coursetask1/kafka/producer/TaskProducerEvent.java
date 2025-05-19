package org.example.t1coursetask1.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.t1coursetask1.dto.response.TaskDtoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskProducerEvent {
    private final KafkaTemplate kafkaTemplate;

    @Value("${spring.kafka.template.default-topic}")
    private String topic;

    public void produce(TaskDtoResponse taskDtoResponse) {
        try {
            log.info("Sending status change to topic {}", topic);
            kafkaTemplate.send(topic, String.valueOf(taskDtoResponse.getId()), taskDtoResponse);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
