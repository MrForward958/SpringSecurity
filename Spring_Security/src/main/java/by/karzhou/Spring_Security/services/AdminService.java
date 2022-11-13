package by.karzhou.Spring_Security.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    //Указали что к этому методу доступ имеет только пользователь с ролью admin
    public void doAdminStuff(){
        System.out.println("Only admin here");
    }
}
