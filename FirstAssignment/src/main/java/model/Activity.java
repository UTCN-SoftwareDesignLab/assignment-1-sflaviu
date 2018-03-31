package model;

import java.sql.Date;

public class Activity {
    private Long id;
    private User performer;
    private Date date;
    private String type;
    private Client modifiedClient;
    private Account modifiedAccount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPerformer() {
        return performer;
    }

    public void setPerformer(User performer) {
        this.performer = performer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Client getModifiedClient() {
        return modifiedClient;
    }

    public void setModifiedClient(Client modifiedClient) {
        this.modifiedClient = modifiedClient;
    }

    public Account getModifiedAccount() {
        return modifiedAccount;
    }

    public void setModifiedAccount(Account modifiedAccount) {
        this.modifiedAccount = modifiedAccount;
    }
}
