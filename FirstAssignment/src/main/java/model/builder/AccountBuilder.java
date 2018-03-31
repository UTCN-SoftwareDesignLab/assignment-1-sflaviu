package model.builder;

import model.Account;
import model.Client;

import java.sql.Date;

public class AccountBuilder {

    private Account account;

    public AccountBuilder() {
        account=new Account();
    }

    public AccountBuilder setId(Long id) {
        account.setId(id);
        return this;
    }

    public AccountBuilder setOwner(Client owner){
        account.setOwner(owner);
        return this;
    }

    public AccountBuilder setType(String type) {
        account.setType(type);
        return this;
    }

    public AccountBuilder setBalance(int balance) {
        account.setBalance(balance);
        return this;
    }

    public AccountBuilder setCreation(Date date) {
        account.setCreation(date);
        return this;
    }

    public Account build() { return account; }
}
