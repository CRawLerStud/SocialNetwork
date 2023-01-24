package com.example.socialnetworkgui.repository;

import java.sql.SQLException;

public interface Repository<ID, T> {

    ID save(T entity) throws RepositoryException, SQLException;
    T delete(ID id) throws RepositoryException, SQLException;
    T findOne(ID id) throws RepositoryException, SQLException;
    Iterable<T> findAll() throws SQLException;

}
