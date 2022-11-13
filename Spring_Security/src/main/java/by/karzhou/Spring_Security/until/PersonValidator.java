package by.karzhou.Spring_Security.until;

import by.karzhou.Spring_Security.models.Person;
import by.karzhou.Spring_Security.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;



@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        //В нашем сервисе мы реализовали loadUserByUsername с выбросом исключения, поэтому тут ловим его
        //Это говно код, нужно написать отдельный сервис с нормальным методом.
        try {
            personDetailsService.loadUserByUsername(person.getUsername());
        } catch (UsernameNotFoundException ignored){
            return; //все ок пользователь с таким именем не найдем
        }

        errors.rejectValue("username","","Человек с таким именем пользователя существует");

    }
}
