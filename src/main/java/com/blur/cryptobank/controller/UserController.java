package com.blur.cryptobank.controller;

import com.blur.cryptobank.data.DoubleDTO;
import com.blur.cryptobank.data.RegistrationRequest;
import com.blur.cryptobank.data.UserEntity;
import com.blur.cryptobank.service.RegistrationService;
import com.blur.cryptobank.service.impl.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private final RegistrationService registrationService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/")
    public String root() {
        return "redirect:cryptos";
    }

    @GetMapping("/user/profile")
    public String userProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserEntity user = userService.findUserByUserName(auth.getName()).get();
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/user/profile/edit")
    public String editProfileForm(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserEntity user;
        boolean exists = userService.findUserByUserName(auth.getName()).isPresent();
        if (exists) {
            user = userService.findUserByUserName(auth.getName()).get();
            user.setPassword("");
            model.addAttribute("user", user);
            return "user/edit_profile";
        }
        return "user/login";
    }

    @PostMapping("/user/profile")
    public String saveProfile(@ModelAttribute("user") UserEntity user){
        userService.updateUser(user);
        return "redirect:profile";
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/register")
    public String signup(Model model) {

        RegistrationRequest registrationRequest = new RegistrationRequest();
        model.addAttribute("registrationRequest", registrationRequest);
        return "user/register";
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token){
        registrationService.confirmToken(token);
        return "redirect:user/login";
    }

    @PostMapping("/register")
    public String saveUser(@ModelAttribute("registrationRequest") RegistrationRequest registrationRequest) {
        registrationService.register(registrationRequest);
        return "redirect:login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/error/access-denied";
    }

    @GetMapping("/user/profile/deposit")
    public String depositModal(Model model) {
        DoubleDTO deposit = new DoubleDTO();
        deposit.setValue(0.0);
        model.addAttribute("deposit", deposit);
        return "deposit";
    }

    @PostMapping("/user/profile/deposit")
    public String depositFiat(@ModelAttribute("deposit") DoubleDTO deposit) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userService.depositFiat(auth.getName(), deposit.getValue());

        return "redirect:/user/profile";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("/users/lock/{id}")
    public String lockUnlockUser(@PathVariable Long id) {
        userService.lockUnlock(id);
        return "redirect:/users";
    }


}

