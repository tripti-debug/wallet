package edu.njit.wallet.Repository;
import edu.njit.wallet.model.SendTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface SendTransactionRepo extends JpaRepository<SendTransaction, Integer> {
        @Transactional
        @Modifying
        @Query(nativeQuery = true, value = "insert into send_transaction(dateortime,memo,cancel_reason,identifier,ssn,amount,total_amount, status)values(?1,?2,?3,?4,?5,?6,?7,?8)")
        void saveSendDetails(Date date, String memo, String cancel, String identifier, String ssn, Double amount, Double total_amount, String status);

        @Transactional
        @Modifying
        @Query(nativeQuery = true, value = "update send_transaction set total_amount=?1 where stid=?2")
        void updateAmount(Double amount, int stid);

        @Transactional
        @Query(nativeQuery = true, value = "select max(stid) from send_transaction")
        Integer getSendTransactionId();

        @Transactional
        @Query(nativeQuery = true, value = "select * from send_transaction where ssn = ?1")
        List<SendTransaction> findBySSN(String ssn);

        @Transactional
        @Query(nativeQuery = true, value = "select * from send_transaction where stid = ?1")
        SendTransaction getSendTransactionId(int id);

        @Transactional
        @Query(nativeQuery = true, value = "select * from send_transaction where status = ?1")
        List<SendTransaction> getAllByStatus(String status);

        @Transactional
        @Query(nativeQuery = true, value = "select * from send_transaction where identifier in ?1")
        List<SendTransaction> getAllByIdentifier(List<String> identifiers);
}