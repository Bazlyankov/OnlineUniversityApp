package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.binding.CourseAddBindingModel;
import com.studentsystemapp.model.binding.CourseResourceAddBindingModel;
import com.studentsystemapp.model.binding.StudentRegisterBindingModel;
import com.studentsystemapp.model.binding.TaskAddBindingModel;
import com.studentsystemapp.service.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TeacherServiceImplTest {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;
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







    }
    @Test
    void getAll() {

        assertEquals(0, teacherService.getAll().size());

        studentService.add(student);

        assertEquals(0, teacherService.getAll().size());

        studentService.add(teacher);

        assertEquals(0, teacherService.getAll().size());
        studentService.makeTeacher(studentService.getByUsername("teacher").getId());

        assertEquals(1, teacherService.getAll().size());



    }
}