package edu.njit.wallet.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserAccount {
    @Id
    private String ssn;
    private String firstname;
    private String lastname;
    private String phoneno;
    private String email;
    private Double balance;
    private String bankId;
    private String baNumber;
    private Double bank_balance;
    private String pbaVerified;
    private String password;
    private String confirmed;
    private Double verificationAmount;
    public UserAccount() {
    }

    public UserAccount(String ssn, String firstname, String lastname, String phoneno,String email, Double balance, String bankId, String baNumber, Double bank_balance,String pbaVerified,String password,String confirmed,Double verificationAmount) {
        this.ssn = ssn;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneno = phoneno;
        this.email=email;
        this.balance = balance;
        this.bankId = bankId;
        this.baNumber = baNumber;
        this.pbaVerified = pbaVerified;
        this.password=password;
        this.confirmed=confirmed;
        this.bank_balance=bank_balance;
        this.verificationAmount=verificationAmount;
    }

    public Double getVerificationAmount() {
        return verificationAmount;
    }

    public void setVerificationAmount(Double verificationAmount) {
        this.verificationAmount = verificationAmount;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public Double getBalance() {
        return balance;
    }

    public Double getBank_balance() {
        return bank_balance;
    }

    public void setBank_balance(Double bank_balance) {
        this.bank_balance = bank_balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }


    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBaNumber() {
        return baNumber;
    }

    public void setBaNumber(String baNumber) {
        this.baNumber = baNumber;
    }

    public String getPbaVerified() {
        return pbaVerified;
    }

    public void setPbaVerified(String pbaVerified) {
        this.pbaVerified = pbaVerified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "ssn='" + ssn + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneno='" + phoneno + '\'' +
                ", balance=" + balance +
                ", bankId='" + bankId + '\'' +
                ", baNumber='" + baNumber + '\'' +
                ", pbaVerified='" + pbaVerified + '\'' +
                '}';
    }
}
