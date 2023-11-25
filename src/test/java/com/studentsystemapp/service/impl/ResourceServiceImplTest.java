package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.binding.CourseAddBindingModel;
import com.studentsystemapp.model.binding.CourseResourceAddBindingModel;
import com.studentsystemapp.model.binding.StudentRegisterBindingModel;
import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.entity.Course;
import com.studentsystemapp.model.enums.UserRolesEnum;
import com.studentsystemapp.repo.CourseRepository;
import com.studentsystemapp.repo.EnrollmentRepository;
import com.studentsystemapp.repo.TaskRepository;
import com.studentsystemapp.repo.UserRepository;
import com.studentsystemapp.service.CourseService;
import com.studentsystemapp.service.EnrollmentService;
import com.studentsystemapp.service.TaskService;
import com.studentsystemapp.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ResourceServiceImplTest {

    @Autowired
    private ResourceServiceImpl resourceService;
    @Autowired
    private StudentServiceImpl studentService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;

    private CourseAddBindingModel courseAddBindingModel;
    private CourseResourceAddBindingModel courseResourceAddBindingModel;
    private Course course;
    private StudentRegisterBindingModel teacher;
    private StudentRegisterBindingModel studentRegisterBindingModel;
    private BaseUser student;

    @BeforeEach
    @Transactional
    public void setUp() {



        teacher = new StudentRegisterBindingModel();

        teacher.setFirstName("Daskal");
        teacher.setLastName("Daskalov");
        teacher.setPassword("password");
        teacher.setUsername("teacher");
        teacher.setEmail("teacher@mail.com");
        teacher.setConfirmPassword("password");


        studentRegisterBindingModel = new StudentRegisterBindingModel();

        studentRegisterBindingModel.setFirstName("Student");
        studentRegisterBindingModel.setLastName("Studentov");
        studentRegisterBindingModel.setPassword("password");
        studentRegisterBindingModel.setConfirmPassword("password");
        studentRegisterBindingModel.setUsername("student");
        studentRegisterBindingModel.setEmail("student@mail.com");




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

        courseService.add(courseAddBindingModel);
        studentService.add(studentRegisterBindingModel);

        course = courseService.getAll().get(0);
        student = studentService.getAllStudents().get(0);


        //userRepository.saveAndFlush(teacher);


    }

    @AfterEach
    @Transactional
    public void tearDown() {

        taskService.deleteAll();
        enrollmentService.deleteAll();
        userService.deleteAll();
        courseService.deleteAll();
        studentService.deleteAll();
    }

    @Test
    @Transactional
    void add() {

        assertTrue(course.getCourseResources().isEmpty());
        resourceService.add(course.getId(), courseResourceAddBindingModel);
        assertEquals(1, course.getCourseResources().size());

    }

    @Test
    @Transactional
    void delete() {

        resourceService.add(course.getId(), courseResourceAddBindingModel);
        assertEquals(1, course.getCourseResources().size());
        resourceService.delete(course.getId(), course.getCourseResources().stream().limit(1)
                .collect(Collectors.toList()).get(0).getId());

        assertEquals(0, course.getCourseResources().size());
    }
}