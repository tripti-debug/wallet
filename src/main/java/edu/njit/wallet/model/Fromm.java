package edu.njit.wallet.model;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"rtid", "identifier"})
})
public class Fromm {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int rtid;
    private String identifier;
    private int percentage;


    public Fromm() {
    }

    public Fromm(int rtid, String identifier, int percentage) {
        this.rtid = rtid;
        this.identifier = identifier;
        this.percentage = percentage;
    }

    public int getRtid() {
        return rtid;
    }

    public void setRtid(int rtid) {
        this.rtid = rtid;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

}
