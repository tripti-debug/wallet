package edu.njit.wallet.model;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"bankId", "baNumber"})
})
public class BankAccount {
    @Id
    @SequenceGenerator(name="bank_account_seq",
            sequenceName="bank_account_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="bank_account_seq")
    private Long Id;
    private String bankId;
    private String baNumber;

    public BankAccount(String bankId, String baNumber) {
        this.bankId = bankId;
        this.baNumber = baNumber;
    }

    public BankAccount(Long id, String bankId, String baNumber) {
        Id = id;
        this.bankId = bankId;
        this.baNumber = baNumber;
    }

    public BankAccount() {
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public void setBaNumber(String baNumber) {
        this.baNumber = baNumber;
    }

    public String getBankId() {
        return bankId;
    }

    public String getBaNumber() {
        return baNumber;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
