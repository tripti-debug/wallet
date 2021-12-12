package edu.njit.wallet.Repository;

import edu.njit.wallet.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Repository
public interface ReceiveRepo extends JpaRepository<Transaction,Integer> {
       @Transactional
       @Modifying
       @Query(nativeQuery = true,value="Insert into receive(ttid,send_from_ssn,receive_to_identifier,dateortime,memo,amount,total_amount) values(?1,?2,?3,?4,?5,?6,?7)")
       void saveTransactionDetails(int ttid,String send_from_ssn, String receive_to_identifier,String dateortime,String memo,Double amount,Double total_amount);

       @Transactional
       @Modifying
       @Query(nativeQuery = true,value="Insert into receive(ttid,request_from_identifier,receive_to_ssn,dateortime,memo,amount,total_amount) values(?1,?2,?3,?4,?5,?6,?7)")
       void saveRequestTransactionDetails(int ttid,String request_from_identifier, String receive_to_ssn,String dateortime,String memo,Double amount,Double total_amount);


       @Transactional
       @Query(nativeQuery = true,value="Select * from receive where send_from_ssn=?1 and dateortime LIKE '---'?2")
       Map<String, String> viewByMonth(String from_ssn, String dateortime);

       @Transactional
       @Query(nativeQuery = true,value="Select * from receive where send_from_ssn=?1 and dateortime=cast(?2 as text)")
       List<Transaction> viewByDate(String from_ssn, String date);
}