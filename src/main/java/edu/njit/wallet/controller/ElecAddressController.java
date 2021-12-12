package edu.njit.wallet.controller;

import edu.njit.wallet.model.ElecAddress;
import edu.njit.wallet.model.UserAccount;
import edu.njit.wallet.service.ElecAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("electronic-address")
public class ElecAddressController {
    @Autowired
    private ElecAddressService elecAddressService;
    @PostMapping
    public void saveElecAddress(String identifier,String verified,String type,String ssn){
        try{
            elecAddressService.saveElecAddress(identifier,verified,0,type,ssn);

        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    @PostMapping("generate-otp")
    public int generateEmailOTP(@RequestBody String identifier){
        return elecAddressService.generateOTP(identifier);
    }

    @PostMapping("verify-otp")
    public void verifyOTP(@RequestBody Map<String,Object> requestBody){
        elecAddressService.verifyOTP((String) requestBody.get("identifier"),(String)requestBody.get("ssn"),(int)requestBody.get("otp"));
        // isEmailAndPhoneVerified((String)requestBody.get("ssn"));
    }

    @GetMapping("isEmailAndPhoneVerified")
    public void isEmailAndPhoneVerified(String ssn){
        elecAddressService.isEmailAndPhoneVerified(ssn);
    }

    @PostMapping("isEmailVerified")
    public String isEmailVerified(@RequestBody String ssn){
        return elecAddressService.isEmailVerified(ssn.replace("\"", ""));
    }

    @PostMapping("isPhoneVerified")
    public String isPhoneVerified(@RequestBody String ssn){
        return elecAddressService.isPhoneVerified(ssn.replace("\"", ""));
    }
}
