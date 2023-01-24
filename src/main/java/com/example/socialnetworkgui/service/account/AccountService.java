package com.example.socialnetworkgui.service.account;

import com.example.socialnetworkgui.models.account.Account;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.repository.account.AccountRepositoryDB;
import com.example.socialnetworkgui.service.ServiceImplementation;
import com.example.socialnetworkgui.validators.ValidationException;
import com.example.socialnetworkgui.validators.Validator;

import java.sql.SQLException;

public class AccountService extends ServiceImplementation<String, Account> {

    AccountRepositoryDB repository;

    public AccountService(Validator<Account> validator, AccountRepositoryDB repository) {
        super(validator, repository);
        this.repository = repository;
    }

    public void changeAccountPassword(String email, String newPassword) throws SQLException, ValidationException, RepositoryException{
        Account foundAccount = repository.findOne(email);
        Account verifyAccount = new Account(email, newPassword, foundAccount.getUser());
        validator.validate(verifyAccount);
        repository.changeAccountPassword(email, newPassword);
    }

}
