package me.cu.week7.project.service;

import me.cu.week7.project.exceptions.HttpStatusException;
import me.cu.week7.project.model.CourseDto;
import me.cu.week7.project.model.StudentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private CourseService courseService;

    private CourseDto testCourse;
    private StudentDto testStudent;

    @BeforeEach
    void setUp() {
        testCourse = new CourseDto();
        testCourse.setTitle("Test Course");

        testStudent = new StudentDto();
        testStudent.setId(1L);
    }

    @Test
    void createCourse_ValidCourse_ReturnsCreatedCourse() {
        CourseDto result = courseService.createCourse(testCourse);

        assertNotNull(result.getId());
        assertEquals(testCourse.getTitle(), result.getTitle());
        assertNotNull(result.getStudentsId());
        assertTrue(result.getStudentsId().isEmpty());
    }

    @Test
    void getCourse_ExistingCourse_ReturnsCourse() {
        CourseDto createdCourse = courseService.createCourse(testCourse);
        Long courseId = createdCourse.getId();

        CourseDto retrievedCourse = courseService.getCourse(courseId);

        assertEquals(createdCourse, retrievedCourse);
    }

    @Test
    void getCourse_NonExistingCourse_ThrowsException() {
        HttpStatusException exception = assertThrows(HttpStatusException.class, () ->
                courseService.getCourse(999L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertTrue(exception.getMessage().contains("Course not found"));
    }

    @Test
    void deleteCourse_ExistingCourse_RemovesCourse() {
        CourseDto createdCourse = courseService.createCourse(testCourse);
        Long courseId = createdCourse.getId();

        courseService.deleteCourse(courseId);

        HttpStatusException exception = assertThrows(HttpStatusException.class, () ->
                courseService.getCourse(courseId)
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void deleteCourse_NonExistingCourse_ThrowsException() {
        HttpStatusException exception = assertThrows(HttpStatusException.class, () ->
                courseService.deleteCourse(999L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertTrue(exception.getMessage().contains("Course not found"));
    }

    @Test
    void enrollStudent_ValidIds_EnrollsStudent() {
        CourseDto createdCourse = courseService.createCourse(testCourse);
        Long courseId = createdCourse.getId();
        Long studentId = 1L;

        when(studentService.get(studentId)).thenReturn(testStudent);

        courseService.enrollStudent(courseId, studentId);

        assertTrue(courseService.getCourse(courseId).getStudentsId().contains(studentId));
    }

    @Test
    void enrollStudent_AlreadyEnrolled_ThrowsException() {
        CourseDto createdCourse = courseService.createCourse(testCourse);
        Long courseId = createdCourse.getId();
        Long studentId = 1L;

        when(studentService.get(studentId)).thenReturn(testStudent);

        courseService.enrollStudent(courseId, studentId);

        HttpStatusException exception = assertThrows(HttpStatusException.class, () ->
                courseService.enrollStudent(courseId, studentId)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertTrue(exception.getMessage().contains("already enrolled"));
    }

    @Test
    void unenrollStudent_EnrolledStudent_RemovesStudent() {
        CourseDto createdCourse = courseService.createCourse(testCourse);
        Long courseId = createdCourse.getId();
        Long studentId = 1L;

        when(studentService.get(studentId)).thenReturn(testStudent);

        courseService.enrollStudent(courseId, studentId);
        courseService.unenrollStudent(courseId, studentId);

        assertFalse(courseService.getCourse(courseId).getStudentsId().contains(studentId));
    }
}