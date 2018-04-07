package model.validation;

import java.util.List;

public class PaymentValidator implements Validator {

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public List<String> getErrors() {
        return null;
    }
}
