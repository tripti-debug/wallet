package edu.njit.wallet.service;

import edu.njit.wallet.Repository.ElecAddressRepo;
import edu.njit.wallet.Repository.UserAccountRepo;
import edu.njit.wallet.model.ElecAddress;
import edu.njit.wallet.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Random;

@Service
public class ElecAddressService {
    @Autowired
    private ElecAddressRepo elecAddressRepo;

    @Autowired
    private UserAccountRepo userAccountRepo;

    public void saveElecAddress(String identifier, String verified, int otp, String type, String ssn) {
        try {
            System.out.println("hiiiiii");
            elecAddressRepo.saveElecAddress(identifier, verified, 0, type, ssn);
        } catch (Exception e) {
            throw e;
        }
    }

    public int generateOTP(String identifier) {
        Random random = new Random();
        int otp = ((random.nextInt(9) + 1) * 10000 + (random.nextInt(9) + 1) * 1000 + (random.nextInt(9) + 1) * 100 + (random.nextInt(9) + 1) * 10 + random.nextInt(9));
        elecAddressRepo.saveOTP(otp, identifier.replace("\"", ""));
        System.out.println(otp);
        return otp;
    }

    public String verifyOTP(String identifier, String ssn, int otp) {
        try {
            ElecAddress usr = elecAddressRepo.verifyOTPP(identifier, ssn);
            if (usr.getOtp() == otp) {
                elecAddressRepo.updateVerified("true", identifier);
                return "verified";
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void isEmailAndPhoneVerified(String ssn) {
        try {
            ElecAddress emailAddress = elecAddressRepo.isVerified("Email", "true", ssn);
            ElecAddress phoneAddress = elecAddressRepo.isVerified("Phone", "true", ssn);
            if (emailAddress != null && phoneAddress != null &&
                    emailAddress.getVerified().equals("true") && phoneAddress.getVerified().equals("true")) {
                userAccountRepo.updateVerifiy("True", ssn);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public String isEmailVerified(String ssn) {
        ElecAddress emailAddress = elecAddressRepo.isVerified("Email", "true", ssn);
        if (emailAddress.getVerified().equals("true")) {
            return "Verified";
        }else
            return "Not Verified";
    }


    public String isPhoneVerified(String ssn) {
        ElecAddress phone = elecAddressRepo.isVerified("Phone", "true", ssn);
        if (phone.getVerified().equals("true")) {
            return "Verified";
        }else
            return "Not Verified";
    }

    public List<String> getSsnByIdentifiers(List<String> identifier) {
        return elecAddressRepo.getByIdentifiers(identifier);
    }
}