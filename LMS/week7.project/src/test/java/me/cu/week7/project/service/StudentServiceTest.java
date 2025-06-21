package me.cu.week7.project.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpStatus;

import me.cu.week7.project.exceptions.HttpStatusException;
import me.cu.week7.project.model.ProblemDto;
import me.cu.week7.project.model.StudentDto;

public class StudentServiceTest {
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        ProblemService problemService = mock(ProblemService.class);
        when(problemService.getProblem(1L)).thenReturn(new ProblemDto());
        when(problemService.getProblem(999L)).thenThrow(new IllegalArgumentException("Задача с ID 999 не найдена"));

        studentService = new StudentService(problemService);
    }

    @Test
    public void testGetAll() {
        // Добавляем студентов в сервис
        StudentDto student1 = new StudentDto();
        student1.setFirstName("John");
        student1.setLastName("Doe");

        StudentDto student2 = new StudentDto();
        student2.setFirstName("Jane");
        student2.setLastName("Smith");

        studentService.add(student1);
        studentService.add(student2);

        ArrayList<StudentDto> allStudents = studentService.getAll();

        assertEquals(2, allStudents.size());
        assertTrue(allStudents.contains(student1));
        assertTrue(allStudents.contains(student2));
    }

    @Test
    public void testGetExistingStudent() {
        StudentDto student = new StudentDto();
        student.setFirstName("John");
        student.setLastName("Doe");
        studentService.add(student);

        StudentDto retrievedStudent = studentService.get(0L);

        assertNotNull(retrievedStudent);
        assertEquals("John", retrievedStudent.getFirstName());
        assertEquals("Doe", retrievedStudent.getLastName());
    }


    @Test
    public void testGetNotExistingStudent() {
        HttpStatusException exception = assertThrows(HttpStatusException.class, () -> {
            studentService.get(999L); // ID, которого нет в сервисе
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals("Student with id = 999 not found", exception.getMessage());
    }

    @Test
    public void testDeleteExistingStudent() {
        StudentDto student = new StudentDto();
        student.setFirstName("John");
        student.setLastName("Doe");
        studentService.add(student);

        studentService.delete(0L);

        assertEquals(0, studentService.getAll().size());
    }

    @Test
    public void testDeleteNonExistingStudent() {
        HttpStatusException exception = assertThrows(HttpStatusException.class, () -> {
            studentService.delete(999L); // ID, которого нет в сервисе
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals("Student with id = 999 not found", exception.getMessage());
    }

    @Test
    public void testPatchNotTasks() {
        StudentDto student = new StudentDto();
        student.setFirstName("John");
        student.setLastName("Doe");
        studentService.add(student);


        StudentDto updStudent = new StudentDto();
        updStudent.setFirstName("Not John");
        updStudent.setLastName("Not Doe");
        updStudent.setLogin("NotJohnNotDoe6666");
        updStudent.setPhoneNumber("14683955");

        studentService.update(0L, updStudent);
        StudentDto retrievedStudent = studentService.get(0L);

        assertNotNull(retrievedStudent);
        assertEquals("Not John", retrievedStudent.getFirstName());
        assertEquals("Not Doe", retrievedStudent.getLastName());
        assertEquals("NotJohnNotDoe6666", retrievedStudent.getLogin());
        assertEquals("14683955", retrievedStudent.getPhoneNumber());
    }

    @Test
    public void testPatchWithExistingTasks() {
        StudentDto student = new StudentDto();
        student.setFirstName("John");
        student.setLastName("Doe");
        studentService.add(student);

        StudentDto updStudent = new StudentDto();

        updStudent.setProblemsId(new ArrayList<>(List.of(1L)));
        studentService.update(0L, updStudent);

        StudentDto retrievedStudent = studentService.get(0L);

        assertNotNull(retrievedStudent);
        assertTrue(retrievedStudent.getProblemsId().contains(1L));
    }

    @Test
    public void testPatchWithNotExistingTasks() {
        StudentDto student = new StudentDto();
        student.setFirstName("John");
        student.setLastName("Doe");
        studentService.add(student);

        StudentDto updStudent = new StudentDto();

        updStudent.setProblemsId(new ArrayList<>(List.of(999L)));
        HttpStatusException exception = assertThrows(HttpStatusException.class, () -> {
            studentService.update(0L, updStudent); // ID, которого нет в сервисе
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals("Problem with Id = 999 does not exists", exception.getMessage());
    }
}
