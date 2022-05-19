package com.blur.cryptobank.controller;

import com.blur.cryptobank.data.Cryptocurrency;
import com.blur.cryptobank.data.DoubleDTO;
import com.blur.cryptobank.data.RegistrationRequest;
import com.blur.cryptobank.data.UserEntity;
import com.blur.cryptobank.service.RegistrationService;
import com.blur.cryptobank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


/**
 * This is the Spring MVC Controller class to handle all user related requests.
 *
 * @author Puskás Levente
 */
@Controller
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private final RegistrationService registrationService;


    /**
     * This method handles the GET request for the default page.
     *
     * @return The name of the view to be rendered.
     */
    @GetMapping("/")
    public String root() {
        return "redirect:cryptos";
    }

    /**
     * This method handles the GET request for the profile page of the currently logged in user.
     * It gets the currently logged in user from the Spring Security context and renders the profile page.
     * If no user is logged in, it redirects to the login page.
     *
     * @param model The model object that is used to pass the user data to the view.
     * @return The name of the view to be rendered.
     */
    @GetMapping("/user/profile")
    public String userProfile(Model model) {
        UserEntity user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "user/profile";
    }

    /**
     * This method handles the GET request for the user profile editing page.
     * It gets the currently logged in user from the Spring Security context and renders the profile editing page with the current user data.
     * If no user is logged in, it redirects to the login page.
     * The password field is left empty.
     * The details of the logged in user are passed to the model object.
     *
     * @param model The model object that is used to pass the user data to the view.
     * @return The name of the view to be rendered.
     */
    @GetMapping("/user/profile/edit")
    public String editProfileForm(Model model) {
        UserEntity user = userService.getCurrentUser();
        user.setPassword("");
        model.addAttribute("user", user);
        return "user/edit_profile";
    }


    /**
     * This method handles the POST request for the user profile editing page.
     * The edited user profile get saved to the database by the UserService.
     *
     * @param user The UserEntity object modified by the editing form.
     * @return The name of the view to be rendered.
     */
    @PostMapping("/user/profile")
    public String saveProfile(@ModelAttribute("user") UserEntity user){
        userService.updateUser(user);
        return "redirect:profile";
    }

    /**
     * The custom login form is rendered by this method.
     * @return The name of the view to be rendered.
     */
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    /**
     * This method handles the GET request for the registration page.
     * The registration form is rendered by this method.
     *
     * @param model The model object that is used to pass RegistrationRequest data to the view.
     * @return The name of the view to be rendered.
     */
    @GetMapping("/register")
    public String signup(Model model) {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        model.addAttribute("registrationRequest", registrationRequest);
        return "user/register";
    }

    /**
     * This method handles the POST request for the registration page.
     * The RegistrationRequest object is filled with the data from the registration form.
     * The RegistrationRequest is passed to the RegistrationService to create a new user.
     * If the registration was successful, the user gets an email with a link to the activation page.
     * Until the user clicks the link, the user is not activated.
     *
     * @param registrationRequest The RegistrationRequest object filled with the data from the registration form.
     * @return The user is redirected to the login page.
     */

    @PostMapping("/register")
    public String saveUser(@ModelAttribute("registrationRequest") RegistrationRequest registrationRequest) {
        registrationService.register(registrationRequest);
        return "redirect:login";
    }


    /**
     * This method handles the GET request from the confirmation email.
     * The activation token is extracted from the URL and passed to the RegistrationService and the user is activated.
     *
     * @param token The token that is extracted from the URL.
     * @return The name of the view to be rendered.
     */
    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token){
        registrationService.confirmToken(token);
        return "redirect:login";
    }

    /**
     * This method handles the GET request for the money depositing page.
     * A deposit form is rendered by this method with a single field for the amount of money to be deposited.
     *
     * @param model The model object that is used to pass the user data to the view.
     * @return The name of the view to be rendered.
     */
    @GetMapping("/user/profile/deposit")
    public String depositModal(Model model) {
        DoubleDTO deposit = new DoubleDTO();
        deposit.setValue(0.0);
        model.addAttribute("deposit", deposit);
        return "deposit";
    }

    /**
     * This method handles the POST request for the money depositing page.
     * The amount of money to be deposited is passed to the UserService.
     *
     * @param deposit The DoubleDTO object containing the amount of money to be deposited.
     * @return Redirecting to the user profile page.
     */
    @PostMapping("/user/profile/deposit")
    public String depositFiat(@ModelAttribute("deposit") DoubleDTO deposit) {
        userService.depositFiat(deposit.getValue());
        return "redirect:/user/profile";
    }


    /**
     * This method handles the GET request for the user administration page.
     * This page is only accessible by the admin user and renders a list of all users.
     *
     * @param model The model object that is used to pass the user data to the view.
     * @return The name of the view to be rendered.
     */
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    /**
     * This method handles the GET request to delete a user.
     * No view is rendered and only the admin is permitted to call this request.
     *
     * @param id The id of the user being deleted.
     * @return Redirecting to the users page.
     */
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

    /**
     * This method handles the GET request called by AJAX to get the current balance of the user revealed.
     * The value is returned in ResponseEntity.
     *
     * @return A ResponseEntity with the balance of the user account.
     */
    @GetMapping("/revealBalance")
    public ResponseEntity<Object> getBalance() {
        Double balance = userService.getCurrentUser().getFiat();
        return new ResponseEntity<Object>(balance, HttpStatus.OK);
    }

    /**
     * This method handles the GET request to for the user wallet page.
     * The users' wallet is rendered by this method.
     *
     * @param model The model object that is used to pass the user data to the view.
     * @return A ResponseEntity with the balance of the user account.
     */
    @GetMapping("/wallet")
    public String wallet(Model model){
        UserEntity user = userService.getCurrentUser();
        Map<Cryptocurrency, Double> wallet = user.getAccount();
        model.addAttribute("wallet" ,wallet.entrySet());
        return "user/wallet";
    }
}

