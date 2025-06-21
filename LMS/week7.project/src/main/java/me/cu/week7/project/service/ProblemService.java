package me.cu.week7.project.service;

import me.cu.week7.project.exceptions.HttpStatusException;
import me.cu.week7.project.model.ProblemDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProblemService {
    private final HashMap<Long, ProblemDto> problems = new HashMap<>();
    private final AtomicLong nextId = new AtomicLong(0);

    public ProblemDto getProblem(Long id){
        ProblemDto problem = problems.get(id);
        if (problem == null) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Topic not found with ID: " + id);
        }
        return problem;
    }

    public ProblemDto addProblem(ProblemDto problemDto){
        long lastId = nextId.getAndIncrement();
        this.problems.put(
                lastId,
                problemDto
        );
        return problemDto;
    }


    public void deleteProblem(Long id){
        ProblemDto problem = this.problems.get(id);
        if (problem == null) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Topic not found with ID: " + id);
        }
        this.problems.remove(id);
    }
}
