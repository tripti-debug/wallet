package edu.njit.wallet.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
@Entity
public class RequestTransaction {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int rtid;
    private Double amount;
    private Double totalValue;
    private Date dateortime;
    private String memo;
    private String ssn;
    private String identifier;
    private Double percentage;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    public RequestTransaction() {
    }

    public RequestTransaction(int rtid, Double amount, Double totalValue, Date dateortime, String memo, String ssn, String identifier, Double percentage, TransactionStatus status) {
        this.rtid = rtid;
        this.amount = amount;
        this.totalValue = totalValue;
        this.dateortime = dateortime;
        this.memo = memo;
        this.ssn = ssn;
        this.identifier = identifier;
        this.percentage = percentage;
        this.status = status;
    }

    public RequestTransaction(int rtid, Double amount, Double totalValue, Date dateortime, String memo, String ssn, String identifier, Double percentage) {
        this.rtid = rtid;
        this.amount = amount;
        this.totalValue=totalValue;
        this.dateortime = dateortime;
        this.memo = memo;
        this.ssn=ssn;
        this.identifier=identifier;
        this.percentage=percentage;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public int getRtid() {
        return rtid;
    }

    public void setRtid(int rtid) {
        this.rtid = rtid;
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

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}
