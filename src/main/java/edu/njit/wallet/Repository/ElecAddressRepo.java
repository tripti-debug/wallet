package edu.njit.wallet.Repository;

import edu.njit.wallet.model.ElecAddress;
import edu.njit.wallet.model.UserAccount;
import edu.njit.wallet.service.ElecAddressService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ElecAddressRepo extends JpaRepository<ElecAddress, String> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "INSERT into elec_address(identifier,verified,otp,type_of,ssn) values (?1,?2,?3,?4,?5)")
    public void saveElecAddress(String identifier,String verified,int otp,String typeOf,String ssn);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value = "UPDATE elec_address set otp=:otp where identifier=:identifier")
    void saveOTP(@Param("otp")int otp, @Param("identifier") String identifier);

    @Transactional
    @Query(nativeQuery = true,value="select identifier,verified,otp,type_of,ssn from elec_address where identifier=?1 and ssn=?2")
    ElecAddress verifyOTPP(String identifier,String ssn);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE elec_address set verified=?1 where identifier=?2")
    void updateVerified(String verified,String identifier);

    @Transactional
    @Query(nativeQuery = true,value="Select identifier,verified,otp,type_of,ssn from elec_address where verified=?2 and ssn=?3 and type_of=?1")
    ElecAddress isVerified(String type,String verified,String ssn);

    @Transactional
    @Query(nativeQuery = true, value = "select * from elec_address where identifier = ?1")
    ElecAddress getByIdentifier(String identifier);

    @Transactional
    @Query(nativeQuery = true, value = "select ssn from elec_address where identifier in ?1")
    List<String> getByIdentifiers(List<String> identifier);
}
