package edu.njit.wallet.controller;
import edu.njit.wallet.model.BankAccount;
import edu.njit.wallet.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("bank-account")
public class BankController {
    @Autowired
    private BankService bankService;

    @PostMapping
    public void saveBankDetails(@RequestBody Map<String, String> requestBody){

        try {
            bankService.saveBankDetails(requestBody.get("bankID"),requestBody.get("baNumber"));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

}
