package com.studentsystemapp.controller;

import com.studentsystemapp.model.binding.EnquiryAddBindingModel;
import com.studentsystemapp.model.view.EnquiryViewModel;
import com.studentsystemapp.service.EnquiryService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;

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
    public ModelAndView addEnquiryPost(@PathVariable("id") Long id,
                                 @Valid @ModelAttribute("enquiryAddBindingModel") EnquiryAddBindingModel enquiryAddBindingModel,
                                 BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("redirect:/home");
        modelAndView.addObject("studentId", id);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("hasErrors", true);
            modelAndView.setViewName("enquiry-add");
            return modelAndView;
        }
        enquiryAddBindingModel.setStudentId(id);
        try {
            enquiryService.add(enquiryAddBindingModel);
        } catch (NoSuchElementException e) {
            modelAndView.addObject("invalidInput", "Teacher with the provided id is not found");
            modelAndView.setViewName("enquiry-add");
        }

        return modelAndView;

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
