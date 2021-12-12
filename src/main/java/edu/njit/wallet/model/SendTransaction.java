package edu.njit.wallet.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
@Entity
public class SendTransaction {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int stid;
    private Double amount;
    private Double totalValue;
    private Date dateortime;
    private String memo;
    private String cancel_reason;
    private String identifier;
    private String ssn;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    public SendTransaction() {
    }

    public SendTransaction(int stid, Double amount, Double totalValue, Date dateortime, String memo, String cancel_reason, String identifier, String ssn, TransactionStatus status) {
        this.stid = stid;
        this.amount = amount;
        this.totalValue = totalValue;
        this.dateortime = dateortime;
        this.memo = memo;
        this.cancel_reason = cancel_reason;
        this.identifier = identifier;
        this.ssn = ssn;
        this.status = status;
    }

    public SendTransaction(int stid, Double amount, Double totalValue, Date dateortime, String memo, String cancel_reason, String identifier, String ssn) {
        this.stid = stid;
        this.amount = amount;
        this.totalValue=totalValue;
        this.dateortime = dateortime;
        this.memo = memo;
        this.cancel_reason = cancel_reason;
        this.identifier = identifier;
        this.ssn = ssn;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public int getStid() {
        return stid;
    }

    public void setStid(int stid) {
        this.stid = stid;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDateortime() {
        return dateortime;
    }

    public void setDateortime(Date dateortime) {
        this.dateortime = dateortime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}
