package com.example.socialnetworkgui.service;

import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.validators.ValidationException;

import java.sql.SQLException;

public interface Service<ID, T> {
    ID save(T entity) throws RepositoryException, SQLException, ValidationException;
    T delete(ID id) throws RepositoryException, SQLException;
    T findOne(ID id) throws RepositoryException, SQLException;
    Iterable<T> findAll() throws SQLException;
}
