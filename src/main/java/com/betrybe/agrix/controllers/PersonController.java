package com.betrybe.agrix.controllers;

import com.betrybe.agrix.controllers.dto.CreatePersonDto;
import com.betrybe.agrix.controllers.dto.PersonDto;
import com.betrybe.agrix.models.entities.Person;
import com.betrybe.agrix.services.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Fertilizer controller class. */
@RestController
@RequestMapping(value = "/persons")
public class PersonController {

  /** Attributes. */
  private final PersonService personService;

  /** Constructor method. */
  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  /** POST person method. */
  @PostMapping()
  public ResponseEntity<PersonDto> createPerson(@RequestBody CreatePersonDto createPersonDto) {
    Person newPerson = personService.createPerson(createPersonDto.toCreatePerson());

    return ResponseEntity.status(HttpStatus.CREATED).body(new PersonDto(
        newPerson.getId(),
        newPerson.getUsername(),
        newPerson.getRole()));
  }
}
