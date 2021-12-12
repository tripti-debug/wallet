package edu.njit.wallet.controller;

import edu.njit.wallet.Repository.HasAdditionalRepo;
import edu.njit.wallet.Repository.UserAccountRepo;
import edu.njit.wallet.model.ElecAddress;
import edu.njit.wallet.model.Email;
import edu.njit.wallet.model.HasAdditional;
import edu.njit.wallet.model.UserAccount;
import edu.njit.wallet.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController ///allows you to make network api
@RequestMapping( "user-account")
public class AccountController {
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private ElecAddressService elecAddressService;
    @Autowired
    private BankService bankService;
    @Autowired
    private EmailService service;
    @Autowired
    private UserAccountRepo userAccountRepo;
    @Autowired
    private HasAdditionalRepo hasAdditionalRepo;
    @PostMapping
    public void createUserAccount(@RequestBody UserAccount userAccount){
        try {
            elecAddressService.saveElecAddress(userAccount.getEmail(),"false",0,"Email",userAccount.getSsn());
            elecAddressService.saveElecAddress(userAccount.getPhoneno(),"false",0,"Phone",userAccount.getSsn());
            bankService.saveBankDetails(userAccount.getBankId(),userAccount.getBaNumber());
            userAccountService.saveUser(userAccount);
            hasAdditionalRepo.saveAdditionalDetails(userAccount.getSsn(),userAccount.getBankId(),userAccount.getBaNumber(),userAccount.getBank_balance(),0.00,"False");
            service.saveEmail(userAccount.getEmail(),userAccount.getSsn());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }
    @PostMapping("login")
    public UserAccount login(@RequestBody Map<String,String> requestBody){
        return userAccountService.login(requestBody.get("ssn"),requestBody.get("password"));
    }
    @PostMapping("get-current-amount")
    public Double fetchCurrentAmount(@RequestBody String ssn){
        return userAccountRepo.fetchCurrentWalletAmount(ssn.replace("\"", ""));

    }
    @PostMapping("deduct-money-from-bank-to-verify")
    public Double deductMoney(@RequestBody String ssn){
        return userAccountService.deductFromBankToVerify(ssn.replace("\"", ""));
    }

    @PostMapping("verify-bank")
    public String verifyBank(@RequestBody Map<String,String> requestBody){
        return userAccountService.verifyVerificationAmount((String)requestBody.get("ssn"),Double.parseDouble(requestBody.get("verificationAmount")));
    }

    @PostMapping("isBankVerified")
    public String isBankVerified(@RequestBody String ssn){
        return userAccountService.isBankVerified(ssn.replace("\"", ""));
    }
    @PostMapping("add-money-to-wallet")
    public String  addMoneyToWallet(@RequestBody Map<String,String> requestBody){
        return userAccountService.addMoneyToWalletFromBank((String)requestBody.get("accountnumber"),(String)requestBody.get("bankid"),Double.parseDouble(requestBody.get("amount")),(String)requestBody.get("ssn"));
    }

    @PostMapping("update")
    public ResponseEntity<UserAccount> updateUserAccount(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(userAccountService.updateUserAccount(body));
    }

    @PostMapping("deposit_money")
    public ResponseEntity<Map<String, String>> depositMoney(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(userAccountService.depositMoney((String) body.get("ssn"), (String) body.get("ac_number"), Double.parseDouble((String) body.get("amount"))));
    }
}



