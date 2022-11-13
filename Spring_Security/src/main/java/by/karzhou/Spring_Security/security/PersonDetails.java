package by.karzhou.Spring_Security.security;

import by.karzhou.Spring_Security.models.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.Collections;

//Класс обертка для нашей сущности, для аутентификации
public record PersonDetails(Person person) implements UserDetails {

    //Для авторизации пользователя по ролям или действиям
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Возвращаем список с ролью текущего person.
        return Collections.singletonList(new SimpleGrantedAuthority(person().getRole()));
    }

    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    @Override
    public String getUsername() {
        return this.person.getUsername();
    }

    //Аккаунт действительный true
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //Аккаунт не заблокирован
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Пароль не просрочен
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //Аккаунт включен
    @Override
    public boolean isEnabled() {
        return true;
    }
}
