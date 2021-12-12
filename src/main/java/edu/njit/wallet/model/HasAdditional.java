package edu.njit.wallet.model;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"ssn", "bankId","baNumber"})
})
public class HasAdditional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String ssn;
    private String bankId;
    private String baNumber;
    private double bank_balance;
    private double amount_deducted;
    private String verified;


    public HasAdditional() {
    }

    public HasAdditional(String ssn, String bankId, String baNumber,Double bank_balance,Double amount_deducted, String verified) {
        this.ssn = ssn;
        this.bankId = bankId;
        this.baNumber = baNumber;
        this.bank_balance=bank_balance;
        this.amount_deducted=amount_deducted;
        this.verified = verified;
    }


    public double getBank_balance() {
        return bank_balance;
    }

    public void setBank_balance(double bank_balance) {
        this.bank_balance = bank_balance;
    }

    public double getAmount_deducted() {
        return amount_deducted;
    }

    public void setAmount_deducted(double amount_deducted) {
        this.amount_deducted = amount_deducted;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
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

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

}
