package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.binding.CourseAddBindingModel;
import com.studentsystemapp.model.binding.CourseResourceAddBindingModel;
import com.studentsystemapp.model.binding.StudentRegisterBindingModel;
import com.studentsystemapp.model.binding.TaskAddBindingModel;
import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.entity.Course;
import com.studentsystemapp.model.enums.UserRolesEnum;
import com.studentsystemapp.repo.CourseRepository;
import com.studentsystemapp.repo.EnrollmentRepository;
import com.studentsystemapp.repo.TaskRepository;
import com.studentsystemapp.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TaskServiceImplTest {

    @Autowired
    private TaskServiceImpl taskService;
    @Autowired
    private CourseServiceImpl courseService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    private CourseAddBindingModel courseAddBindingModel;
    private CourseResourceAddBindingModel courseResourceAddBindingModel;
    private TaskAddBindingModel taskAddBindingModel;
    private Course course;
    private BaseUser teacher;
    private StudentRegisterBindingModel studentRegisterBindingModel;
    private BaseUser student;

    @BeforeEach
    @Transactional
    public void setUp() {



        teacher = new BaseUser();
        student = new BaseUser();

        teacher.setFirstName("Daskal");
        teacher.setLastName("Daskalov");
        teacher.setPassword("password");
        teacher.setUsername("teacher");
        teacher.setEmail("teacher@mail.com");
        teacher.setEnrollments(new HashSet<>());
        teacher.setRole(UserRolesEnum.TEACHER);


        studentRegisterBindingModel = new StudentRegisterBindingModel();

        student.setFirstName("Student");
        student.setLastName("Studentov");
        student.setPassword("password");
        student.setUsername("student");
        student.setEmail("student@mail.com");
        student.setRole(UserRolesEnum.STUDENT);




        courseAddBindingModel = new CourseAddBindingModel();
        courseAddBindingModel.setName("courseName");
        courseAddBindingModel.setDescription("courseDescription");
        courseAddBindingModel.setTeacherUsername("teacher");

        courseResourceAddBindingModel = new CourseResourceAddBindingModel();
        courseResourceAddBindingModel.setTitle("resource");
        courseResourceAddBindingModel.setDescription("description");
        courseResourceAddBindingModel.setVideoUrl("url");

        userRepository.save(teacher);
        userRepository.save(student);


        courseService.add(courseAddBindingModel);

        taskAddBindingModel = new TaskAddBindingModel();
        taskAddBindingModel.setDescription("task description");
        taskAddBindingModel.setCourseId(courseRepository.findAll().get(0).getId());



        //userRepository.saveAndFlush(teacher);


    }

    @AfterEach
    @Transactional
    public void tearDown() {

        taskRepository.deleteAll();

        enrollmentRepository.deleteAll();
        userRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    @Transactional
    void addTask() {

        Long studentId = student.getId();
        assertEquals(0, student.getTasks().size());
        taskService.addTask(studentId,taskAddBindingModel);
        assertEquals(1, student.getTasks().size());

    }

    @Test
    @Transactional
    void getById() {

        Long studentId = student.getId();
        taskService.addTask(studentId,taskAddBindingModel);
        Long taskId = taskRepository.findAll().get(0).getId();
        assertNotNull(taskService.getById(taskId));

    }

    @Test
    void uploadFile() {
    }

    @Test
    @Transactional
    void setGradeById() {

        Long studentId = student.getId();
        taskService.addTask(studentId,taskAddBindingModel);
        Long taskId = taskRepository.findAll().get(0).getId();
        assertEquals(0, student.getTasks().stream().filter(t -> t.getIsGraded()).collect(Collectors.toList()).size());
        taskService.setGradeById(taskId, BigDecimal.valueOf(6), studentId);
        assertEquals(1, student.getTasks().stream().filter(t -> t.getIsGraded()).collect(Collectors.toList()).size());
    }

    @Test
    @Transactional
    void getTasksByUsername() {

        Long studentId = student.getId();
        assertEquals(0, taskService.getTasksByUsername("student").size());
        taskService.addTask(studentId,taskAddBindingModel);
        assertEquals(1, taskService.getTasksByUsername("student").size());

    }
}