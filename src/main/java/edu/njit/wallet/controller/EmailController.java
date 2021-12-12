package edu.njit.wallet.controller;

import edu.njit.wallet.model.Email;
import edu.njit.wallet.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("email")
public class EmailController {
    @Autowired
   private EmailService emailService;
    @PostMapping
    public void saveEmailAddress(String email,String ssn){
        try {
            emailService.saveEmail(email,ssn);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }

    }



}
