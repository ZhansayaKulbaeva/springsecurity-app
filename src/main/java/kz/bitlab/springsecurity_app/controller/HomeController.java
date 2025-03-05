package kz.bitlab.springsecurity_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/loginPage")
    private String loginPage() {
        return "login";
    }

    @GetMapping("/myProfile")
    private String myProfile() {
        return "myProfile";
    }
}
