package com.studentsystemapp.controller;




import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.view.EnquiryViewModel;
import com.studentsystemapp.model.view.EnrollmentViewModel;
import com.studentsystemapp.model.view.TaskViewModel;
import com.studentsystemapp.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

@Controller
public class HomeController {

    private final CourseService courseService;
    private final TaskService taskService;
    private final UserService userService;
    private final EnquiryService enquiryService;


    public HomeController(CourseService courseService, TaskService taskService, UserService userService, EnquiryService enquiryService) {
        this.courseService = courseService;
        this.taskService = taskService;
        this.userService = userService;
        this.enquiryService = enquiryService;
    }


    @GetMapping("/")
    public ModelAndView index() {


        return new ModelAndView("index");
    }



    @GetMapping("/home") public String home(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<TaskViewModel> tasks = taskService.getTasksByUsername( authentication.getName());
        Set<EnrollmentViewModel> enrollments = courseService.getEnrollmentsByUsername( authentication.getName());
        Set<EnquiryViewModel> enquiries = enquiryService.getEnquiriesForUser( authentication.getName());
        BaseUser user = userService.getByUsername(authentication.getName());

        model.addAttribute("tasks", tasks);
        model.addAttribute("user", user);
        model.addAttribute("enquiries", enquiries);
        model.addAttribute("enrollments", enrollments);

        return "home";
    }











}
