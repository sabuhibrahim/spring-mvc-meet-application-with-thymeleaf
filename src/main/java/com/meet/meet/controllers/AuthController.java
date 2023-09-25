package com.meet.meet.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.meet.meet.dtos.RegistrationDto;
import com.meet.meet.models.UserEntity;
import com.meet.meet.services.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController extends AbstractController {
    
    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(getClass());


    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String newUser(Model model) {
        RegistrationDto registerForm = new RegistrationDto();

        model.addAttribute("registerForm", registerForm);
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
        @Valid @ModelAttribute("registerForm") RegistrationDto registerForm,
        BindingResult result,
        RedirectAttributes redirectAttributes
    ) {
        
        if(result.hasErrors()) {
            logger.debug("Registration could not complete succesfully");
            return "auth/register";
        }

        UserEntity existingUser = userService.findByEmail(registerForm.getEmail());

        boolean uniqueValid = true;

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            uniqueValid = false;
        }

        existingUser = userService.findByUsername(registerForm.getUsername());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            uniqueValid = false;
        }

        if(!uniqueValid) {
            redirectAttributes.addFlashAttribute(
                "errorMessage", 
                "Username or Email is already exists"
            );
            return "redirect:/register";
        }

        userService.registerUser(registerForm);

        redirectAttributes.addFlashAttribute(
            "successMessage", 
            "Registration completed succesfully"
        );

        return "redirect:/groups";
    }
}
