package com.studentsystemapp.controller;


import com.studentsystemapp.model.entity.Course;
import com.studentsystemapp.service.CourseService;
import com.studentsystemapp.service.ResourceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final CourseService courseService;

    public ResourceController(ResourceService resourceService, CourseService courseService) {

        this.resourceService = resourceService;
        this.courseService = courseService;
    }

    @GetMapping("/{courseId}")
    public String courseResources(@PathVariable("courseId") Long courseId, Model model) {

        Optional<Course> optionalCourse = courseService.findById(courseId);

        if (optionalCourse.isEmpty()) {
            //TODO: handle error
            return "all-students";
        }

        Course course = optionalCourse.get();

        model.addAttribute("resources", course.getCourseResources());
        model.addAttribute("course", course);

        return "course-resources";
    }

    @PostMapping("/{courseId}/delete/{resourceId}")
    public String deleteResource( @PathVariable("courseId") Long courseId, @PathVariable("resourceId") Long resourceId) {

        resourceService.delete(courseId, resourceId);

        return "redirect:/resources/{courseId}";


    }

}
