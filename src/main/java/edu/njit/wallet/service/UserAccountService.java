package edu.njit.wallet.service;

import edu.njit.wallet.Repository.ElecAddressRepo;
import edu.njit.wallet.Repository.HasAdditionalRepo;
import edu.njit.wallet.Repository.UserAccountRepo;
import edu.njit.wallet.model.ElecAddress;
import edu.njit.wallet.model.HasAdditional;
import edu.njit.wallet.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserAccountService {
    @Autowired
    private UserAccountRepo userAccountRepo;
    @Autowired
    private HasAdditionalRepo hasAdditionalRepo;
    @Autowired
    private HasAdditionalService hasAdditionalService;

    @Autowired
    private ElecAddressRepo elecAddressRepo;

    @Autowired
    private EmailService emailService;

    public void saveUser(UserAccount userAccount){
        try {
            userAccountRepo.saveUserAccount(userAccount.getSsn(),userAccount.getFirstname(),
                    userAccount.getLastname(),userAccount.getPhoneno(),userAccount.getEmail(),0.00,userAccount.getBankId(),
                    userAccount.getBaNumber(),userAccount.getBank_balance(),0.00,"False",userAccount.getPassword(),"False");
        } catch (Exception e) {
            throw e;
        }

    }
    public UserAccount login(String ssn,String password){
        try{
            UserAccount usr= userAccountRepo.login(ssn);
            if(usr.getPassword().equals(password)){
                return usr;
            } else{
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception e){
            throw e;
        }
    }
    public Double deductMoney(Double amount,String ssn){
        try{
            UserAccount currentAmount=userAccountRepo.getCurrentAmount(ssn);
            Double updatedAmount=currentAmount.getBalance()-amount;
            userAccountRepo.updateNewAmount(updatedAmount,ssn);
            return updatedAmount;
        }catch(Exception e){
            throw e;
        }
    }

    public Double addMoney(Double amount,String ssn){
        try{
            UserAccount currentAmount=userAccountRepo.getCurrentAmount(ssn);
            Double updatedAmount=currentAmount.getBalance()+amount;
            userAccountRepo.updateNewAmount(updatedAmount,ssn);
            return updatedAmount;
        }catch(Exception e){
            throw e;
        }
    }
    public Double addMoneyByIdentifier(Double amount,String identifier){
        try{
            UserAccount currentAmount=userAccountRepo.getCurrentAmountOfReceiver(identifier);
            Double updatedAmount=currentAmount.getBalance()+amount;
            userAccountRepo.updateNewAmountToReceiver(updatedAmount,identifier);
            return updatedAmount;
        }catch(Exception e){
            throw e;
        }
    }

    public Double deductMoneyByIdentifier(Double amount,String identifier){
        try{
            UserAccount currentAmount=userAccountRepo.getCurrentAmountOfReceiver(identifier);
            Double updatedAmount=currentAmount.getBalance()-amount;
            userAccountRepo.updateNewAmountToReceiver(updatedAmount,identifier);
            return updatedAmount;
        }catch(Exception e){
            throw e;
        }
    }
    public Double deductFromBankToVerify(String ssn){
        try{

            UserAccount userAccount=userAccountRepo.getCurrentAmount(ssn);
            if(userAccount.getPbaVerified().equals("False".replace("\"", "\'"))){
                if(userAccount.getVerificationAmount()!=0.00) {
                    Double addPreviousVerificationAmountToBank = userAccount.getVerificationAmount() + userAccount.getBank_balance();
                    userAccountRepo.updateNewAmountToBank(addPreviousVerificationAmountToBank, ssn);
                }
                Double amount=generateRandomDouble();
                Double updatedAmount=userAccount.getBank_balance()-amount;
                userAccountRepo.updateNewAmountToBank(updatedAmount,ssn);
                hasAdditionalRepo.updateBankBalance(updatedAmount,userAccount.getBankId(),userAccount.getBaNumber());
                UserAccount userAccountAmount= userAccountRepo.getCurrentAmount(ssn);
                Double updateWalletAmount=userAccountAmount.getBalance()+amount;
                userAccountRepo.updateWalletAmountForSender(updateWalletAmount,ssn);
                userAccountRepo.updateVerificationAmount(amount,ssn);
                return amount;
            }
            return 0.00;
        }catch(Exception e){
            throw e;
        }
    }

    public String verifyVerificationAmount(String ssn,Double amount){

            UserAccount userAccount = userAccountRepo.getCurrentAmount(ssn);
            if (userAccount.getVerificationAmount().equals(amount) ) {
                hasAdditionalRepo.updateVerificationAmountInHasAdditional(amount,ssn,"True");
                userAccountRepo.updateVerifiedStatus(ssn,"True");
                return "Verified";
            }else{
                return "Not Verified";
            }
    }
    private double generateRandomDouble() {
        double num = new Random().nextDouble();
        return new BigDecimal(num).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    public String isBankVerified(String ssn) {
        UserAccount userAccount = userAccountRepo.getCurrentAmount(ssn);
        if (userAccount.getPbaVerified().equals("True")){
            return "Verified";
        }else
            return "Not Verified";
    }
    public String addMoneyToWalletFromBank(String accountNumber, String bankId, Double amount, String ssn ) {
        HasAdditional hasAdditional=hasAdditionalRepo.returnHasAdditionalReference(bankId,accountNumber);
        if(hasAdditional.getVerified().equals("True")){
            UserAccount userAccount=userAccountRepo.getCurrentAmount(ssn);
            userAccountRepo.updateNewAmount((userAccount.getBalance()+amount),ssn);
            hasAdditionalRepo.updateBankBalance(hasAdditional.getBank_balance()-amount,bankId,accountNumber);
            UserAccount reference=userAccountRepo.getCurrentAmount(ssn);
            if(reference.getBankId().equals(bankId)&&reference.getBaNumber().equalsIgnoreCase(accountNumber)){
                userAccountRepo.updateNewAmountToBank(hasAdditional.getBank_balance()-amount,ssn);
            }
            return "Ok";
        }else{
            return "Not added";
        }
    }

    public UserAccount getUserBySSN(String ssn) {
        return userAccountRepo.getUserAccountBySSN(ssn);
    }

    public void updateUserAccount(UserAccount account) {
        userAccountRepo.save(account);
    }

    public UserAccount getUserByIdentifier(String identifier) {
        UserAccount userAccount = userAccountRepo.getUserAccountByPhoneNumber(identifier);
        if (userAccount != null) {
            return userAccount;
        }
        return userAccountRepo.getUserAccountBySSN(emailService.getSSNFromEmail(identifier));
    }

    public UserAccount updateUserAccount(Map<String, Object> body) {
        boolean emailChanged = false, phoneChanged = false;
        UserAccount userAccount = userAccountRepo.getUserAccountBySSN((String) body.get("ssn"));
        if (userAccount == null) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "no user account found");
        userAccount.setFirstname((String) body.get("firstname"));
        userAccount.setLastname((String) body.get("lastname"));
        if (!body.get("email").toString().equals(userAccount.getEmail())) {
            emailChanged = true;
        }
        if (!body.get("phonenumber").toString().equals(userAccount.getPhoneno())) {
            phoneChanged = true;
        }
        userAccount.setEmail((String) body.get("email"));
        userAccount.setPhoneno((String) body.get("phonenumber"));
        if (!body.get("password").toString().equals("********************")) {
            userAccount.setPassword((String) body.get("password"));
        }
        if (emailChanged) {
            ElecAddress newElecAddress = new ElecAddress(userAccount.getEmail(), "false", 0, "Email", userAccount.getSsn());
            elecAddressRepo.save(newElecAddress);
        }
        if (phoneChanged) {
            ElecAddress newElecAddress = new ElecAddress(userAccount.getPhoneno(), "false", 0, "Phone", userAccount.getSsn());
            elecAddressRepo.save(newElecAddress);
        }
        return userAccountRepo.save(userAccount);
    }

    public List<UserAccount> getUserBySSN(List<String> ssnList) {
        return userAccountRepo.getUserAccountBySSN(ssnList);
    }

    public Map<String, String> depositMoney(String ssn, String ac_number, Double amount) {
        Optional<HasAdditional> optionalHasAdditional = hasAdditionalService.getAdditionalBanks(ssn)
                .stream()
                .filter(item -> item.getBaNumber().equals(ac_number))
                .collect(Collectors.toList())
                .stream().findFirst();
        if (optionalHasAdditional.isEmpty()) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "no bank account found");
        HasAdditional hasAdditional = optionalHasAdditional.get();
        UserAccount userAccount = userAccountRepo.getUserAccountBySSN(ssn);
        if (amount > userAccount.getBalance()) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "no sufficient balance to transfer");
        userAccount.setBalance(userAccount.getBalance() - amount);
        hasAdditional.setBank_balance(hasAdditional.getBank_balance() + amount);
        userAccountRepo.save(userAccount);
        hasAdditionalService.update(hasAdditional);
        return Map.of("status", "amount deposit successful");
    }
}
