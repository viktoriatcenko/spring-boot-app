package ru.maxima.springboottest.ProjectSpringBoot1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RestAPIController {

    @RequestMapping("/hello")
    public String sayHello() {
        return "Hello World!";
    }

    @RequestMapping("/goodbye")
    public String sayGoodbye() {
        return "Goodbye!";
    }

}
