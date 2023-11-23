package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.binding.CourseAddBindingModel;
import com.studentsystemapp.model.binding.CourseResourceAddBindingModel;
import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.entity.Course;
import com.studentsystemapp.model.entity.Enrollment;
import com.studentsystemapp.model.enums.UserRolesEnum;
import com.studentsystemapp.model.view.CourseViewModel;
import com.studentsystemapp.repo.CourseRepository;
import com.studentsystemapp.repo.EnrollmentRepository;
import com.studentsystemapp.repo.TaskRepository;
import com.studentsystemapp.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseServiceImplTest {

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
    private BaseUser student;

    private static int count = 0;

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



        student = new BaseUser();

        student.setFirstName("Student");
        student.setLastName("Studentov");
        student.setPassword("password");
        student.setUsername("student");
        student.setEmail("student@mail.com");
        student.setEnrollments(new HashSet<>());
        student.setRole(UserRolesEnum.STUDENT);




        courseAddBindingModel = new CourseAddBindingModel();
        courseAddBindingModel.setName("courseName");
        courseAddBindingModel.setDescription("courseDescription");
        courseAddBindingModel.setTeacherUsername("teacher");

        courseResourceAddBindingModel = new CourseResourceAddBindingModel();
        courseResourceAddBindingModel.setTitle("resource");
        courseResourceAddBindingModel.setDescription("description");
        courseResourceAddBindingModel.setVideoUrl("url");




        userRepository.save(student);
        userRepository.save(teacher);


        userRepository.saveAndFlush(student);
        userRepository.saveAndFlush(teacher);


    }

    @AfterEach
    @Transactional
    public void tearDown() {


        taskRepository.deleteAll();

        enrollmentRepository.findAll().forEach(e -> e.setCourse(null));
        enrollmentRepository.findAll().forEach(e -> e.setUser(null));
        enrollmentRepository.saveAllAndFlush(enrollmentRepository.findAll());
        taskRepository.findAll().forEach(t -> t.setCourse(null));

        userRepository.findAll().forEach(e -> e.setEnrollments(null));
        userRepository.findAll().forEach(e -> e.setTasks(null));
        userRepository.findAll().forEach(e -> e.setEnquiries(null));
        userRepository.findAll().forEach(e -> e.setGrades(null));
        courseRepository.findAll().forEach(e -> e.setEnrollments(null));
        courseRepository.findAll().forEach(e -> e.setCourseResources(null));
        courseRepository.saveAllAndFlush(courseRepository.findAll());
        userRepository.saveAllAndFlush(userRepository.findAll());
        taskRepository.saveAllAndFlush(taskRepository.findAll());

        enrollmentRepository.deleteAll();
        userRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    @Transactional
    void addSuccessful() {

        assertTrue(courseRepository.findAll().isEmpty());
        assertTrue(courseService.add(courseAddBindingModel));
        assertEquals(1, courseRepository.findAll().size());

    }

    @Test
    @Transactional
    void getAll() {

        courseService.add(courseAddBindingModel);

        assertEquals(1, courseRepository.findAll().size());

        courseAddBindingModel.setName("course2");
        assertTrue(courseService.add(courseAddBindingModel));
        assertEquals(2, courseRepository.findAll().size());

    }

    @Test
    @Transactional
    void getCourseById() {


        courseService.add(courseAddBindingModel);
        Course course1 = courseService.getCourseByName("courseName").get();
        assertEquals(courseAddBindingModel.getName() ,course1.getName());
        assertEquals(courseAddBindingModel.getTeacherUsername() ,course1.getTeacher().getUsername());
    }



    @Test

    @Transactional
    void addDuplicate() {

        courseAddBindingModel.setTeacherUsername("teacher2Name");
        assertFalse(courseService.add(courseAddBindingModel));
    }

    @Test
    @Transactional
    void remove() {
        courseService.add(courseAddBindingModel);

        assertEquals(courseAddBindingModel.getName(), courseService.getCourseByName("courseName").get().getName());

        course = courseService.getCourseByName("courseName").get();
        courseService.addStudent("courseName","student");
        assertEquals(1, userRepository.getByUsername("student").get().getEnrollments().size());
        assertTrue(courseService.remove(course.getId(), student.getId()));
        assertEquals(0, courseService.getEnrollmentsByUsername("student").size());

    }

    @Test
    @Transactional
    void addStudent() {


        courseService.add(courseAddBindingModel);
        assertEquals(0, courseService.getEnrollmentsByUsername("student").size());

        courseService.addStudent(courseAddBindingModel.getName(), "student");
        assertEquals(1, courseService.getEnrollmentsByUsername("student").size());

    }

    @Test
    @Transactional
    void addResourceToCourse() {



        courseService.add(courseAddBindingModel);
        Course course1 = courseRepository.getCourseByName("courseName").get();
        assertTrue(course1.getCourseResources().isEmpty());
        courseService.addResourceToCourse("courseName",courseResourceAddBindingModel);
        course1 = courseRepository.getCourseByName("courseName").get();
        assertEquals(1, course1.getCourseResources().size());

    }

    @Test
    @Transactional
    void findById() {


        courseService.add(courseAddBindingModel);

        Course course1 = courseRepository.getCourseByName("courseName").get();
        assertNotNull(course1);


    }

    @Test
    @Transactional
    void getCoursesByUsername() {



        courseService.add(courseAddBindingModel);
        assertEquals(0, student.getEnrollments().size());
        courseService.addStudent("courseName", "student");
        BaseUser baseUser = userRepository.getByUsername("student").get();
        assertEquals(1, baseUser.getEnrollments().size());

    }

    @Test
    void getCourseByName() {

        assertTrue(courseService.getCourseByName("courseName").isEmpty());
        courseService.add(courseAddBindingModel);
        assertTrue(courseService.getCourseByName("courseName").isPresent());


    }

    @Test
    @Transactional
    void uploadTasksForCourse() {


        courseService.add(courseAddBindingModel);
        Course course1 = courseService.getCourseByName("courseName").get();
        assertTrue(course1.getCourseResources().isEmpty());
        courseService.addResourceToCourse("courseName",new CourseResourceAddBindingModel());
        assertEquals(1, course1.getCourseResources().size());

    }

}