package com.studentsystemapp.controller;

import com.studentsystemapp.model.binding.StudentRegisterBindingModel;
import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.view.CourseViewModel;
import com.studentsystemapp.model.view.EnrollmentViewModel;
import com.studentsystemapp.model.view.StudentViewModel;
import com.studentsystemapp.service.StudentService;
import com.studentsystemapp.service.TeacherService;
import com.studentsystemapp.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/students")
public class StudentController {


    private final StudentService studentService;
    private final TeacherService teacherService;
    private final UserService userService;

    public StudentController(StudentService studentService, TeacherService teacherService, UserService userService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.userService = userService;
    }

    @GetMapping("/add")
    public String addStudent(Model model) {

        StudentRegisterBindingModel studentRegisterBindingModel = new StudentRegisterBindingModel();
        model.addAttribute("student", studentRegisterBindingModel);

        return "new-student";

    }

    @PostMapping("/add")
    public String addStudent(@ModelAttribute("student") StudentRegisterBindingModel studentRegisterBindingModel) {

        studentService.add(studentRegisterBindingModel);

        return "redirect:/students";

    }



    @GetMapping("")
    public String allStudents(Model model) {

        List<BaseUser> students = studentService.getAllStudents();
        List<BaseUser> teachers = teacherService.getAll();
        model.addAttribute("students", students);
        model.addAttribute("teachers", teachers);

        return "all-students";
    }

    @GetMapping("{id}/courses")
    public String seeCourses(@PathVariable("id") Long id, Model model) {

        StudentViewModel student = studentService.getById(id);

        List<CourseViewModel> courses = student.getEnrollments().stream()
                .map(EnrollmentViewModel::getCourse).collect(Collectors.toList());


        model.addAttribute("student", student);
        model.addAttribute("courses", courses);


        return "student-courses";
    }

    @GetMapping("/{id}/make-teacher")
    public String makeTeacher(@PathVariable("id") Long id) {

        studentService.makeTeacher(id);


        return "redirect:/students";
    }

    @PostMapping("/{studentId}/upload-picture")
    public String handlePictureUpload(@PathVariable("studentId") Long studentId, @RequestParam("solutionFile") MultipartFile file, Model model) {
        try {

            studentService.uploadPicture(file, studentId);

            model.addAttribute("message", "File uploaded successfully.");
        } catch (IOException e) {
            model.addAttribute("message", "File upload failed.");
        }

        return "redirect:/home";
    }



    @GetMapping("/{id}")
    public String seeStudent(@PathVariable("id") Long id, Model model) {

        StudentViewModel student = studentService.getById(id);


        model.addAttribute("student", student);
        model.addAttribute("viewerId", userService.getByUsername(SecurityContextHolder.getContext()
                .getAuthentication().getName()).getId());


        return "student-view";
    }

}
