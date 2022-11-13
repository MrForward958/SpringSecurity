package by.karzhou.Spring_Security.services;

import by.karzhou.Spring_Security.models.Person;
import by.karzhou.Spring_Security.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RegistrationService {

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Шифруем пароль и сохраняем зарегистрированного пользователя
    @Transactional
    public void register(Person person){
        //Шифруем пароль с помощью нашего passwordEncoder.
        String encoderPassword = passwordEncoder.encode(person.getPassword());
        //Перезаписываем человеку вместо обычного пароля, зашифрованный.
        person.setPassword(encoderPassword);
        //Назначаем роль всем новым пользователям.
        person.setRole("ROLE_USER");
        //Сохраняем человека в БД.
        peopleRepository.save(person);
    }
}
