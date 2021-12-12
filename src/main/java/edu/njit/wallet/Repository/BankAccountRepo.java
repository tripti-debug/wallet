package edu.njit.wallet.Repository;

import edu.njit.wallet.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into bank_account(bank_id,ba_number)values(?1,?2)")
    void saveBankDetails(String bankId, String baNumber);
}
