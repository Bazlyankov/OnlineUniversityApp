package com.studentsystemapp.controller;

import com.studentsystemapp.model.binding.PasswordChangeForm;
import com.studentsystemapp.model.binding.UserLoginBindingModel;
import com.studentsystemapp.model.binding.UserRegisterBindingModel;
import com.studentsystemapp.service.TeacherService;
import com.studentsystemapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class UserController {


    private final UserService userService;
    private final TeacherService teacherService;

    public UserController( UserService userService, TeacherService teacherService) {
        this.userService = userService;
        this.teacherService = teacherService;
    }

    @GetMapping("/login")
    public ModelAndView showLoginForm(@ModelAttribute("userLoginBindingModel") UserLoginBindingModel userLoginBindingModel) {
        return new ModelAndView("login");
    }

    @GetMapping("/{id}/change-password")
    public ModelAndView changePasswordGet(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("change-password");
        modelAndView.addObject("passwordChangeForm", new PasswordChangeForm());

        return modelAndView;
    }

    @PostMapping("/{id}/change-password")
    public ModelAndView changePassword(@PathVariable("id") Long id, @ModelAttribute("passwordChangeForm") PasswordChangeForm passwordChangeForm) {
        ModelAndView modelAndView = new ModelAndView("change-password");
        if (!passwordChangeForm.getPassword().equals(passwordChangeForm.getConfirmPassword())) {
            modelAndView.addObject("invalidInput", "Password and confirm password don't match.");
            modelAndView.setViewName("change-password");
            return modelAndView;
        }
        userService.changePassword(id, passwordChangeForm.getPassword());
        return new ModelAndView("redirect:/students");
    }


    @PostMapping("/login")
    public ModelAndView postLogin(@ModelAttribute("userLoginBindingModel") @Valid UserLoginBindingModel userLoginBindingModel, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("login");
        }

        return new ModelAndView("redirect:/");
    }

    @PostMapping("/login-error")
    public ModelAndView onFailure(@ModelAttribute("userLoginBindingModel") UserLoginBindingModel userLoginBindingModel,
            BindingResult bindingResult,
            Model model) {

        model.addAttribute("username", userLoginBindingModel.getUsername());
        model.addAttribute("bad_credentials", "true");

        return new ModelAndView("login");
    }




    @GetMapping("/register")
    public ModelAndView register(@ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel) {

        return new ModelAndView("register");
    }


    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute("userRegisterBindingModel") @Valid UserRegisterBindingModel userRegisterBindingModel,
                                 BindingResult bindingResult) {

        boolean hasErrors = bindingResult.hasErrors();

        if(!hasErrors) {
            List<String> registerErrors = userService.register(userRegisterBindingModel);
            if (!registerErrors.isEmpty()) {
                ModelAndView modelAndView = new  ModelAndView("register");
                modelAndView.addObject("errors", registerErrors);
                return modelAndView;
            }
            return new ModelAndView("redirect:/login");
        }

        return new ModelAndView("register");

    }

    @GetMapping("/logout")
    public ModelAndView logout() {

        return new ModelAndView("redirect:/");
    }

    @GetMapping("/{id}/make-student")
    public String makeTeacher(@PathVariable("id") Long id) {

        teacherService.makeStudent(id);
        return "redirect:/students";
    }


}
