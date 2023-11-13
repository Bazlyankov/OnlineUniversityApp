package com.studentsystemapp.controller;

import com.studentsystemapp.model.binding.UserLoginBindingModel;
import com.studentsystemapp.model.binding.UserRegisterBindingModel;
import com.studentsystemapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView showLoginForm(@ModelAttribute("userLoginBindingModel") UserLoginBindingModel userLoginBindingModel) {
        return new ModelAndView("login");
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
            boolean register = userService.register(userRegisterBindingModel);


            return new ModelAndView("redirect:/login");
        }

        return new ModelAndView("register");

    }

    @GetMapping("/logout")
    public ModelAndView logout() {

        return new ModelAndView("redirect:/");
    }



}
