package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.binding.CourseAddBindingModel;
import com.studentsystemapp.model.binding.CourseResourceAddBindingModel;
import com.studentsystemapp.model.entity.*;
import com.studentsystemapp.model.view.CourseViewModel;
import com.studentsystemapp.repo.CourseRepository;
import com.studentsystemapp.repo.ResourceRepository;
import com.studentsystemapp.repo.UserRepository;
import com.studentsystemapp.repo.TaskRepository;
import com.studentsystemapp.service.CourseService;
import jakarta.transaction.Transactional;
import org.hibernate.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.studentsystemapp.util.HibernateUtil.sessionFactory;
import static com.studentsystemapp.util.TaskDescriptions.taskDescriptions;

@Service
public class CourseServiceImpl implements CourseService {


    private final CourseRepository courseRepository;
    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    private static int taskCounter = 0;

    private final ModelMapper modelMapper;


    public CourseServiceImpl(CourseRepository courseRepository, ResourceRepository resourceRepository, UserRepository userRepository, TaskRepository taskRepository, ModelMapper modelMapper) {
        this.courseRepository = courseRepository;
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
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
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();


        Hibernate.initialize(courseRepository);
        CourseViewModel course = modelMapper.map(courseRepository.getCourseById(id).get(), CourseViewModel.class);

        transaction.commit();
        session.close();

        return course;
    }

    @Override
    @Transactional
    public boolean add(CourseAddBindingModel courseAddBindingModel) {

        Optional<Course> optionalCourse = getCourseByName(courseAddBindingModel.getName());

        if (optionalCourse.isPresent()) {
            return false;
        }

        Course course = modelMapper.map(courseAddBindingModel, Course.class);
        BaseUser teacher = userRepository.getByUsername(courseAddBindingModel.getTeacherUsername()).get();

        course.setTeacher(userRepository.getByUsername(courseAddBindingModel.getTeacherUsername()).get());

        courseRepository.save(course);
        teacher.getCourses().add(course);
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

        optionalCourse.get().getStudents().remove(optionalStudent.get());
        optionalStudent.get().getCourses().remove(optionalCourse.get());

        courseRepository.saveAndFlush(optionalCourse.get());
        userRepository.saveAndFlush(optionalStudent.get());

        return true;

    }



    @Override
    @Transactional
    public void addStudent(Long id, String username) {

        Optional<BaseUser> optionalStudent = userRepository.getByUsername(username);
        Optional<Course> optionalCourse = courseRepository.findById(id );

        if (optionalStudent.isEmpty() || optionalCourse.isEmpty()) {
            //TODO: handle error
        }


        BaseUser student = optionalStudent.get();
        Course course = optionalCourse.get();

        course.getStudents().add(student);
        student.getCourses().add(course);

        userRepository.saveAndFlush(student);
        courseRepository.saveAndFlush(course);

        System.out.println("added student");
    }

    @Override
    @Transactional
    public void addResourceToCourse(Long id, CourseResourceAddBindingModel resourceAddBindingModel) {

        Optional<Course> optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isEmpty()) {
            //TODO: handle error
        }

        Course course = optionalCourse.get();
        CourseResource resource = modelMapper.map(resourceAddBindingModel, CourseResource.class);
        course.getCourseResources().add(resource);
        courseRepository.saveAndFlush(course);



    }

    @Override
    public Optional<Course> findById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public Set<CourseViewModel> getCoursesByUsername(String username) {

        Set<CourseViewModel> courseViewModels = userRepository.findByUsername(username).get().getCourses()
                .stream().map(c -> modelMapper.map(c, CourseViewModel.class))
                .collect(Collectors.toSet());

        return courseViewModels ;


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

        Course course =optionalCourse.get();
        Set<BaseUser> studentsInCourse = course.getStudents();

        for (BaseUser student : studentsInCourse) {
            uploadTasksForStudent(student, courseId);
        }
    }

    @Transactional
    public void uploadTasksForStudent(BaseUser student, Long courseId) {
        if (taskCounter < 3) {

            Task task = new Task(taskDescriptions.get(taskCounter), courseRepository.findById(courseId).get());
            taskRepository.save(task);
            student.getTasks().add(task);
            System.out.println("Uploading tasks for students...");
            taskCounter++;
            userRepository.saveAndFlush(student);
        }
    }
}

