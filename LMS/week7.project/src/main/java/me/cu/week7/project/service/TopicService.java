package me.cu.week7.project.service;

import me.cu.week7.project.exceptions.HttpStatusException;
import me.cu.week7.project.model.ProblemDto;
import me.cu.week7.project.model.TopicDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TopicService {
    private final HashMap<Long, TopicDto> topics = new HashMap<>();
    private final AtomicLong nextId = new AtomicLong(0);
    private final ProblemService problemService;

    @Autowired
    public TopicService(ProblemService problemService) {
        this.problemService = problemService;
    }

    public TopicDto getTopic(Long id){
        TopicDto topic = topics.get(id);
        if (topic == null) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Topic not found with ID: " + id);
        }
        return topic;
    }

    public TopicDto addTopic(TopicDto topicDto){
        long lastId = nextId.getAndIncrement();
        this.topics.put(
                lastId,
                topicDto
        );
        return topicDto;
    }

    public TopicDto updateTopic(TopicDto topicDto, Long id){
        TopicDto topic = topics.get(id);
        if (topic == null) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Topic not found with ID: " + id);
        }
        this.topics.put(id, topicDto);
        return topicDto;
    }


    public void deleteTopic(Long id){
        TopicDto topic = topics.get(id);
        if (topic == null) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Topic not found with ID: " + id);
        }
        this.topics.remove(id);
    }

    public void AddProblem(Long topicId, ProblemDto problemDto) {
        TopicDto topic = topics.get(topicId);
        if (topic == null) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Topic not found with ID: " + topicId);
        }

        problemService.addProblem(problemDto);
        ArrayList<Long> problems = topics.get(topicId).getProblemsId();
        problems.add(problemDto.getId());
        topics.get(topicId).setProblemsId(problems);
    }
}

