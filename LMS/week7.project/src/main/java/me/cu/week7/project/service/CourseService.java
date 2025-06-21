package me.cu.week7.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import me.cu.week7.project.model.TopicDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import me.cu.week7.project.exceptions.HttpStatusException;
import me.cu.week7.project.model.CourseDto;
import me.cu.week7.project.model.StudentDto;

@Service
public class CourseService {

    private final Map<Long, CourseDto> courseMap = new HashMap<>();
    private final AtomicLong nextId = new AtomicLong(0); // Auto id generator (incremental)

    private final StudentService studentService;
    private final TopicService topicService;

    @Autowired
    public CourseService(StudentService studentService, TopicService topicService) {
        this.studentService = studentService;
        this.topicService = topicService;
    }

    public CourseDto createCourse(CourseDto courseDto) {
        long id = nextId.getAndIncrement();
        courseDto.setId(id);
        courseDto.setStudentsId(new ArrayList<>());
        courseMap.put(id, courseDto);
        return courseDto;
    }

    public CourseDto getCourse(Long id) {
        CourseDto course = courseMap.get(id);
        if (course == null) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Course not found with ID: " + id);
        }
        return course;
    }

    public void deleteCourse(Long id) {
        CourseDto course = courseMap.remove(id);
        if (course == null) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Course not found with ID: " + id);
        }
    }

    public void enrollStudent(Long courseId, Long studentId) {

        CourseDto course = courseMap.get(courseId);
        if (course == null) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Course not found with ID: " + courseId);
        }

        StudentDto student = studentService.get(studentId);
        if(student == null){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Student not found with ID: " + studentId);
        }

        if (course.getStudentsId().contains(studentId)) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Student with ID: " + studentId + " is already enrolled in course with ID: " + courseId);
        }

        course.getStudentsId().add(studentId);
    }

    public void unenrollStudent(Long courseId, Long studentId) {

        CourseDto course = courseMap.get(courseId);
        if (course == null) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Course not found with ID: " + courseId);
        }


        StudentDto student = studentService.get(studentId);
        if(student == null){
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Student not found with ID: " + studentId);
        }

        if (!course.getStudentsId().contains(studentId)) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Student with ID: " + studentId + " is not enrolled in course with ID: " + courseId);
        }

        course.getStudentsId().remove(studentId);
    }

    public List<CourseDto> getAllCourses() {
        return new ArrayList<>(courseMap.values());
    }

    public void AddTopic(Long courseId, TopicDto topic) {
        if (!courseMap.containsKey(courseId)) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Course not found with ID: " + courseId);
        }

        topicService.addTopic(topic);
        ArrayList<Long> topics = courseMap.get(courseId).getTopicsId();
        topics.add(topic.getId());
        courseMap.get(courseId).setTopicsId(topics);

    }

    public void RemoveTopic(Long courseId, Long topicId) {
        if (!courseMap.containsKey(courseId)) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Course not found with ID: " + courseId);
        }

        topicService.deleteTopic(topicId);
    }

}