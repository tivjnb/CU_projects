package me.cu.week7.project.service;

import me.cu.week7.project.exceptions.HttpStatusException;
import me.cu.week7.project.model.TopicDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    @InjectMocks
    private TopicService topicService;

    private TopicDto testTopic;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        testTopic = new TopicDto();
        testTopic.setId(1L);
        testTopic.setTitle("Test Topic");
        testTopic.setTitle("Test Description");

        Field topicsField = TopicService.class.getDeclaredField("topics");
        topicsField.setAccessible(true);
        topicsField.set(topicService, new HashMap<Long, TopicDto>());

        Field nextIdField = TopicService.class.getDeclaredField("nextId");
        nextIdField.setAccessible(true);
        nextIdField.set(topicService, new AtomicLong(0));
    }

    @Test
    void addTopic_ShouldReturnAddedTopic() {
        TopicDto result = topicService.addTopic(testTopic);

        assertEquals(testTopic, result);
        assertEquals(testTopic, topicService.getTopic(0L));
    }

    @Test
    void getTopic_ExistingId_ShouldReturnTopic() {
        topicService.addTopic(testTopic);

        TopicDto result = topicService.getTopic(0L);

        assertEquals(testTopic, result);
    }

    @Test
    void getTopic_NonExistingId_ShouldThrowException() {
        HttpStatusException exception = assertThrows(HttpStatusException.class, () -> {
            topicService.getTopic(999L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals("Topic not found with ID: 999", exception.getMessage());
    }

    @Test
    void updateTopic_ExistingId_ShouldUpdateAndReturnTopic() {
        topicService.addTopic(testTopic);

        TopicDto updatedTopic = new TopicDto();
        updatedTopic.setId(1L);
        updatedTopic.setTitle("Updated Topic");
        updatedTopic.setText("Updated Description");

        TopicDto result = topicService.updateTopic(updatedTopic, 0L);

        assertEquals(updatedTopic, result);
        assertEquals(updatedTopic, topicService.getTopic(0L));
    }

    @Test
    void updateTopic_NonExistingId_ShouldThrowException() {
        TopicDto updatedTopic = new TopicDto();

        HttpStatusException exception = assertThrows(HttpStatusException.class, () -> {
            topicService.updateTopic(updatedTopic, 999L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals("Topic not found with ID: 999", exception.getMessage());
    }


}
