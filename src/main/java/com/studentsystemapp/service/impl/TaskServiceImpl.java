package com.studentsystemapp.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.studentsystemapp.model.binding.TaskAddBindingModel;
import com.studentsystemapp.model.view.TaskViewModel;
import com.studentsystemapp.model.entity.*;
import com.studentsystemapp.repo.CourseRepository;
import com.studentsystemapp.repo.GradeRepository;
import com.studentsystemapp.repo.UserRepository;
import com.studentsystemapp.repo.TaskRepository;
import com.studentsystemapp.service.TaskService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final GradeRepository gradeRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;



    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, GradeRepository gradeRepository, CourseRepository courseRepository, ModelMapper modelMapper, Cloudinary cloudinary) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.gradeRepository = gradeRepository;
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
        this.cloudinary = cloudinary;
    }


    @Override
    @Transactional
    public void addTask(Long id, TaskAddBindingModel taskAddBindingModel) {

        Task task = modelMapper.map(taskAddBindingModel, Task.class);
        task.setCourse(courseRepository.findById(taskAddBindingModel.getCourseId()).get());
        //TODO: handle  missing student
        BaseUser student = userRepository.findById(id).get();

        student.getTasks().add(task);
        BaseUser teacher = task.getCourse().getTeacher();
        teacher.getTasks().add(task);

        taskRepository.save(task);
        userRepository.saveAndFlush(student);
        userRepository.saveAndFlush(teacher);

    }

    @Override
    public Task getById(Long id) {

        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isEmpty()) {
            //TODO: handle error
            return null;
        }

        return optionalTask.get();
    }

    @Override
    @Transactional
    public String uploadFile(MultipartFile file, Long taskId) throws IOException {


        Map<?, ?> cloudinaryResponse = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "public_id", "task" + taskId + "solution.nb"
                ,"resource_type", "auto"));


        Task task = taskRepository.findById(taskId).get();
        task.setIsCompleted(true);

        task.setTaskSolution((String) cloudinaryResponse.get("url"));
        taskRepository.saveAndFlush(task);

        return "";
    }

    @Override
    @Transactional
    public void setGradeById(Long taskId, BigDecimal gradeValue, Long studentId) {

        Optional<Task> optionalTask = taskRepository.findById(taskId);
        Optional<BaseUser> optionalStudent = userRepository.findById(studentId);

        if (optionalTask.isEmpty() || optionalStudent.isEmpty()) {
            //TODO: handle error
            return;
        }

        Task task = optionalTask.get();
        BaseUser student =  optionalStudent.get();
        Grade grade = new Grade(gradeValue, student);
        gradeRepository.save(grade);
        task.setGrade(grade);
        task.setIsGraded(true);
        task.setIsCompleted(true);
        taskRepository.saveAndFlush(task);
        userRepository.saveAndFlush(student);

        System.out.println("");

    }

    @Override
    public Set<TaskViewModel> getTasksByUsername(String username) {



        return userRepository.findByUsername(username).get().getTasks()
                .stream().map(t -> modelMapper.map(t, TaskViewModel.class))
                .collect(Collectors.toSet());
    }
}
