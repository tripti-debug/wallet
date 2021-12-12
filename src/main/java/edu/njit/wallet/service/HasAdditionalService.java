package edu.njit.wallet.service;

import edu.njit.wallet.Repository.HasAdditionalRepo;
import edu.njit.wallet.model.HasAdditional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HasAdditionalService {
    @Autowired
    private HasAdditionalRepo hasAdditionalRepo;

    public void saveHasAdditional(HasAdditional hasAdditional) {
        try {
            hasAdditionalRepo.saveAdditionalDetails(hasAdditional.getSsn(),hasAdditional.getBankId(), hasAdditional.getBaNumber(),hasAdditional.getBank_balance(),hasAdditional.getAmount_deducted(),hasAdditional.getVerified());
        } catch (Exception e) {
            throw e;
        }
    }

    public List<HasAdditional> getAdditionalBanks(String ssn) {
        List<HasAdditional> hasAdditionals = hasAdditionalRepo.getAdditionalBanks();
        return hasAdditionals
                .stream()
                .filter(hasAdditional -> hasAdditional.getSsn().equals(ssn))
                .collect(Collectors.toList());
    }

    public void update(HasAdditional hasAdditional) {
        hasAdditionalRepo.save(hasAdditional);
    }
}
