package com.studentsystemapp.controller;

import com.studentsystemapp.model.binding.EnquiryAddBindingModel;
import com.studentsystemapp.model.view.EnquiryViewModel;
import com.studentsystemapp.service.EnquiryService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/enquiries")
public class EnquiryController {

    private final EnquiryService enquiryService;

    public EnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    @GetMapping("/{id}/add")
    public ModelAndView addEnquiryGet(@PathVariable("id") Long id) {


        ModelAndView modelAndView = new ModelAndView("enquiry-add");

        EnquiryAddBindingModel enquiryAddBindingModel = new EnquiryAddBindingModel();

        modelAndView.addObject("enquiryAddBindingModel", enquiryAddBindingModel);
        modelAndView.addObject("studentId", id);

        return modelAndView;

    }

    @PostMapping("/{id}/add")
    public String addEnquiryPost(@PathVariable("id") Long id, @ModelAttribute("enquiryAddBindingModel") EnquiryAddBindingModel enquiryAddBindingModel) {


        ModelAndView modelAndView = new ModelAndView("enquiry-add");
        enquiryAddBindingModel.setStudentId(id);
        enquiryService.add(enquiryAddBindingModel);

        return "redirect:/home";

    }


    @GetMapping("/{id}/respond")
    public ModelAndView addResponse(@PathVariable("id") Long id) {


        EnquiryViewModel enquiryViewModel = enquiryService.getById(id);
        ModelAndView modelAndView = new ModelAndView("enquiry-respond");
        modelAndView.addObject("enquiry", enquiryViewModel);

        return modelAndView;

    }

    @PostMapping("/{id}/respond")
    public ModelAndView addResponsePost(@PathVariable("id") Long id, @ModelAttribute("enquiry") EnquiryViewModel enquiry) {

        enquiryService.addResponse(enquiry.getResponse(), id);

        return new ModelAndView("redirect:/home");

    }

}
