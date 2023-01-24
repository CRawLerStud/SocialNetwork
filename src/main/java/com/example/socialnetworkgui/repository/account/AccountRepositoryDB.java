package com.example.socialnetworkgui.repository.account;

import com.example.socialnetworkgui.models.account.Account;
import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.repository.Repository;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.utils.RowsCalculator;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class AccountRepositoryDB implements Repository<String, Account> {

    private final String url;
    private final String username;
    private final String password;

    public AccountRepositoryDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public String save(Account entity) throws RepositoryException, SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);

        String findQuery = "SELECT * FROM accounts WHERE email = ?";
        PreparedStatement findStatement = connection.prepareStatement(findQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

        findStatement.setString(1, entity.getEmail());

        ResultSet findResult = findStatement.executeQuery();
        int noRows = RowsCalculator.getNoRows(findResult);
        if(noRows == 0) {

            String query = "INSERT INTO accounts(email, pass, \"userID\") VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getPassword());
            statement.setLong(3, entity.getUser().getId());

            statement.executeUpdate();

            connection.close();
            return entity.getEmail();
        }

        connection.close();
        throw new RepositoryException("Account exists!");
    }

    @Override
    public Account delete(String email) throws RepositoryException, SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);

        Account deletedAccount = findOne(email);

        String query = "DELETE FROM accounts WHERE email = ?";
        PreparedStatement statement = connection.prepareStatement(query);

        statement.setString(1, email);

        statement.executeUpdate();

        connection.close();

        return deletedAccount;
    }

    @Override
    public Account findOne(String email) throws RepositoryException, SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT pass, \"userID\" FROM accounts WHERE email = ?";
        PreparedStatement statement = connection.prepareStatement(query);

        statement.setString(1, email);

        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            String password = resultSet.getString("pass");
            Long userID = resultSet.getLong("userID");

            User user = getUser(userID);

            connection.close();
            return new Account(email, password, user);

        }

        connection.close();
        throw new RepositoryException("Account does not exist!");
    }

    @Override
    public Iterable<Account> findAll() throws SQLException {
        Set<Account> rez = new HashSet<>();

        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT email, pass, \"userID\" FROM accounts WHERE email = ?";
        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()){

            String email = resultSet.getString("email");
            String password = resultSet.getString("pass");
            Long userID = resultSet.getLong("userID");

            User user = getUser(userID);

            Account account = new Account(email, password, user);

            rez.add(account);

        }

        connection.close();
        return rez;
    }

    public void changeAccountPassword(String email, String newPassword)throws RepositoryException{
        try{
            Connection connection = DriverManager.getConnection(url, username, password);

            String query = "UPDATE accounts " +
                    "SET pass = ? " +
                    "WHERE email = ?";

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, newPassword);
            statement.setString(2, email);

            if(statement.executeUpdate() == 0) {
                connection.close();
                throw new RepositoryException("Entity is not existent!");
            }
            connection.close();
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private User getUser(Long userID) throws SQLException{

        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT lastname, surname, birthdate " +
                "FROM users " +
                "WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, userID);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        String lastname = resultSet.getString("lastname");
        String surname = resultSet.getString("surname");
        LocalDate birthdate = resultSet.getDate("birthdate").toLocalDate();

        User user = new User(lastname, surname, birthdate);
        user.setId(userID);

        connection.close();

        return user;
    }
}
