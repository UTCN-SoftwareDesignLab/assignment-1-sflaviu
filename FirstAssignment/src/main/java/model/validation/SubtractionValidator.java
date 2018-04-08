package model.validation;

import model.Account;

import java.util.ArrayList;
import java.util.List;

public class SubtractionValidator implements Validator {

    private final Account account;
    private final int sum;
    private final List<String> errors;

    public SubtractionValidator(Account account,int sum) {
        this.account=account;
        this.sum=sum;
        errors = new ArrayList<>();
    }
    @Override
    public boolean validate() {
        validateSum();
        validateFunds();
        return errors.isEmpty();
    }

    private void validateSum()
    {
        if(sum<=0)
            errors.add("The retracted sum must be a non-negative number!");
    }
    private void validateFunds()
    {
        if(account.getBalance()-sum<0)
            errors.add("Insufficient funds!");
    }


    @Override
    public List<String> getErrors() {
        return errors;
    }
}
