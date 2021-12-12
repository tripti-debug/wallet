package edu.njit.wallet.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Email {
    @Id
    private String emailAdd;
    private String ssn;


    public Email() {
    }

    public Email(String emailAdd, String ssn) {
        this.emailAdd = emailAdd;
        this.ssn = ssn;
    }

    public String getEmailAdd() {
        return emailAdd;
    }

    public void setEmailAdd(String emailAdd) {
        this.emailAdd = emailAdd;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
}
