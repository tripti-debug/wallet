package edu.njit.wallet.Repository;
import edu.njit.wallet.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserAccountRepo extends JpaRepository<UserAccount,String> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into user_account(ssn,firstname,lastname,phoneno,email,balance,bank_id,ba_number,bank_balance,verification_amount,pba_verified,password,confirmed) values(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13);")
    void saveUserAccount(String ssn,String firstname,String lastname, String phoneno,String email, Double balance,String bankId, String baNumber,Double bank_balance,Double verificationAmount,String pdaVerified,String password,String confirmed);

    @Transactional
    @Query(nativeQuery = true,value="select ssn,firstname,lastname,phoneno,email,balance,bank_id,ba_number,bank_balance,verification_amount, pba_verified,password,confirmed from user_account  where ssn=?1")
    UserAccount login(String ssn);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE user_account set confirmed=?1 where ssn=?2")
    void updateVerifiy(String confirmed,String ssn);

    @Transactional
    @Query(nativeQuery = true,value="select ssn,firstname,lastname,phoneno,email,balance,bank_id,ba_number,bank_balance,verification_amount,pba_verified as pba_verified,password,confirmed from user_account  where ssn=?1")
    UserAccount getCurrentAmount(String ssn);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE user_account set balance=?1 where ssn=?2")
    void updateNewAmount(Double balance,String ssn);

    @Transactional
    @Query(nativeQuery = true,value="select ssn,firstname,lastname,phoneno,email,balance,bank_id,ba_number,bank_balance,verification_amount,pba_verified as pba_verified,password,confirmed from user_account  where email=?1 or phoneno=?1")
    UserAccount getCurrentAmountOfReceiver(String identifier);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE user_account set balance=?1 where email=?2 or phoneno=?2")
    void updateNewAmountToReceiver(Double balance,String identifier);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE user_account set balance=?1 where ssn=?2")
    void updateWalletAmountForSender(Double balance,String ssn);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE user_account set balance=?1 where email=?2 or phoneno=?2")
    void updateWalletAmountForReceiver(Double balance,String identifier);

    @Transactional
    @Query(nativeQuery = true,value="select balance from user_account  where ssn=?1")
    Double fetchCurrentWalletAmount(String ssn);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value="update user_account set bank_balance=?1 where ssn=?2")
    void updateNewAmountToBank(Double balance,String ssn);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value="update user_account set verification_amount =?1 where ssn=?2")
    void updateVerificationAmount(Double balance,String ssn);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value="update user_account set pba_verified=?2 where ssn=?1")
    void updateVerifiedStatus(String ssn,String Verified);


    @Transactional
    @Query(nativeQuery = true, value = "select * from user_account where ssn = ?1")
    UserAccount getUserAccountBySSN(String ssn);

    @Transactional
    @Query(nativeQuery = true, value = "select * from user_account where phoneno = ?1")
    UserAccount getUserAccountByPhoneNumber(String identifier);

    @Transactional
    @Query(nativeQuery = true, value = "select * from user_account where ssn in ?1")
    List<UserAccount> getUserAccountBySSN(List<String> ssnList);
}

