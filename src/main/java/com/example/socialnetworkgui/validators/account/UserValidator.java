package com.example.socialnetworkgui.validators.account;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.validators.ValidationException;
import com.example.socialnetworkgui.validators.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        validateSurname(entity.getSurname());
        validateLastname(entity.getLastname());
        validateAge(entity.getYears());
    }

    private void validateAge(int years) throws ValidationException{
        if(years < 13)
            throw new ValidationException("Invalid Age!");
    }

    private void validateLastname(String lastname) throws ValidationException{
        if(lastname.length() == 0)
            throw new ValidationException("Invalid Lastname!");
        Pattern pattern = Pattern.compile("^[A-Z][A-Za-z ]+");
        Matcher matcher = pattern.matcher(lastname);
        if (!(matcher.matches()) && lastname.trim().length() > 0)
            throw new ValidationException("Invalid Lastname!");
    }

    private void validateSurname(String surname) throws ValidationException{
        if(surname.length() == 0)
            throw new ValidationException("Invalid Surname!");
        Pattern pattern = Pattern.compile("^[A-Z][A-Za-z ]+");
        Matcher matcher = pattern.matcher(surname);
        if (!(matcher.matches()) && surname.trim().length() > 0)
            throw new ValidationException("Invalid Surname!");
    }
}
