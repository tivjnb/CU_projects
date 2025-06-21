package me.cu.week7.project.service;

import me.cu.week7.project.exceptions.HttpStatusException;
import me.cu.week7.project.model.ProblemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class ProblemServiceTest {

    private ProblemService problemService;
    private ProblemDto testProblem;

    @BeforeEach
    void setUp() {
        problemService = new ProblemService();
        testProblem = new ProblemDto();
    }

    @Test
    void getProblem_ExistingProblem_ReturnsProblem() {
        ProblemDto addedProblem = problemService.addProblem(testProblem);
        Long id = 0L;

        ProblemDto retrievedProblem = problemService.getProblem(id);

        assertEquals(addedProblem, retrievedProblem);
    }

    @Test
    void getProblem_NonExistingProblem_ThrowsException() {
        HttpStatusException exception = assertThrows(HttpStatusException.class, () ->
                problemService.getProblem(999L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertTrue(exception.getMessage().contains("Topic not found"));
    }

    @Test
    void addProblem_ValidProblem_ReturnsSameProblem() {
        ProblemDto result = problemService.addProblem(testProblem);

        assertEquals(testProblem, result);
    }

    @Test
    void deleteProblem_ExistingProblem_RemovesProblem() {
        problemService.addProblem(testProblem);
        Long id = 0L;

        problemService.deleteProblem(id);

        HttpStatusException exception = assertThrows(HttpStatusException.class, () ->
                problemService.getProblem(id)
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void deleteProblem_NonExistingProblem_ThrowsException() {
        HttpStatusException exception = assertThrows(HttpStatusException.class, () ->
                problemService.deleteProblem(999L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertTrue(exception.getMessage().contains("Topic not found"));
    }
}