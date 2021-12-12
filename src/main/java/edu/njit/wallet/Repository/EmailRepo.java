package edu.njit.wallet.Repository;

import edu.njit.wallet.model.Email;
import edu.njit.wallet.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface EmailRepo extends JpaRepository<Email, String> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into email( email_add,ssn)values(?1,?2)")
    void saveEmail(String emailAdd,String ssn);

    @Transactional
    @Query(nativeQuery = true, value = "select * from email where ssn = ?1")
    List<Email> getAllBySSN(String ssn);

    @Transactional
    @Query(nativeQuery = true, value = "select ssn from email where email_add = ?1")
    String getSSNFromEmail(String email);
}
