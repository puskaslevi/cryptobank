package com.blur.cryptobank.controller;

import com.blur.cryptobank.data.RegistrationRequest;
import com.blur.cryptobank.data.UserEntity;
import com.blur.cryptobank.data.UserRole;
import com.blur.cryptobank.service.RegistrationService;
import com.blur.cryptobank.service.impl.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private final RegistrationService registrationService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @GetMapping("/")
    public String root() {
//        UserEntity user = new UserEntity("admin", "ADMIN", "ADMIN", "admin@admin.com", "admin", UserRole.ADMIN);
//        user.setEnabled(true);
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        userService.updateUser(user);
        return "index";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String signup(Model model) {

        RegistrationRequest registrationRequest = new RegistrationRequest();
        model.addAttribute("registrationRequest", registrationRequest);
        return "register";
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token){
        registrationService.confirmToken(token);
        return "redirect:/login";
    }

    @PostMapping("/register")
    public String saveUser(@ModelAttribute("registrationRequest") RegistrationRequest registrationRequest) {
        registrationService.register(registrationRequest);
        return "redirect:/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/error/access-denied";
    }

}

