package edu.njit.wallet.Repository;

import edu.njit.wallet.model.Fromm;
import edu.njit.wallet.model.HasAdditional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface HasAdditionalRepo extends JpaRepository<HasAdditional, Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into has_additional(ssn,bank_id,ba_number,bank_balance,amount_deducted,verified)values(?1,?2,?3,?4,?5,?6)")
    void saveAdditionalDetails(String ssn,String bank_id, String ba_number,Double bank_balance, Double amount_deducted,String verified);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value="update has_additional set amount_deducted=?1,verified=?3 where ssn=?2")
    void updateVerificationAmountInHasAdditional(Double balance,String ssn,String Verified);

    @Transactional
    @Query(nativeQuery = true,value="Select ssn as id, ssn,bank_id as bank_id,ba_number as ba_number,bank_balance,amount_deducted,verified from has_additional where ba_number=?2 and bank_id=?1")
    HasAdditional returnHasAdditionalReference(String bank_id, String ba_number);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value="update has_additional set bank_balance=?1 where ba_number=?3 and bank_id=?2")
    void updateBankBalance(Double balance,String bank_id, String ba_number);

    @Transactional
    @Query(nativeQuery = true,value="Select id, ssn,bank_id as bank_id,ba_number as ba_number,bank_balance,amount_deducted,verified from has_additional")
    List<HasAdditional> getAdditionalBanks();
}
