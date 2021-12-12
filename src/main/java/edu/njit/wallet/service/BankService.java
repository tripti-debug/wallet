package edu.njit.wallet.service;

import edu.njit.wallet.Repository.BankAccountRepo;
import edu.njit.wallet.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankService {
        @Autowired
        private BankAccountRepo bankAccountRepo;

        public void saveBankDetails(String bankId,String baNumber){
        try{
            bankAccountRepo.saveBankDetails(bankId,baNumber);
        } catch (Exception e) {
            throw e;
         }
    }
}
