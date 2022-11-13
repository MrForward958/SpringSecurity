package by.karzhou.Spring_Security.services;


import by.karzhou.Spring_Security.models.Person;
import by.karzhou.Spring_Security.repositories.PeopleRepository;
import by.karzhou.Spring_Security.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
//Implements UserDetailsService - даем понять Spring что этот сервис работает на security.
//И возвращает пользователя по имени.
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    //Что-то нам такая реализация через интерфейс потом упростит.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUsername(username);

        //Если пользователь не найден, выбросим исключение, пользователь не найден.
        if(person.isEmpty())
            throw new UsernameNotFoundException("User not found!");
        //Если найден оборачиваем нашего человека в personDetails, и возвращаем.
        return  new PersonDetails(person.get());
    }
}
