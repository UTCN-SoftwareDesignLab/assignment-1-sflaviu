package model.validation;

import model.Account;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountValidator implements Validator{

    private final Account account;
    private final List<String> errors;

    public AccountValidator(Account account) {
        this.account=account;
        errors = new ArrayList<>();
    }

    public boolean validate()
    {
        validateBalance(account.getBalance());
        validateCreation(account.getCreation());
        return errors.isEmpty();
    }

    private void validateBalance(int balance)
    {
        if(balance<0)
            errors.add("Can not start account with a negative balance!");
    }

    private void validateCreation(Date creation)
    {
        if(creation.after(new Date()))
            errors.add("No time travelling allowed");
    }

    public List<String> getErrors() {
        return errors;
    }
}
