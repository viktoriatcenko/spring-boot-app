package ru.maxima.springboottest.ProjectSpringBoot1.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.maxima.springboottest.ProjectSpringBoot1.models.Person;
import ru.maxima.springboottest.ProjectSpringBoot1.services.AdminService;
import ru.maxima.springboottest.ProjectSpringBoot1.services.RegistrationService;
import ru.maxima.springboottest.ProjectSpringBoot1.validate.PersonValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService service;
    private final PersonValidator validator;
    private final AdminService adminService;

    @Autowired
    public AuthController(RegistrationService service, PersonValidator validator, AdminService adminService) {
        this.service = service;
        this.validator = validator;
        this.adminService = adminService;
    }


    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person,
                                      BindingResult bindingResult) {
        validator.validate(person, bindingResult);
        service.register(person);
        return "redirect:/auth/login";
    }

    @GetMapping("/admin")
    public String adminPage() {
        adminService.doAdminThings();
        return "auth/admin";
    }

    @GetMapping("/logout")
    public String logOutPage() {
        return "logout";
    }

}
