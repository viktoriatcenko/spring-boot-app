package ru.maxima.springboottest.ProjectSpringBoot1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hello")
public class HelloController {


    @GetMapping()
    public String getPeople() {
        return "Hello from Docker";
    }
}
