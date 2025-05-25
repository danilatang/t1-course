package org.example.t1coursetask1.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class TestContainerConfig {
    private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:16");
    private static final DockerImageName KAFKA_IMAGE = DockerImageName.parse("apache/kafka:latest");

    @Container
    protected static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRES_IMAGE)
            .withDatabaseName("testdb")
            .withExposedPorts(5432);

    @Container
    protected static final KafkaContainer kafka = new KafkaContainer(KAFKA_IMAGE)
            .withExposedPorts(9092);

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.kafka.template.default-topic", () -> "task-status-topic");
    }
}
