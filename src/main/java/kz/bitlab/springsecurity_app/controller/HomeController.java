package kz.bitlab.springsecurity_app.controller;

import kz.bitlab.springsecurity_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    @GetMapping("/loginPage")
    private String loginPage() {
        return "login";
    }

    @GetMapping("/myProfile")
    private String myProfile() {
        return "myProfile";
    }

    @GetMapping("/register")
    private String registerPage() {
        return "register";
    }

    @PreAuthorize("hasAnyRole('ROLE_MODERATOR')")
    @GetMapping("/moderator")
    private String moderatorPage() {
        return "moderatorPage";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    private String adminPage() {
        return "adminPage";
    }

    @PostMapping("/register")
    public String register(@RequestParam("fullName") String fullName,
                           @RequestParam("email") String email,
                           @RequestParam("password") String password,
                           @RequestParam("repeatPassword") String repeatPassword) {
        Boolean check = userService.addUser(fullName, email, password, repeatPassword);
        if (check){
            return "redirect:/loginPage";
        }else {
            return "redirect:register?someError";
        }
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("repeatNewPassword") String repeatNewPassword) {
        Boolean check = userService.updatePassword(oldPassword, newPassword, repeatNewPassword);
        if (check){
            return "redirect:/myProfile";
        }else {
            return "redirect:myProfile?someError";
        }
    }
}
