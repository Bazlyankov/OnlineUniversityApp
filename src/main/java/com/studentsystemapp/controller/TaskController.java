package com.studentsystemapp.controller;

import com.cloudinary.Cloudinary;
import com.studentsystemapp.model.binding.TaskAddBindingModel;
import com.studentsystemapp.model.entity.Task;
import com.studentsystemapp.model.view.StudentViewModel;
import com.studentsystemapp.model.view.TaskViewModel;
import com.studentsystemapp.service.StudentService;
import com.studentsystemapp.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

@Controller
@RequestMapping("/students")
public class TaskController {

    private final StudentService studentService;
    private final ModelMapper modelMapper;
    private final TaskService taskService;


    public TaskController(StudentService studentService, ModelMapper modelMapper, TaskService taskService) {
        this.studentService = studentService;
        this.modelMapper = modelMapper;
        this.taskService = taskService;
    }

    @GetMapping("{id}/tasks")
    public String seeTasks(@PathVariable("id") Long id, Model model) {

        StudentViewModel student = studentService.getById(id);


        model.addAttribute("student", student);
        model.addAttribute("tasks", student.getTasks().stream().map(t -> modelMapper
                .map(t, TaskViewModel.class)));


        return "student-tasks";
    }

    @GetMapping("{id}/tasks/add")
    public String addTask(@PathVariable("id") Long id, Model model,TaskAddBindingModel taskAddBindingModel) {

        StudentViewModel student = studentService.getById(id);


        model.addAttribute("student", student);
        model.addAttribute("task", taskAddBindingModel);


        return "task-add";
    }

    @PostMapping("{id}/tasks/add")
    public String addTask(@PathVariable("id") Long id, @ModelAttribute("student") StudentViewModel studentViewModel,
                          @ModelAttribute("task") TaskAddBindingModel taskAddBindingModel) {


        taskAddBindingModel.setStudentId(id);
        taskService.addTask(id, taskAddBindingModel);
        return "redirect:/students/{id}/tasks";
    }

    @PostMapping("/{studentId}/tasks/{taskId}/add-grade")
    public String addGrade(@PathVariable("studentId") Long studentId, @PathVariable("taskId") Long taskId,
                           @RequestParam BigDecimal gradeValue) {

        taskService.setGradeById(taskId, gradeValue, studentId);

        return "redirect:/students/{studentId}/tasks";

    }

//    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
//    public void uploadTasksForCourse() {
//        // Specify the course ID for which you want to upload tasks
//        Long courseId = 1L; // Replace with the actual course ID
//
//        // Call the service to upload tasks for the course
//        courseService.uploadTasksForCourse(courseId);
//    }

    @PostMapping("/{studentId}/tasks/{taskId}/upload")
    public String handleFileUpload(@PathVariable("taskId") Long taskId,  @RequestParam("solutionFile") MultipartFile file, Model model) {
        try {

            taskService.uploadFile(file, taskId);

            model.addAttribute("message", "File uploaded successfully.");
        } catch (IOException e) {
            model.addAttribute("message", "File upload failed.");
        }

        return "redirect:/home";
    }



    @PostMapping("/{studentId}/tasks/{taskId}//upload")
    public String handleFileUpload(@PathVariable("taskId") Long taskId, @RequestParam("file") MultipartFile file) {
        try {


            // Create and save a TaskFile entity with Cloudinary information
            Task task = taskService.getById(taskId);

            if (task == null ) {
                //TODO: handle error
                return "redirect:/{studentId}/tasks";
            }

            taskService.uploadFile(file, taskId);



            // Redirect to the tasks page
            return "redirect:/tasks";
        } catch (IOException e) {
            // Handle file upload error
            return "redirect:/tasks?error";
        }
    }

}
