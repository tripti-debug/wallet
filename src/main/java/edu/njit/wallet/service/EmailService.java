package edu.njit.wallet.service;

import edu.njit.wallet.Repository.EmailRepo;
import edu.njit.wallet.model.Email;
import edu.njit.wallet.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Random;

@Service
public class EmailService {
    @Autowired
    private EmailRepo emailRepo;

    public void saveEmail(String email,String ssn){
        try {
            emailRepo.saveEmail(email,ssn);
        }catch (Exception e) {
            throw e;
        }
    }

    public List<Email> getAllEmailsBySSN(String ssn) {
        return emailRepo.getAllBySSN(ssn);
    }

    public String getSSNFromEmail(String email) {
        return emailRepo.getSSNFromEmail(email);
    }
}
