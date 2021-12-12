package edu.njit.wallet.Repository;

import edu.njit.wallet.model.Email;
import edu.njit.wallet.model.Fromm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface FrommRepo extends JpaRepository<Fromm, Long> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into fromm(identifier,percentage)values(?1,?2)")
    void saveFromDetails(String identifier,int percentage);
}
