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
    public String addCourse(@ModelAttribute("course") @Valid CourseAddBindingModel courseAddBindingModel,
                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            //TODO: handle errors
        }

        courseAddBindingModel.setTeacherUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        courseService.add(courseAddBindingModel);

        return "redirect:/home";

    }

    @GetMapping("/{courseId}/add")
    public String addStudentToCourse(@PathVariable("courseId") Long id) {


        return "add-student-to-course";

    }

    @PostMapping("/{courseId}/add")
    public String addStudentToCoursePost(@PathVariable("courseId") Long id, @RequestParam String studentInput) {

        courseService.addStudent(id, studentInput);

        return "redirect:/courses/{courseId}";

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

        CourseViewModel course = courseService.getCourseById(id);

        if (course == null) {
            //TODO: handle error
            return "redirect:/";

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
    public String addResource(@PathVariable("id") Long id, @ModelAttribute("resource") @Valid CourseResourceAddBindingModel resourceAddBindingModel,
                              BindingResult bindingResult) {


        CourseViewModel course = courseService.getCourseById(id);

        if (course == null) {
            //TODO: handle error
            return "redirect:/";

        }

        resourceService.add(id, resourceAddBindingModel);

        return "redirect:/resources/{id}";
    }

}
