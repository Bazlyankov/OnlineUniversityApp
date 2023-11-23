package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.binding.CourseAddBindingModel;
import com.studentsystemapp.model.binding.CourseResourceAddBindingModel;
import com.studentsystemapp.model.entity.*;
import com.studentsystemapp.model.view.CourseViewModel;
import com.studentsystemapp.model.view.EnrollmentViewModel;
import com.studentsystemapp.repo.*;
import com.studentsystemapp.service.CourseService;
import jakarta.transaction.Transactional;
import org.hibernate.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.studentsystemapp.util.TaskDescriptions.taskDescriptions;

@Service
public class CourseServiceImpl implements CourseService {


    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ResourceRepository resourceRepository;
    private final ModelMapper modelMapper;
    private static int taskCounter = 0;


    public CourseServiceImpl(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository, UserRepository userRepository, TaskRepository taskRepository, ResourceRepository resourceRepository, ModelMapper modelMapper) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.resourceRepository = resourceRepository;
        this.modelMapper = modelMapper;
    }



    @Override
    @Transactional
    public List<Course> getAll() {
        Hibernate.initialize(courseRepository.findAll());
        return courseRepository.findAll();
    }

    @Override
    @Transactional
    public CourseViewModel getCourseById(Long id) {
        Optional<Course> optionalCourse = courseRepository.getCourseById(id);
        if(optionalCourse.isEmpty()) {
            throw new NoSuchElementException();
        }


        Hibernate.initialize(courseRepository);
        CourseViewModel course = modelMapper.map(optionalCourse.get(), CourseViewModel.class);


        return course;
    }

    @Override
    @Transactional
    public boolean add(CourseAddBindingModel courseAddBindingModel) {

        Optional<Course> optionalCourse = getCourseByName(courseAddBindingModel.getName());
        String teacherUsername = courseAddBindingModel.getTeacherUsername();
        Optional<BaseUser> optionalTeacher = userRepository.getByUsername(teacherUsername);

        if (optionalCourse.isPresent() || optionalTeacher.isEmpty()) {
            return false;
        }

        Course course = modelMapper.map(courseAddBindingModel, Course.class);
        BaseUser teacher = optionalTeacher.get();

        courseRepository.save(course);

        Enrollment enrollment = new Enrollment(course, teacher);
        enrollmentRepository.save(enrollment);

        if (course.getEnrollments() == null) {
            course.setEnrollments(new HashSet<>());
        }

        course.getEnrollments().add(enrollment);
        courseRepository.saveAndFlush(course);

        if (teacher.getEnrollments() == null) {
            teacher.setEnrollments(new HashSet<>());
        }

        teacher.getEnrollments().add(enrollment);
        userRepository.saveAndFlush(teacher);

        return true;
    }

    @Override
    @Transactional
    public boolean remove(Long courseId, Long studentId) {
        Optional<BaseUser> optionalStudent = userRepository.findById(studentId);
        Optional<Course> optionalCourse = courseRepository.findById(courseId);


        if (optionalStudent.isEmpty() || optionalCourse.isEmpty()) {
            return false;
        }



        BaseUser student =  optionalStudent.get();
        Course course = optionalCourse.get();


        Enrollment enrollment = new Enrollment(course, student);

        optionalCourse.get().getEnrollments().remove(enrollment);
        student.getEnrollments().remove(enrollment);
        enrollmentRepository.delete(enrollment);

        courseRepository.saveAndFlush(optionalCourse.get());
        userRepository.saveAndFlush(optionalStudent.get());

        return true;

    }



    @Override
    @Transactional
    public void addStudent(Long id, String username) {

        Optional<BaseUser> optionalStudent = userRepository.getByUsername(username);
        Optional<Course> optionalCourse = courseRepository.findById(id );

        addStudentToCourse(optionalStudent, optionalCourse);
    }

    @Override
    @Transactional
    public void addStudent(String courseName, String username) {

        Optional<BaseUser> optionalStudent = userRepository.getByUsername(username);
        Optional<Course> optionalCourse = courseRepository.getCourseByName(courseName );

        addStudentToCourse(optionalStudent, optionalCourse);
    }

    private void addStudentToCourse(Optional<BaseUser> optionalStudent, Optional<Course> optionalCourse) {
        if (optionalStudent.isEmpty() || optionalCourse.isEmpty()) {
            throw new NoSuchElementException();
        }


        BaseUser student = optionalStudent.get();
        Course course = optionalCourse.get();
        Enrollment enrollment = new Enrollment(course, student);
        enrollmentRepository.save(enrollment);

        course.getEnrollments().add(enrollment);
        student.getEnrollments().add(enrollment);

        userRepository.saveAndFlush(student);
        courseRepository.saveAndFlush(course);

        System.out.println("added student");
    }

    @Override
    @Transactional
    public void addResourceToCourse(Long id, CourseResourceAddBindingModel resourceAddBindingModel) {

        Optional<Course> optionalCourse = courseRepository.findById(id);

        addResourceHelper(resourceAddBindingModel, optionalCourse);


    }

    @Override
    @Transactional
    public void addResourceToCourse(String courseName, CourseResourceAddBindingModel resourceAddBindingModel) {

        Optional<Course> optionalCourse = courseRepository.getCourseByName(courseName);

        addResourceHelper(resourceAddBindingModel, optionalCourse);


    }

    private void addResourceHelper(CourseResourceAddBindingModel resourceAddBindingModel, Optional<Course> optionalCourse) {
        if (optionalCourse.isEmpty()) {
            throw new NoSuchElementException();
        }

        Course course = optionalCourse.get();
        CourseResource resource = modelMapper.map(resourceAddBindingModel, CourseResource.class);
        if (course.getCourseResources() == null) course.setCourseResources(new HashSet<>());
        course.getCourseResources().add(resource);

        resourceRepository.save(resource);
        courseRepository.saveAndFlush(course);
    }

    @Override
    public Optional<Course> findById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public Set<EnrollmentViewModel> getEnrollmentsByUsername(String username) {
        Optional<BaseUser> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException();
        }

        return optionalUser.get().getEnrollments().stream().map(
                e -> modelMapper.map(e, EnrollmentViewModel.class)
        ).collect(Collectors.toSet());
    }



    @Override
    public Optional<Course> getCourseByName(String name) {
        return courseRepository.getCourseByName(name);
    }

    @Override
    public void uploadTasksForCourse(Long courseId) {


        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isEmpty()) {
            return;
        }

        Course course = optionalCourse.get();
        Set<BaseUser> studentsInCourse = course.getEnrollments().stream()
                .map(e -> e.getUser())
                .collect(Collectors.toSet());

        for (BaseUser student : studentsInCourse) {
            uploadTasksForStudent(student, courseId);
        }
    }

    @Transactional
    public void uploadTasksForStudent(BaseUser student, Long courseId) {
        if (taskCounter < 3) {

            Optional<Course> optionalCourse = courseRepository.findById(courseId);

            if (optionalCourse.isEmpty()) {
                throw new NoSuchElementException();
            }

            Task task = new Task(taskDescriptions.get(taskCounter), optionalCourse.get());
            taskRepository.save(task);
            student.getTasks().add(task);
            System.out.println("Uploading tasks for students...");
            taskCounter++;
            userRepository.saveAndFlush(student);
        }
    }
}

