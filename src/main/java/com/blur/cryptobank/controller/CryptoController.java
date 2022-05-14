package com.blur.cryptobank.controller;

import com.blur.cryptobank.data.Cryptocurrency;
import com.blur.cryptobank.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CryptoController {

    private CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService){
        super();
        this.cryptoService = cryptoService;
    }

    @GetMapping("/cryptos")
    public String listCryptos(Model model) {
        model.addAttribute("cryptos", cryptoService.getAllCrypto());
        return "cryptos";
    }

    @GetMapping("/cryptos/new")
    public String createCryptoForm(Model model){
        Cryptocurrency crypto = new Cryptocurrency();
        model.addAttribute("crypto", crypto);
        return "create_crypto";
    }

    @PostMapping("/cryptos")
    public String saveCrypto(@ModelAttribute("crypto") Cryptocurrency cryptocurrency) {
        cryptoService.saveCrypto(cryptocurrency);
        return "redirect:/cryptos";
    }

    @GetMapping("/cryptos/edit/{id}")
    public String editCryptoForm(@PathVariable Long id, Model model) {
        model.addAttribute("crypto", cryptoService.getCryptoById(id));
        return "edit_crypto";
    }

    @PostMapping("/cryptos/{id}")
    public String updateStudent(@PathVariable Long id,
                                @ModelAttribute("crypto") Cryptocurrency crypto,
                                Model model) {

        // get student from database by id
        Cryptocurrency cryptoToEdit = cryptoService.getCryptoById(id);
        cryptoToEdit.setName(crypto.getName());
        cryptoToEdit.setSymbol(crypto.getSymbol());
        cryptoToEdit.setValue(crypto.getValue());

        // save updated student object
        cryptoService.updateCrypto(cryptoToEdit);
        return "redirect:/cryptos";
    }
    @GetMapping("/cryptos/{id}")
    public String deleteCrypto(@PathVariable Long id) {
        cryptoService.deleteCryptoById(id);
        return "redirect:/cryptos";
    }
}
