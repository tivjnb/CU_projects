package me.cu.week7.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import me.cu.week7.project.exceptions.HttpStatusException;
import me.cu.week7.project.model.StudentDto;

@Service
public class StudentService {
    private final HashMap<Long, StudentDto> students = new HashMap<>();
    private final AtomicLong nextId = new AtomicLong(0); // Auto id generator (incremental)

    private final ProblemService problemService;

    @Autowired
    public StudentService (ProblemService problemService){
        this.problemService = problemService;
    }

    public ArrayList<StudentDto> getAll() {
        return new ArrayList<>(students.values());
    }

    public StudentDto get(long id) {
        if (!students.containsKey(id)) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Student with id = %s not found".formatted(id));
        }
        return students.get(id);
    }

    public void add(StudentDto student) {
        long lastId = nextId.getAndIncrement();
        student.setId(lastId);
        if (student.getProblemsId() == null) {
            student.setProblemsId(new ArrayList<>());
        }
        students.put(lastId, student);
    }

    public void update(long id, StudentDto updStudent) {
        if (!students.containsKey(id)) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Student with id = %s not found".formatted(id));
        }
        StudentDto student = students.get(id);
        // login
        if (updStudent.getLogin() != null && !updStudent.getLogin().isBlank()) {
            student.setLogin(updStudent.getLogin());
        }

        // first name
        if (updStudent.getFirstName() != null && !updStudent.getFirstName().isBlank()) {
            student.setFirstName(updStudent.getFirstName());
        }

        //last name
        if (updStudent.getLastName() != null && !updStudent.getLastName().isBlank()) {
            student.setLastName(updStudent.getLastName());
        }

        if (updStudent.getPhoneNumber() != null && !updStudent.getPhoneNumber().isBlank()) {
            student.setPhoneNumber(updStudent.getPhoneNumber());
        }

        if (updStudent.getProblemsId() != null) {
            for (long problemId : updStudent.getProblemsId()) {
                try {
                    problemService.getProblem(problemId);
                }
                catch (IllegalArgumentException e) {
                    throw new HttpStatusException(
                            HttpStatus.NOT_FOUND,
                            "Problem with Id = %s does not exists".formatted(problemId)
                    );
                }
                student.solveProblem(problemId);
            }
        }

        students.put(id, student);
    }

    public void delete(long id) {
        if (!students.containsKey(id)) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Student with id = %s not found".formatted(id));
        }
        students.remove(id);
    }

}
