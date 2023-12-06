package com.studentsystemapp.controller;

import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.entity.Course;
import com.studentsystemapp.model.view.CourseViewModel;
import com.studentsystemapp.model.view.UserViewModel;
import com.studentsystemapp.service.StudentService;
import com.studentsystemapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class StudentRestController {

    private final UserService userService;

    @Autowired
    public StudentRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/courses/{id}")
    public List<CourseViewModel> getAllStudents(@PathVariable("id") Long id) {
        return userService.getCourseForUser(id);
    }
}