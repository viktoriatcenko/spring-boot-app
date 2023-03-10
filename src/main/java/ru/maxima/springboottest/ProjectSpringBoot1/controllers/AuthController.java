package ru.maxima.springboottest.ProjectSpringBoot1.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.springboottest.ProjectSpringBoot1.dto.PersonDTO;
import ru.maxima.springboottest.ProjectSpringBoot1.models.Person;
import ru.maxima.springboottest.ProjectSpringBoot1.security.PersonDetails;
import ru.maxima.springboottest.ProjectSpringBoot1.services.AdminService;
import ru.maxima.springboottest.ProjectSpringBoot1.services.RegistrationService;
import ru.maxima.springboottest.ProjectSpringBoot1.util.JWTUtil;
import ru.maxima.springboottest.ProjectSpringBoot1.validate.PersonValidator;

import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService service;
    private final PersonValidator validator;
    private final AdminService adminService;
    private final ModelMapper modelMapper;
    private final JWTUtil jwtUtil;

    @Autowired
    public AuthController(RegistrationService service, PersonValidator validator, AdminService adminService, ModelMapper modelMapper, JWTUtil jwtUtil) {
        this.service = service;
        this.validator = validator;
        this.adminService = adminService;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
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
    public Map<String, String> performRegistration(@RequestBody @Valid PersonDTO personDTO,
                                                   BindingResult bindingResult) {
        Person person = convertToPerson(personDTO);

        validator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return Map.of("message", "Ошибка!");
        }

        service.register(person);
        String token = jwtUtil.generateToken(person.getName());

        return Map.of("jwt-token", token);
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

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    @GetMapping("/showUser")
    @ResponseBody
    public Person getPeople() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return personDetails.getPerson();
    }

}
