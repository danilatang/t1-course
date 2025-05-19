package org.example.t1coursetask1.services;

import lombok.RequiredArgsConstructor;
import org.example.t1coursetask1.dto.response.TaskDtoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender mailSender;

    @Value("${spring.notification.email.sender}")
    private String sender;

    @Value("${spring.notification.email.recipient}")
    private String recipient;

    public void sendMessage(TaskDtoResponse taskDtoResponse) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setFrom(sender);
        message.setSubject("Status was updated");
        message.setText("Task with ID: " + taskDtoResponse.getId() + " got new status: " + taskDtoResponse.getStatus());
        mailSender.send(message);
    }

}
