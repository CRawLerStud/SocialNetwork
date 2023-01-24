package com.example.socialnetworkgui.validators.account;

import com.example.socialnetworkgui.models.account.Account;
import com.example.socialnetworkgui.validators.ValidationException;
import com.example.socialnetworkgui.validators.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountValidator implements Validator<Account> {

    UserValidator userValidator;

    public AccountValidator(UserValidator userValidator){
        this.userValidator = userValidator;
    }

    @Override
    public void validate(Account entity) throws ValidationException {
        validateEmail(entity.getEmail());
        validatePassword(entity.getPassword());
        userValidator.validate(entity.getUser());
    }

    private void validatePassword(String password) throws ValidationException{
        if(password.trim().length() < 6 || password.length() < 6 || password.length() > 50)
            throw new ValidationException("Invalid password!");

        String regex = "[0-9]{2,}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        if(!matcher.find())
            throw new ValidationException("Invalid password!");
    }

    private void validateEmail(String email) throws ValidationException{
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if(!matcher.matches())
            throw new ValidationException("Invalid email!");
    }
}
