package edu.njit.wallet.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ElecAddress {
    @Id
    private String identifier;
    private String verified;
    int otp;
    private String typeOf;
    private String ssn;

    public ElecAddress() {
    }

    public ElecAddress(String identifier, String verified, int otp, String typeOf,String ssn) {
        this.identifier = identifier;
        this.verified = verified;
        this.otp = otp;
        this.typeOf = typeOf;
        this.ssn=ssn;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getTypeOf() {
        return typeOf;
    }

    public void setTypeOf(String typeOf) {
        this.typeOf = typeOf;
    }

}
