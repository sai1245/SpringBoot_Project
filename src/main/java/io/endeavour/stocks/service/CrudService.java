package io.endeavour.stocks.service;

import io.endeavour.stocks.entity.crud.Address;
import io.endeavour.stocks.entity.crud.Person;
import io.endeavour.stocks.repository.crud.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CrudService {

    private final static Logger LOGGER= LoggerFactory.getLogger(CrudService.class);
    @Autowired
    PersonRepository personRepository;

    public List<Person> getAllPersons(){
        LOGGER.debug("In the getAllPersons() method of the {} class", getClass());
        return personRepository.findAll();
    }
    public Person insertPerson(Person person){
        Optional<List<Address>> addressListOptional = Optional.ofNullable(person.getAddressList());
        addressListOptional.ifPresent(addresses -> {
            addresses.forEach(address -> {
                address.setPerson(person);
            });
        });
        return  personRepository.save(person);
    }


    public Optional<Person> getPerson(Integer personID) {
        return personRepository.findById(personID);
    }
}
