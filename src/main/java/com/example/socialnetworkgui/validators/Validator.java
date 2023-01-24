package com.example.socialnetworkgui.validators;

public interface Validator<T> {

    void validate(T entity) throws ValidationException;
}
