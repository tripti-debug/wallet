package edu.njit.wallet.service;

import edu.njit.wallet.Repository.FrommRepo;
import edu.njit.wallet.model.Fromm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FromService {
    @Autowired
    private FrommRepo frommRepo;
    public void saveFrom(String identifier, int percentage){
        try {
            frommRepo.saveFromDetails(identifier,percentage);
        }catch(Exception e){
            throw e;
        }
    }
}
