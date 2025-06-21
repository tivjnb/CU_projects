package me.cu.week7.project.model;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class CourseDtoTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testGetterAndSetter() {
        Long id = 1L;
        String title = "Title";
        String description = "Description";
        ArrayList<Long> topicsId = new ArrayList<>();
        topicsId.add(10L);
        ArrayList<Long> studentsId = new ArrayList<>();
        studentsId.add(100L);

        CourseDto courseDto = new CourseDto();
        courseDto.setId(id);
        courseDto.setTitle(title);
        courseDto.setDescription(description);
        courseDto.setTopicsId(topicsId);
        courseDto.setStudentsId(studentsId);

        assertEquals(id, courseDto.getId());
        assertEquals(title, courseDto.getTitle());
        assertEquals(description, courseDto.getDescription());
        assertEquals(topicsId, courseDto.getTopicsId());
        assertEquals(studentsId, courseDto.getStudentsId());
    }

    @Test
    void testValidation_ValidTitle() {
        CourseDto courseDto = new CourseDto();
        courseDto.setTitle("Valid Title");
        
        Set<ConstraintViolation<CourseDto>> violations = validator.validate(courseDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidation_EmptyTitle() {
        CourseDto courseDto = new CourseDto();
        courseDto.setTitle("");
        
        Set<ConstraintViolation<CourseDto>> violations = validator.validate(courseDto);
        assertFalse(violations.isEmpty());
        
        ConstraintViolation<CourseDto> violation = violations.iterator().next();
        assertEquals("title cannot be empty", violation.getMessage());
    }

    @Test
    void testValidation_NullTitle() {
        CourseDto courseDto = new CourseDto();
        courseDto.setTitle(null);
        
        Set<ConstraintViolation<CourseDto>> violations = validator.validate(courseDto);
        assertFalse(violations.isEmpty());
        
        ConstraintViolation<CourseDto> violation = violations.iterator().next();
        assertEquals("title cannot be empty", violation.getMessage());
    }
}