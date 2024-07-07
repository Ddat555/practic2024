package com.example.practic2024.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OtherController {

    //Get на главную страницу
    @GetMapping("/")
    public String getHome(Model model){
        return "home";
    }

    //Get на страницу "О нас"
    @GetMapping("/about")
    public String getAbout(Model model){
        return "about";
    }

    //Get на регистрацию
    @GetMapping("/registration")
    public String getRegistration(Model model){
        return "registration";
    }

    @GetMapping("/login")
    public String getLogin(Model model){
        return "login";
    }

}
