package com.blur.cryptobank.controller;

import com.blur.cryptobank.data.Cryptocurrency;
import com.blur.cryptobank.data.DoubleDTO;
import com.blur.cryptobank.data.Transaction;
import com.blur.cryptobank.data.UserEntity;
import com.blur.cryptobank.service.CryptoService;
import com.blur.cryptobank.service.TransactionService;
import com.blur.cryptobank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is a Spring MVC controller class for Cryptocurrency related requests. It is responsible for mapping the requests to the appropriate methods.
 * @author Puskás Levente
 */
@AllArgsConstructor
@Controller
public class CryptoController {

    private CryptoService cryptoService;
    private TransactionService transactionService;
    private UserService userService;

    /**
     * This is the index page of the application. The list of cryptocurrencies is displayed on this page in a table format.
     * The cryptocurrency list is passed to the model object as an attribute. This way Thymeleaf can render the list of cryptocurrencies.
     *
     * @param model The model object that is used to pass data to the view.
     * @return The view with a table of cryptocurrencies in the database which serves as the index page.
     */
    @GetMapping("/cryptos")
    public String listCryptos(Model model) {
        model.addAttribute("cryptos", cryptoService.getAllCrypto());
        return "crypto/cryptos";
    }

    /**
     * This is the GET mapping for the Cryptocurrency creation page. The model object gets a new, empty Cryptocurrency object.
     * This page can be accessed only by a logged-in user with the role "ADMIN".
     *
     * @param model The model object that is used to pass the empty Cryptocurrency to the view.
     * @return A cryptocurrency creation form.
     */
    @GetMapping("/cryptos/new")
    public String createCryptoForm(Model model){
        Cryptocurrency crypto = new Cryptocurrency();
        model.addAttribute("crypto", crypto);
        return "crypto/create_crypto";
    }

    /**
     * This is the POST mapping for the Cryptocurrency creation form.
     * The Cryptocurrency object is created from the form data provided by the Model object as a @ModelAttribute.
     * The Cryptocurrency object is then saved to the database via the CryptoService.
     * Only a logged-in user with the role "ADMIN" can POST to this endpoint.
     *
     * @param cryptocurrency The Cryptocurrency object that is created from the form data.
     * @return Redirecting to the index page.
     */
    @PostMapping("/cryptos")
    public String saveCrypto(@ModelAttribute("crypto") Cryptocurrency cryptocurrency) {
        cryptoService.saveCrypto(cryptocurrency);
        return "redirect:/";
    }

    /**
     * This is the GET mapping for the Cryptocurrency update page.
     * The Cryptocurrency object is retrieved from the database via the CryptoService based on the id provided in the URL.
     * The Cryptocurrency object is then passed to the model object as an attribute.
     * The form will display the current attributes of the Cryptocurrency as a default input.
     * Only a logged-in user with the role "ADMIN" can update a Cryptocurrency object.
     *
     * @param id The id of the Cryptocurrency object that is retrieved from the database.
     * @param model The model object that is used to pass the Cryptocurrency object to the view.
     * @return A cryptocurrency update form.
     */
    @GetMapping("/cryptos/edit/{id}")
    public String editCryptoForm(@PathVariable Long id, Model model) {
        model.addAttribute("crypto", cryptoService.getCryptoById(id));
        return "crypto/edit_crypto";
    }

    /**
     * This is the POST mapping for the Cryptocurrency update form.
     * The Cryptocurrency object is modified by the form data provided by the Model object as a @ModelAttribute.
     * The Cryptocurrency object is then updated and saved to the database by the CryptoService.
     *
     * @param crypto The Cryptocurrency object that is created from the form data.
     * @return Redirecting to the index page.
     */
    @PostMapping("/cryptos/edit/{id}")
    public String updateCrypto(@PathVariable Long id,
                                @ModelAttribute("crypto") Cryptocurrency crypto,
                                Model model) {

        cryptoService.updateCrypto(crypto);
        return "redirect:/";
    }

    /**
     * This is the GET mapping for deleting a Cryptocurrency.
     * The Cryptocurrency object is deleted based on the ID provided in the path.
     * The Cryptocurrency object then is deleted via the CryptoService based on this ID.
     * This is only accessed by a logged-in user with the role "ADMIN".
     *
     * @param id The id of the Cryptocurrency object that is retrieved from the database.
     * @return Redirecting to the index page.
     */
    @GetMapping("/cryptos/{id}")
    public String deleteCrypto(@PathVariable Long id) {
        cryptoService.deleteCryptoById(id);
        return "redirect:/";
    }

    /**
     * This is the GET mapping for the Cryptocurrency trading page.
     * The Cryptocurrency object is retrieved from the database via the CryptoService based on the id provided in the URL.
     * The Cryptocurrency object is then passed to the model object as an attribute.
     * The form will display the current attributes of the Cryptocurrency as a default input.
     *
     * @param trade This is a string parameter that tells the view what kind of trade to display (buying or selling)
     *              The trade parameter is passed by in the GET mapping of the form.
     * @param id The id of the Cryptocurrency object that is retrieved from the database.
     * @param model The model object that is used to pass attributes to the view.
     * @return A cryptocurrency trading form.
     */
    @GetMapping("/cryptos/trade/{id}")
    public String tradeCrypto(@RequestParam String trade, @PathVariable Long id, Model model){
        Cryptocurrency crypto = cryptoService.getCryptoById(id);

        // The coin which is being traded is passed to the model object as an attribute.
        model.addAttribute("crypto", crypto);

        // How much "real money" is in the user's account
        model.addAttribute("balance", userService.getCurrentUser().getFiat());

        // This DTO type for Double is used because Double doesn't have a default empty constructor.
        DoubleDTO value = new DoubleDTO(0.0);
        model.addAttribute("value", value);

        // This parameter determines which form to display (buy or sell).
        model.addAttribute("trade", trade);

        // The amount that the user has of the selected coin.
        model.addAttribute("cryptoAmount", userService.getCurrentUser().getAccount().get(crypto));
        return "crypto/trade_crypto";
    }

    /**
     * This is the POST mapping for the Cryptocurrency trading form.
     *
     *
     * @param trade This is a string parameter that tells the tradeCrypto method what kind of trade to be made (buying or selling)
     *              The trade parameter is passed by in the POST request.
     * @param id The id of the Cryptocurrency object that is being traded.
     * @param value The amount of the cryptocurrency that the user wants to trade,
     *              and also it can be the amount of Fiat money that the user wants to spend on the given cryptocurrency.
     * @return Redirecting to the index page.
     */
    @PostMapping("/cryptos/trade/{id}")
    public String makeTrade(@RequestParam String trade, @PathVariable Long id,
                            @ModelAttribute("value") DoubleDTO value){
        transactionService.tradeCrypto(id, value.getValue(), trade);
        return "redirect:/";
    }

    /**
     * This is the GET mapping for the transaction page.
     * An empty Transaction object is created and passed to the model object as an attribute.
     * The user can select a recipient from the list of registered users.
     *
     *
     *
     * @param id The id of the Cryptocurrency object in which the transaction is being made.
     * @param model The model object that is used to pass the necessary attributes to the view.
     * @return The transaction form view.
     */
    @GetMapping("/cryptos/transfer/{id}")
    public String transferForm(@PathVariable Long id, Model model) {
        Transaction transaction = new Transaction();
        Cryptocurrency crypto = cryptoService.getCryptoById(id);
        transaction.setCurrency(crypto);
        model.addAttribute("transaction", transaction);
        model.addAttribute("crypto", crypto);
        List<UserEntity> users = userService.findAll();
        UserEntity currentUser = userService.getCurrentUser();
        users.remove(currentUser);
        model.addAttribute("users", users);
        model.addAttribute("account", currentUser.getAccount());
        return "transfer";
    }

    /**
     * This is the POST mapping for the transaction form.
     * The Transaction object is passed by the Model.
     * The Transaction object is then saved to the database by the TransactionService.
     * An Exception is thrown if the user tries to transfer more coins than they have, though the form handles this with validation.
     *
     * @param transaction The Transaction object that is created from the form data.
     * @return Redirecting to the index page.
     */
    @PostMapping("/cryptos/transfer/{id}")
    public String makeTransfer(@PathVariable Long id, @ModelAttribute("transaction") Transaction transaction
                               ) throws Exception {
        try {
            transactionService.makeTransaction(transaction);
        } catch (Exception e){
            return "/error/forbidden";
        }
        return "redirect:/";
    }


    /**
     * This is the GET mapping for a request made by an AJAX call.
     * This is to filter the Cryptocurrency table based on symbol and name.
     * This is achieved asynchronously by the AJAX call, so the page doesn't need to be reloaded.
     * Based on the filtering criteria, the cryptoService returns a filtered list of Cryptocurrency objects.
     *
     * @param symbol The symbol filter parameter that is passed in the URL.
     * @param name The name filter parameter that is passed in the URL.
     * @return A response is returned with a list of coins.
     */
    @GetMapping("/getCryptos")
    public ResponseEntity<Object> getCryptos(@RequestParam("symbol") String symbol, @RequestParam("name") String name) {
        List<Cryptocurrency> cryptos = cryptoService.getAllCryptoFilter(symbol, name);
        return new ResponseEntity<Object>(cryptos, HttpStatus.OK);
    }
}
