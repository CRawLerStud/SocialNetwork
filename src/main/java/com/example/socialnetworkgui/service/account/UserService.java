package com.example.socialnetworkgui.service.account;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.repository.account.UserRepositoryDB;
import com.example.socialnetworkgui.service.ServiceImplementation;
import com.example.socialnetworkgui.validators.ValidationException;
import com.example.socialnetworkgui.validators.account.UserValidator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;

public class UserService extends ServiceImplementation<Long, User> {

    UserValidator validator;
    UserRepositoryDB repositoryDB;

    public UserService(UserValidator validator, UserRepositoryDB repositoryDB) {
        super(validator, repositoryDB);
        this.repositoryDB = repositoryDB;
        this.validator = validator;
    }

    public User changeUserBirthdate(Long ID, LocalDate newBirthdate) throws ValidationException, RepositoryException, SQLException{
        User modifiedUser = findOne(ID);
        User newUser = new User(modifiedUser.getId(), modifiedUser.getLastname(), modifiedUser.getSurname(), newBirthdate);
        validator.validate(newUser);
        return repositoryDB.changeUserBirthdate(ID, newBirthdate);
    }

    public User changeUserLastname(Long ID, String newLastname) throws ValidationException, RepositoryException, SQLException{
        User modifiedUser = findOne(ID);
        User newUser = new User(ID, newLastname, modifiedUser.getSurname(), modifiedUser.getBirthDate());
        validator.validate(newUser);
        return repositoryDB.changeUserLastname(ID, newLastname);
    }

    public User changeUserSurname(Long ID, String newSurname) throws ValidationException, RepositoryException, SQLException{
        User modifiedUser = findOne(ID);
        User newUser = new User(ID, modifiedUser.getLastname(), newSurname, modifiedUser.getBirthDate());
        validator.validate(newUser);
        return repositoryDB.changeUserSurname(ID, newSurname);
    }

    public Set<User> usersWithLastname(String lastname) {
        return repositoryDB.usersWithLastname(lastname);
    }

    public Set<User> usersWithSurname(String surname) {
        return repositoryDB.usersWithSurname(surname);
    }

    public Set<User> usersWithFullname(String lastname, String surname) {
        return repositoryDB.usersWithFullname(lastname, surname);
    }

    public Set<User> usersOlderThan(int minimumAge) {
        return repositoryDB.usersOlderThan(minimumAge);
    }

    public Set<User> usersWhoseLastnameStartsWith(String sequence) {
        return repositoryDB.usersWhoseLastnameStartsWith(sequence);
    }

    public Set<User> usersWhoseSurnameStartsWith(String sequence) {
        return repositoryDB.usersWhoseSurnameStartsWith(sequence);
    }

    public Set<User> getAllFriendsForUser(User user) throws SQLException{
        return repositoryDB.getAllUserFriends(user);
    }

}
