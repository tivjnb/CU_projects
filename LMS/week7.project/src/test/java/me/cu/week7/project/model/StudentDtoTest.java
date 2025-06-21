package me.cu.week7.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDtoTest {
    private StudentDto studentDto;

    @BeforeEach
    void setUp() {
        studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setLogin("login");
        studentDto.setFirstName("John");
        studentDto.setLastName("Doe");
        studentDto.setPhoneNumber("1234567890");
        studentDto.setProblemsId(new ArrayList<>());
    }

    @Test
    void gettersAndSettersTest() {
        assertEquals(1L, studentDto.getId());
        assertEquals("login", studentDto.getLogin());
        assertEquals("John", studentDto.getFirstName());
        assertEquals("Doe", studentDto.getLastName());
        assertEquals("1234567890", studentDto.getPhoneNumber());
        assertNotNull(studentDto.getProblemsId());
        assertTrue(studentDto.getProblemsId().isEmpty());
    }

    @Test
    void solveProblemTest() {
        long problemId = 100L;
        studentDto.solveProblem(problemId);

        assertEquals(1, studentDto.getProblemsId().size());
        assertTrue(studentDto.getProblemsId().contains(100L));
    }

}
