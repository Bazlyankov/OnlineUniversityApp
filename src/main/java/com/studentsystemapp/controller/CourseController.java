package com.studentsystemapp.controller;

import com.studentsystemapp.model.binding.CourseAddBindingModel;
import com.studentsystemapp.model.binding.CourseResourceAddBindingModel;
import com.studentsystemapp.model.enums.UserRolesEnum;
import com.studentsystemapp.model.view.CourseViewModel;
import com.studentsystemapp.model.view.EnrollmentViewModel;
import com.studentsystemapp.service.CourseService;
import com.studentsystemapp.service.ResourceService;
import com.studentsystemapp.service.StudentService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final ResourceService resourceService;

    private final StudentService studentService;

    public CourseController( CourseService courseService, ResourceService resourceService, StudentService studentService) {

        this.courseService = courseService;
        this.resourceService = resourceService;
        this.studentService = studentService;
    }

    @Transactional
    @GetMapping("") public String courses(Model model) {

        model.addAttribute("allStudents", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAll());

        return "courses";
    }

    @GetMapping("/add")
    public String addCourse(Model model) {

        CourseAddBindingModel courseAddBindingModel = new CourseAddBindingModel();
        model.addAttribute("course", courseAddBindingModel);

        return "course-add";

    }

    @PostMapping("/add")
    public ModelAndView addCourse(@ModelAttribute("course") @Valid CourseAddBindingModel courseAddBindingModel,
                                  BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView("redirect:/courses");
        modelAndView.addObject("hasErrors", bindingResult.hasErrors());
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("course-add");
            return modelAndView;
        }

        courseAddBindingModel.setTeacherUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        courseService.add(courseAddBindingModel);

        return modelAndView;

    }

    @GetMapping("/{courseId}/add")
    public String addStudentToCourse(@PathVariable("courseId") Long id) {

        return "add-student-to-course";

    }

    @PostMapping("/{courseId}/add")
    public ModelAndView addStudentToCoursePost(Model model, @PathVariable("courseId") Long id, @RequestParam String studentInput) {

        ModelAndView modelAndView = new ModelAndView("redirect:/courses/{courseId}");
        try {
            courseService.addStudent(id, studentInput);
        } catch (NoSuchElementException e) {
            model.addAttribute("errorMessage", "No student with this username exists.");
            modelAndView.setViewName("add-student-to-course");
        }

        return modelAndView;

    }

    @PostMapping("/{courseId}/delete/{studentId}")
    public String deleteStudentFromCourse(@PathVariable("courseId") Long courseId,
                                          @PathVariable("studentId") Long studentId) {

        courseService.remove(courseId, studentId);
        return "redirect:/courses/{courseId}";

    }

    @Transactional
    @GetMapping("/{id}")
    public String seeUsers(@PathVariable("id") Long id, Model model) {
        CourseViewModel course;
        try {

            course = courseService.getCourseById(id);
        } catch (NoSuchElementException e) {
            return "redirect:/error";
        }


        model.addAttribute("course", course);
        model.addAttribute("students", course.getEnrollments().stream()
                .map(EnrollmentViewModel::getUser)
                .filter(student -> student.getRole().equals(UserRolesEnum.STUDENT)).toList());
        model.addAttribute("teachers", course.getEnrollments().stream()
                .map(EnrollmentViewModel::getUser)
                .filter(teachers -> teachers.getRole().equals(UserRolesEnum.TEACHER)).toList());


        return "course-students";
    }

    @GetMapping("/{id}/resources/add")
    public String addResource(@PathVariable("id") Long id, Model model ) {

        CourseResourceAddBindingModel resourceAddBindingModel = new CourseResourceAddBindingModel();
        model.addAttribute("resource", resourceAddBindingModel);

        return "resource-add";
    }

    @PostMapping("/{id}/resources/add")
    public ModelAndView addResource(@PathVariable("id") Long id, @ModelAttribute("resource") @Valid CourseResourceAddBindingModel resourceAddBindingModel,
                              BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("redirect:/resources/{id}");
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("resource-add");
            modelAndView.addObject("hasErrors", true);
            return modelAndView;
        }

        try {
            resourceService.add(id, resourceAddBindingModel);
        } catch (NoSuchElementException e) {
            modelAndView.addObject("invalidId", "Please enter a valid course id");
            modelAndView.setViewName("resource-add");

        }

        return modelAndView;
    }

}
