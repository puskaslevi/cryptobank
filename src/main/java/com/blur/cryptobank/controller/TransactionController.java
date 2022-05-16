package com.blur.cryptobank.controller;

import antlr.ASTNULLType;
import com.blur.cryptobank.data.Cryptocurrency;
import com.blur.cryptobank.data.Transaction;
import com.blur.cryptobank.data.UserEntity;
import com.blur.cryptobank.service.CryptoService;
import com.blur.cryptobank.service.impl.TransactionService;
import com.blur.cryptobank.service.impl.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class TransactionController {

    private TransactionService transactionService;
    private CryptoService cryptoService;
    private UserService userService;

    @GetMapping("/transfer")
    public String transferForm(Model model) {
        Transaction transaction = new Transaction();
        model.addAttribute("transaction", transaction);
        model.addAttribute("cryptos", cryptoService.getAllCrypto());
        List<UserEntity> users = userService.findAll();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = userService.findUserByUserName(auth.getName()).get();
        users.remove(currentUser);
        model.addAttribute("users", users);
        return "transfer";
    }
    
    @PostMapping("/transfer")
    public String makeTransfer(@ModelAttribute("transaction") Transaction transaction,
                               @ModelAttribute("cryptos") ArrayList<Cryptocurrency> cryptos,
                               @ModelAttribute("users") ArrayList<UserEntity> users) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        transaction.setSender(userService.findUserByUserName(auth.getName()).get());
        System.out.println(transaction.getReceiver());
        transactionService.makeTransaction(transaction);
        return "redirect:/";
    }
}
