package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.binding.CourseAddBindingModel;
import com.studentsystemapp.model.binding.CourseResourceAddBindingModel;
import com.studentsystemapp.model.binding.EnquiryAddBindingModel;
import com.studentsystemapp.model.binding.StudentRegisterBindingModel;
import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.entity.Course;
import com.studentsystemapp.model.enums.UserRolesEnum;
import com.studentsystemapp.model.view.EnquiryViewModel;
import com.studentsystemapp.service.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class EnquiryServiceImplTest {
    @Autowired
    private StudentServiceImpl studentService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private EnquiryService enquiryService;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;

    private CourseAddBindingModel courseAddBindingModel;
    private CourseResourceAddBindingModel courseResourceAddBindingModel;
    private EnquiryAddBindingModel enquiryAddBindingModel;
    private Course course;
    private StudentRegisterBindingModel teacher;
    private StudentRegisterBindingModel studentRegisterBindingModel;
    private BaseUser student;

    @BeforeEach
    @Transactional
    public void setUp() {



        teacher = new StudentRegisterBindingModel();
        enquiryAddBindingModel = new EnquiryAddBindingModel();

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

        enquiryAddBindingModel.setStudentId(userService.getByUsername("student").getId());
        enquiryAddBindingModel.setTeacherId(userService.getByUsername("teacher").getId());
        enquiryAddBindingModel.setQuestion("Question!");


    }

    @AfterEach
    @Transactional
    public void tearDown() {

        taskService.deleteAll();
        userService.deleteAll();
        courseService.deleteAll();
        studentService.deleteAll();
        enquiryService.deleteAll();
    }
    @Test
    void add() {

        assertTrue(enquiryService.getEnquiriesForUser("student").isEmpty());
        assertTrue(enquiryService.getEnquiriesForUser("teacher").isEmpty());
        enquiryService.add(enquiryAddBindingModel);
        assertEquals(1, enquiryService.getEnquiriesForUser("student").size());
        assertEquals(1, enquiryService.getEnquiriesForUser("teacher").size());
    }

    @Test
    void getEnquiriesForUser() {

        enquiryService.add(enquiryAddBindingModel);
        EnquiryViewModel enquiryViewModel = enquiryService.getEnquiriesForUser("student").stream().toList().get(0);
        assertEquals(1, enquiryService.getEnquiriesForUser("student").size());
        assertEquals("Question!",enquiryViewModel.getQuestion());
        assertEquals("teacher",enquiryViewModel.getParticipants().stream()
                .filter(u -> u.getRole()
                .equals(UserRolesEnum.TEACHER))
                .toList()
                .get(0)
                .getUsername());


    }

    @Test
    void addResponse() {

        enquiryService.add(enquiryAddBindingModel);
        EnquiryViewModel enquiryViewModel = enquiryService.getEnquiriesForUser("student")
                .stream().toList().get(0);

        assertNull(enquiryViewModel.getResponse());
        enquiryService.addResponse("Response.",enquiryViewModel.getId());
        enquiryViewModel = enquiryService.getEnquiriesForUser("student")
                .stream().toList().get(0);
        assertEquals("Response.", enquiryViewModel.getResponse());

    }

    @Test
    void getById() {

        enquiryService.add(enquiryAddBindingModel);
        EnquiryViewModel enquiryViewModel = enquiryService.getEnquiriesForUser("student")
                .stream().toList().get(0);

        assertEquals("Question!",enquiryService.getById(enquiryViewModel.getId()).getQuestion());

    }
}