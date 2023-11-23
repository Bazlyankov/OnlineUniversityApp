package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.binding.CourseAddBindingModel;
import com.studentsystemapp.model.binding.CourseResourceAddBindingModel;
import com.studentsystemapp.model.binding.StudentRegisterBindingModel;
import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.entity.Course;
import com.studentsystemapp.model.entity.Grade;
import com.studentsystemapp.model.entity.Student;
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
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentServiceImplTest {


    @Autowired
    private StudentServiceImpl studentService;
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
    private Course course;
    private BaseUser teacher;
    private StudentRegisterBindingModel studentRegisterBindingModel;
    private BaseUser student;

    @BeforeEach
    @Transactional
    public void setUp() {



        teacher = new BaseUser();

        teacher.setFirstName("Daskal");
        teacher.setLastName("Daskalov");
        teacher.setPassword("password");
        teacher.setUsername("teacher");
        teacher.setEmail("teacher@mail.com");
        teacher.setEnrollments(new HashSet<>());
        teacher.setRole(UserRolesEnum.TEACHER);


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

        userRepository.save(teacher);

        courseService.add(courseAddBindingModel);


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
    void add() {

        assertEquals(0, studentService.getAllStudents().size());
        studentService.add(studentRegisterBindingModel);
        student = studentService.getAllStudents().get(0);
        assertNotNull(student);


    }

    @Test
    @Transactional
    void remove() {

        studentService.add(studentRegisterBindingModel);
        studentService.remove("student");
        assertEquals(1, userRepository.findAll().size());
    }



    @Test
    void getAllStudents() {

        assertEquals(0, studentService.getAllStudents().size());
        studentService.add(studentRegisterBindingModel);

        assertEquals(1, studentService.getAllStudents().size());
        studentRegisterBindingModel.setUsername("student2");
        studentRegisterBindingModel.setEmail("student2@mail.com");
        studentService.add(studentRegisterBindingModel);

        assertEquals(2, studentService.getAllStudents().size());


    }

    @Test
    @Transactional
    void getAllByCourse() {

        assertEquals(0, studentService.getAllStudents().size());
        studentService.add(studentRegisterBindingModel);

        assertEquals(1, studentService.getAllStudents().size());
        studentRegisterBindingModel.setUsername("student2");
        studentRegisterBindingModel.setEmail("student2@mail.com");
        studentService.add(studentRegisterBindingModel);
        course = courseService.getCourseByName("courseName").get();

        assertEquals(1, studentService.getAllByCourse(course).size());
        courseService.addStudent("courseName","student");

        assertEquals(2, studentService.getAllByCourse(course).size());
        courseService.addStudent("courseName","student2");

        assertEquals(3, studentService.getAllByCourse(course).size());

    }

//    @Test
//    @Transactional
//    void addGrade() {
//
//        studentService.add(studentRegisterBindingModel);
//
//        student = studentService.getAllStudents().get(0);
//        Long id = student.getId();
//        assertNull(student.getGrades());
//        studentService.addGrade(id,new Grade(BigDecimal.valueOf(5), student));
//        assertEquals(1, student.getGrades().size());
//
//
//    }

    @Test
    void getById() {


        studentService.add(studentRegisterBindingModel);
        Long id = studentService.getAllStudents().get(0).getId();
        assertEquals("student",studentService.getById(id).getUsername());

    }

    @Test
    @Transactional
    void makeTeacher() {

        studentService.add(studentRegisterBindingModel);

        student = studentService.getAllStudents().get(0);
        assertTrue(student.getRole().equals(UserRolesEnum.STUDENT));
        studentService.makeTeacher(student.getId());
        assertTrue(student.getRole().equals(UserRolesEnum.TEACHER));


    }
}