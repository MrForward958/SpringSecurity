package by.karzhou.Spring_Security.controllers;

import by.karzhou.Spring_Security.security.PersonDetails;
import by.karzhou.Spring_Security.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HelloController {

    private final AdminService adminService;

    @Autowired
    public HelloController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/hello")
    public String hello(){
        return "/hello";
    }

    @GetMapping("/get")
    public String get(){
        //Получаем Authentication пользователя из сессии.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //Получаем его Principal (personDetails), и downcast до personDetails
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        System.out.println(personDetails.person());
        return "/hello";
    }

    @GetMapping("/admin")
    public String adminPage(){
        adminService.doAdminStuff();
        return "/admin";
    }
}
