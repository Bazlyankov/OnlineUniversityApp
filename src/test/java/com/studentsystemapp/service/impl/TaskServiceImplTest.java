package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.binding.CourseAddBindingModel;
import com.studentsystemapp.model.binding.CourseResourceAddBindingModel;
import com.studentsystemapp.model.binding.StudentRegisterBindingModel;
import com.studentsystemapp.model.binding.TaskAddBindingModel;
import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.entity.Course;
import com.studentsystemapp.model.entity.Student;
import com.studentsystemapp.model.enums.UserRolesEnum;
import com.studentsystemapp.repo.CourseRepository;
import com.studentsystemapp.repo.EnrollmentRepository;
import com.studentsystemapp.repo.TaskRepository;
import com.studentsystemapp.repo.UserRepository;
import com.studentsystemapp.service.CourseService;
import com.studentsystemapp.service.StudentService;
import com.studentsystemapp.service.TaskService;
import com.studentsystemapp.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TaskServiceImplTest {

    @Autowired
    private TaskService taskService;
    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;


    @Autowired
    private EnrollmentServiceImpl enrollmentService;
    private CourseAddBindingModel courseAddBindingModel;
    private CourseResourceAddBindingModel courseResourceAddBindingModel;
    private TaskAddBindingModel taskAddBindingModel;
    private StudentRegisterBindingModel teacher;
    private StudentRegisterBindingModel studentRegisterBindingModel;
    private StudentRegisterBindingModel student;

    @BeforeEach
    @Transactional
    public void setUp() {



        teacher = new StudentRegisterBindingModel();
        student = new StudentRegisterBindingModel();

        teacher.setFirstName("Daskal");
        teacher.setLastName("Daskalov");
        teacher.setPassword("password");
        teacher.setUsername("teacher");
        teacher.setEmail("teacher@mail.com");
        teacher.setConfirmPassword("password");



        studentRegisterBindingModel = new StudentRegisterBindingModel();

        student.setFirstName("Student");
        student.setLastName("Studentov");
        student.setPassword("password");
        student.setConfirmPassword("password");
        student.setUsername("student");
        student.setEmail("student@mail.com");




        courseAddBindingModel = new CourseAddBindingModel();
        courseAddBindingModel.setName("courseName");
        courseAddBindingModel.setDescription("courseDescription");
        courseAddBindingModel.setTeacherUsername("teacher");

        courseResourceAddBindingModel = new CourseResourceAddBindingModel();
        courseResourceAddBindingModel.setTitle("resource");
        courseResourceAddBindingModel.setDescription("description");
        courseResourceAddBindingModel.setVideoUrl("url");

        studentService.add(teacher);
        studentService.makeTeacher(studentService.getByUsername("teacher").getId());
        studentService.add(student);


        courseService.add(courseAddBindingModel);

        taskAddBindingModel = new TaskAddBindingModel();
        taskAddBindingModel.setDescription("task description");
        taskAddBindingModel.setCourseId(courseService.getCourseByName("courseName").get().getId());



        //userRepository.saveAndFlush(teacher);


    }

    @AfterEach
    @Transactional

    public void tearDown() throws InterruptedException {

        taskService.deleteAll();
        studentService.deleteAll();
        enrollmentService.deleteAll();
        userService.deleteAll();
        courseService.deleteAll();
    }

    @Test
    @Transactional
    @Rollback
    void addTask() {

        BaseUser baseUser = userService.getByUsername("student");
        assertEquals(0, baseUser.getTasks().size());
        taskService.addTask(baseUser.getId(),taskAddBindingModel);
        assertEquals(1, baseUser.getTasks().size());

    }

    @Test
    @Transactional
    @Rollback
    void getById() {
        BaseUser baseUser = userService.getByUsername("student");
        taskService.addTask(baseUser.getId(), taskAddBindingModel);
        assertFalse(taskService.getTasksByUsername("student").isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void setGradeById() throws InterruptedException {
        Thread.sleep(2000);
        BaseUser baseUser = userService.getByUsername("student");
        Long studentId = baseUser.getId();
        taskService.addTask(studentId,taskAddBindingModel);
        Long taskId = taskService.getTasksByUsername("student").stream().toList().get(0).getId();
        assertEquals(0, baseUser.getTasks().stream().filter(t -> t.getIsGraded()).collect(Collectors.toList()).size());
        taskService.setGradeById(taskId, BigDecimal.valueOf(6), studentId);
        assertEquals(1, baseUser.getTasks().stream().filter(t -> t.getIsGraded()).collect(Collectors.toList()).size());
    }

    @Test
    @Transactional
    @Rollback
    void getTasksByUsername() throws InterruptedException {
        BaseUser baseUser = userService.getByUsername("student");
        Long studentId = baseUser.getId();
        assertEquals(0, taskService.getTasksByUsername("student").size());
        taskService.addTask(studentId,taskAddBindingModel);
        assertEquals(1, taskService.getTasksByUsername("student").size());
    }
}