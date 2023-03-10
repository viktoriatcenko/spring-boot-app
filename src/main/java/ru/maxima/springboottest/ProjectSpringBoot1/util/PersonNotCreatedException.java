package ru.maxima.springboottest.ProjectSpringBoot1.util;

public class PersonNotCreatedException extends RuntimeException {
    public PersonNotCreatedException(String message) {
        super(message);
    }
}
