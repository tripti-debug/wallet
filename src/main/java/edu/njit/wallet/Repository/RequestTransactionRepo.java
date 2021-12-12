package edu.njit.wallet.Repository;

import edu.njit.wallet.model.HasAdditional;
import edu.njit.wallet.model.RequestTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface RequestTransactionRepo extends JpaRepository<RequestTransaction, Integer> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into request_transaction( dateortime,memo,identifier,ssn,amount,total_amount,status)values(?1,?2,?3,?4,?5,?6, ?7)")
    void saveRequestDetails(Date date,String memo,String identifier,String ssn,Double amount, Double total_amount, String status);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update request_transaction set total_amount=?1 where rtid=?2")
    void updateAmount(Double amount, int rtid);

    @Transactional
    @Query(nativeQuery = true, value = "select max(rtid) from request_transaction")
    Integer getRequestTransactionId();

    @Transactional
    @Query(nativeQuery = true, value = "select * from request_transaction where rtid = ?1")
    RequestTransaction getRequestTransactionId(int id);

    @Transactional
    @Query(nativeQuery = true, value = "select * from request_transaction where ssn = ?1 or identifier in ?2")
    List<RequestTransaction> getRequestTransactionBySSN(String ssn, List<String> identifiers);
}