package ru.maxima.springboottest.ProjectSpringBoot1.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.springboottest.ProjectSpringBoot1.dto.PersonDTO;
import ru.maxima.springboottest.ProjectSpringBoot1.models.Person;
import ru.maxima.springboottest.ProjectSpringBoot1.services.PeopleService;
import ru.maxima.springboottest.ProjectSpringBoot1.util.PersonErrorResponse;
import ru.maxima.springboottest.ProjectSpringBoot1.util.PersonNotCreatedException;
import ru.maxima.springboottest.ProjectSpringBoot1.util.PersonNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class PeopleRestController {

    private final PeopleService peopleService;
    private final PersonDTO personDTO;
    private final ModelMapper modelMapper;


    @Autowired
    public PeopleRestController(PeopleService peopleService, PersonDTO personDTO, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.personDTO = personDTO;
        this.modelMapper = modelMapper;
    }


    @GetMapping()
    public List<PersonDTO> getPeople() {
        return peopleService.findAll().stream()
                .map(this::convertToPersonDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable("id") int id) {
        return convertToPersonDTO(peopleService.findOne(id));
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }


    /**
     * POST - localhost:8080/
     * RequestBode requires JSON
     * {
     *         "name": "NAME",
     *         "age": 35,
     *         "email": "test@mail.ru"
     *     }
     *  Response OK = 200
     *  Response BAD_REQUEST = 400
     *  {
     *      "message" : "Could not to create Person",
     *      "timestamp" : "2023-03-03 20:40:05.438629+03"
     *  }
     */
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder bld = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> {
                bld.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            });
            throw new PersonNotCreatedException(bld.toString());
        }
        peopleService.save(convertToPerson(personDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }


//
//    @GetMapping("/{id}/edit")
//    public String edit(Model model, @PathVariable("id") int id) {
//        model.addAttribute("person", peopleService.findOne(id));
//        return "people/edit";
//    }
//
//    @PatchMapping("/{id}")
//    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
//                         @PathVariable("id") int id) {
//        if (bindingResult.hasErrors())
//            return "people/edit";
//
//        peopleService.update(id, person);
//        return "redirect:/people";
//    }
//
//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") int id) {
//        peopleService.delete(id);
//        return "redirect:/people";
//    }

    @ExceptionHandler
    public ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                "Person with this id is not found",
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
