package com.studentsystemapp.controller;

import com.studentsystemapp.model.binding.StudentRegisterBindingModel;
import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.view.StudentViewModel;
import com.studentsystemapp.service.CourseService;
import com.studentsystemapp.service.StudentService;
import com.studentsystemapp.service.TeacherService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.studentsystemapp.model.entity.Course;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {


    private final StudentService studentService;
    private final CourseService courseService;
    private final TeacherService teacherService;

    public StudentController(StudentService studentService, CourseService courseService, TeacherService teacherService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.teacherService = teacherService;
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

        List<BaseUser> students = studentService.getAll();
        List<BaseUser> teachers = teacherService.getAll();
        model.addAttribute("students", students);
        model.addAttribute("teachers", teachers);

        return "all-students";
    }

    @GetMapping("{id}/courses")
    public String seeCourses(@PathVariable("id") Long id, Model model) {

        StudentViewModel student = studentService.getById(id);

        List<Course> courses = student.getCourses();

        if (student == null) return "all-students";

        model.addAttribute("student", student);
        model.addAttribute("courses", courses);


        return "student-courses";
    }

    @GetMapping("/{id}/make-teacher")
    public String makeTeacher(@PathVariable("id") Long id, Model model) {

        studentService.makeTeacher(id);


        return "redirect:/students";
    }




    @GetMapping("/{id}")
    public String seeStudent(@PathVariable("id") Long id, Model model) {

        StudentViewModel student = studentService.getById(id);


        model.addAttribute("student", student);


        return "student-view";
    }

}
