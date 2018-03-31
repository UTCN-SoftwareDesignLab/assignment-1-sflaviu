package model.validation;

import model.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientValidator implements Validator {

    private final Client client;
    private final List<String> errors;

    public ClientValidator(Client client) {
        this.client=client;
        errors = new ArrayList<>();
    }

    public boolean validate()
    {
        validateCardNr(client.getCardNr());
        validateCnp(client.getCnp());
        return errors.isEmpty();
    }

    private void validateCardNr(String cardNr)
    {
        if(cardNr.length()!=16)
            errors.add("Card number must have 16 characters!");
        if (cardNr.matches("[0-9]+"))
            errors.add("Card number must only contain numbers");
    }

    private void validateCnp(String cnp)
    {
        if(cnp.length()!=10)
            errors.add("Personal numerical code must contain 10 characters!");
        if (cnp.matches("[0-9]+"))
            errors.add("Personal numerical code must contain only digits");
    }


    public List<String> getErrors() {
        return errors;
    }

}
