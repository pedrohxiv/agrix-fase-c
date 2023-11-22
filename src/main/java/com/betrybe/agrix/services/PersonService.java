package com.betrybe.agrix.services;

import com.betrybe.agrix.models.entities.Person;
import com.betrybe.agrix.models.repositories.PersonRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Person service class. */
@Service
public class PersonService {

  /** Attributes. */
  private final PersonRepository personRepository;

  /** Constructor method. */
  @Autowired
  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  /** Get person by ID method. */
  public Optional<Person> getPersonById(Long id) {
    Optional<Person> person = personRepository.findById(id);

    if (person.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(person.get());
  }

  /** Get person by username method. */
  public Optional<Person> getPersonByUsername(String username) {
    Optional<Person> person = personRepository.findByUsername(username);

    if (person.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(person.get());
  }

  /** Create person method. */
  public Person createPerson(Person person) {
    return personRepository.save(person);
  }
}
