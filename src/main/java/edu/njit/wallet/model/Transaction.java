package edu.njit.wallet.model;


import javax.persistence.*;
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int tid;
    private String transaction_type;
    private int ttid;
    private String from_ssn;
    private String to_identifier;
    private String dateortime;
    private String memo;
    private int amount;
    private int total_amount;



    public Transaction(int tid, String transaction_type, int ttid, String from_ssn, String to_identifier, String dateortime, String memo, int amount, int total_amount) {
        this.tid = tid;
        this.transaction_type = transaction_type;
        this.ttid = ttid;
        this.from_ssn = from_ssn;
        this.to_identifier = to_identifier;
        this.dateortime = dateortime;
        this.memo = memo;
        this.amount = amount;
        this.total_amount = total_amount;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public int getTtid() {
        return ttid;
    }

    public void setTtid(int ttid) {
        this.ttid = ttid;
    }

    public String getFrom_ssn() {
        return from_ssn;
    }

    public void setFrom_ssn(String from_ssn) {
        this.from_ssn = from_ssn;
    }

    public String getTo_identifier() {
        return to_identifier;
    }

    public void setTo_identifier(String to_identifier) {
        this.to_identifier = to_identifier;
    }

    public String getDateortime() {
        return dateortime;
    }

    public void setDateortime(String dateortime) {
        this.dateortime = dateortime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public Transaction() {
    }
}
