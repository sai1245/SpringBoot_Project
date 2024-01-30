package io.endeavour.stocks.service;

import io.endeavour.stocks.StockException;
import io.endeavour.stocks.entity.crud.Address;
import io.endeavour.stocks.entity.crud.Person;
import io.endeavour.stocks.repository.crud.AddressRepository;
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

    @Autowired
    AddressRepository addressRepository;

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

    public Person updatePerson(Person person, Integer personID) {
        if (personRepository.existsById(personID)){
            return insertPerson(person);
        }
        else {
            throw new StockException("Given Person to be updated , doesnot exist in the database");
        }

    }

    public void deletePerson(Integer personID){
        if (personRepository.existsById(personID)){
            personRepository.deleteById(personID);
        }
        else {
            throw new StockException("Sent personID does not exist in the databse to be deleated");
        }
    }

    public List<Address> getAllAddresses(){
        return addressRepository.findAll();
    }
}
