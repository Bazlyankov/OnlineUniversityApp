package com.studentsystemapp.controller;

import com.studentsystemapp.model.view.CourseViewModel;
import com.studentsystemapp.model.view.EnrollmentViewModel;
import com.studentsystemapp.model.view.UserViewModel;
import com.studentsystemapp.service.StudentService;
import com.studentsystemapp.service.TeacherService;
import com.studentsystemapp.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("")
    public String allStudents(Model model) {

        List<UserViewModel> students = studentService.getAllStudents();
        List<UserViewModel> teachers = teacherService.getAll();
        model.addAttribute("students", students);
        model.addAttribute("teachers", teachers);

        return "all-users";
    }

    @GetMapping("{id}/courses")
    public String seeCourses(@PathVariable("id") Long id, Model model) {

        UserViewModel student = userService.getById(id);

        List<CourseViewModel> courses = student.getEnrollments().stream()
                .map(EnrollmentViewModel::getCourse).collect(Collectors.toList());

        model.addAttribute("student", student);
        model.addAttribute("courses", courses);


        return "user-courses";
    }

    @GetMapping("/{id}/make-teacher")
    public String makeTeacher(@PathVariable("id") Long id) {

        studentService.makeTeacher(id);
        return "redirect:/students";
    }

    @PostMapping("/{id}/upload-picture")
    @PreAuthorize("@securityService.hasUserId(authentication, #id)")
    public String handlePictureUpload(@PathVariable("id") Long id, @RequestParam("solutionFile") MultipartFile file, Model model) {
        try {
            userService.uploadPicture(file, id);
            model.addAttribute("message", "File uploaded successfully.");
        } catch (IOException e) {
            model.addAttribute("message", "File upload failed.");
        }

        return "redirect:/students/" + id;
    }



    @GetMapping("/{id}")
    public String seeStudent(@PathVariable("id") Long id, Model model) {

        UserViewModel student = userService.getById(id);


        model.addAttribute("student", student);
        model.addAttribute("viewerId", userService.getByUsername(SecurityContextHolder.getContext()
                .getAuthentication().getName()).getId());
        model.addAttribute("viewerUsername", SecurityContextHolder.getContext()
                .getAuthentication().getName());

        return "user-view";
    }

}
