package com.example.socialnetworkgui.service;

import com.example.socialnetworkgui.repository.Repository;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.validators.ValidationException;
import com.example.socialnetworkgui.validators.Validator;

import java.sql.SQLException;

public class ServiceImplementation<ID, T> implements Service<ID, T>{

    protected Validator<T> validator;
    protected Repository<ID, T> repository;

    public ServiceImplementation(Validator<T> validator, Repository<ID, T> repository){
        this.validator = validator;
        this.repository = repository;
    }
    @Override
    public ID save(T entity) throws RepositoryException, SQLException, ValidationException {
        validator.validate(entity);
        return repository.save(entity);
    }

    @Override
    public T delete(ID id) throws RepositoryException, SQLException {
        return repository.delete(id);
    }

    @Override
    public T findOne(ID id) throws RepositoryException, SQLException {
        return repository.findOne(id);
    }

    @Override
    public Iterable<T> findAll() throws SQLException {
        return repository.findAll();
    }
}
