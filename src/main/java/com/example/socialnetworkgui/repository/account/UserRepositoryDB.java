package com.example.socialnetworkgui.repository.account;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.repository.Repository;
import com.example.socialnetworkgui.repository.RepositoryException;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class UserRepositoryDB implements Repository<Long, User> {

    private final String url;
    private final String username;
    private final String password;

    public UserRepositoryDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Long save(User entity) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);

        String lastname = entity.getLastname();
        String surname = entity.getSurname();
        String birthdate = entity.getBirthDate().toString();

        String query = "INSERT INTO users(lastname, surname, birthdate) VALUES (?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, lastname);
        statement.setString(2, surname);
        statement.setDate(3, Date.valueOf(birthdate));

        statement.executeUpdate();

        ResultSet key = statement.getGeneratedKeys();
        Long userID = null;
        if(key.next())
            userID = key.getLong(1);

        connection.close();
        return userID;

    }

    @Override
    public User delete(Long aLong) throws RepositoryException, SQLException {
        User deletedUser;

        Connection connection = DriverManager.getConnection(url, username, password);

        deletedUser = findOne(aLong);
        if(deletedUser == null)
            throw new RepositoryException("Entity is not existent!");

        String query = "DELETE FROM users WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, aLong);

        statement.executeUpdate();

        connection.close();

        return deletedUser;
    }

    @Override
    public User findOne(Long aLong) throws RepositoryException, SQLException {

        User foundUser;

        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT * FROM users WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, aLong);

        ResultSet result = statement.executeQuery();

        if(result.next()) {
            Long ID = result.getLong("id");
            String lastname = result.getString("lastname");
            String surname = result.getString("surname");
            LocalDate birthdate = result.getDate("birthdate").toLocalDate();
            foundUser = new User(lastname, surname, birthdate);
            foundUser.setId(ID);
        }
        else{
            connection.close();
            throw new RepositoryException("Entity is not existent!");
        }
        connection.close();
        return foundUser;
    }

    @Override
    public Iterable<User> findAll() {
        return obtainSetAfterQuery(
                "SELECT * FROM users"
        );
    }

    /**
     * Returns a list with all the users whose lastname is lastname(parameter)
     * @param lastname the looked up lastname
     * @return a list with all the users whose lastname is lastname
     */
    public Set<User> usersWithLastname(String lastname){
        return obtainSetAfterQuery(
                "SELECT * FROM users " +
                        "WHERE lastname='" + lastname + "'"
        );
    }

    /**
     * Returns a list with all the users whose surname is surname(parameter)
     * @param surname the looked up surname
     * @return a list with all the users whose surname is surname(parameter)
     */
    public Set<User> usersWithSurname(String surname){
        return obtainSetAfterQuery(
                "SELECT * FROM users " +
                        "WHERE surname='" + surname + "'"
        );
    }


    /**
     * Returns a list with all the users that has fullname = lastname + surname
     * @param lastname the looked up lastname
     * @param surname the looked up surname
     * @return a list with all the users whose fullname is lastname + surname ( parameter )
     */
    public Set<User> usersWithFullname(String lastname, String surname){
        return obtainSetAfterQuery(
                "SELECT * FROM users" +
                        " WHERE lastname='" + lastname + "' AND surname='" + surname + "'"
        );
    }

    /**
     * Returns a list with all the users whose lastname starts with sequence(parameter)
     * @param sequence the looked up sequence
     * @return a list with all the users whose lastname starts with sequence
     */
    public Set<User> usersWhoseLastnameStartsWith(String sequence){
        return obtainSetAfterQuery(
                "SELECT * FROM users " +
                        "WHERE lastname LIKE'" + sequence + "%'"
        );
    }

    /**
     * Returns a list with all the users whose surname starts with sequence
     * @param sequence the looked up sequence
     * @return a list with all the users whose surname starts with sequence(parameter)
     */
    public Set<User> usersWhoseSurnameStartsWith(String sequence){
        return obtainSetAfterQuery(
                "SELECT * FROM users " +
                        "WHERE surname LIKE'" + sequence + "%'"
        );
    }

    /**
     * Returns a list with all the users older than minimumAge
     * @param minimumAge the minimumAge
     * @return a list with all the users older than minimumAge
     */
    public Set<User> usersOlderThan(int minimumAge){
        return obtainSetAfterQuery(
                "SELECT * FROM users " +
                        "WHERE DATE_PART('YEAR', NOW()::DATE) - DATE_PART('YEAR', birthdate::DATE) >= " + minimumAge
        );
    }

    /**
     * Change user's lastname
     * @param ID the looked up user's ID
     * @param newLastname the new lastname
     * @return the user with the modified lastname
     * @throws RepositoryException if there is no user with the id ID
     */
    public User changeUserLastname(Long ID, String newLastname) throws RepositoryException, SQLException{
        updateUserAfterQuery(
                "UPDATE users " +
                        "SET lastname = '" + newLastname +"'" +
                        "WHERE id = "+ ID.toString()
        );
        return findOne(ID);
    }

    /**
     * Change a user's surname
     * @param ID the looked up user's ID
     * @param newSurname the new surname
     * @return the user with the modified surname
     * @throws RepositoryException if there is no user with the id ID
     */
    public User changeUserSurname(Long ID, String newSurname) throws RepositoryException, SQLException{
        updateUserAfterQuery(
                "UPDATE users " +
                        "SET surname = '" + newSurname +"'" +
                        "WHERE id = "+ ID.toString()
        );
        return findOne(ID);
    }

    /**
     * Change a user's birthdate
     * @param ID the looked up user's ID
     * @param newBirthDate the new birthDate
     * @return the user with the modified birthdate
     * @throws RepositoryException if there's no user with the id ID
     */
    public User changeUserBirthdate(Long ID, LocalDate newBirthDate) throws RepositoryException, SQLException{
        updateUserAfterQuery(
                "UPDATE users " +
                        "SET birthdate = '" + newBirthDate.toString() +"'" +
                        "WHERE id = "+ ID.toString()
        );
        return findOne(ID);
    }

    public Set<User> getAllUserFriends(User user) throws SQLException{
        Set<User> friends = new HashSet<>();
        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT users.id, users.lastname, users.surname, users.birthdate FROM " +
                "(SELECT \"user1ID\" AS id FROM " +
                "(SELECT \"user1ID\" FROM friendships " +
                "WHERE \"user1ID\" = ? OR \"user2ID\" = ? " +
                "UNION " +
                "SELECT \"user2ID\" FROM friendships" +
                " WHERE \"user1ID\" = ? OR \"user2ID\" = ?)" +
                " AS F WHERE \"user1ID\" != ?) AS A" +
                " INNER JOIN users on users.id = A.id";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, user.getId());
        statement.setLong(2, user.getId());
        statement.setLong(3, user.getId());
        statement.setLong(4, user.getId());
        statement.setLong(5, user.getId());

        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            Long ID = resultSet.getLong("id");
            String surname = resultSet.getString("surname");
            String lastname = resultSet.getString("lastname");
            LocalDate birthdate = resultSet.getDate("birthdate").toLocalDate();

            friends.add(new User(ID, lastname, surname, birthdate));
        }

        connection.close();
        return friends;
    }
    private Set<User> createListOfResult(ResultSet resultSet){
        Set<User> result = new HashSet<>();

        try {
            while (resultSet.next()) {
                Long ID = resultSet.getLong("id");
                String lastname = resultSet.getString("lastname");
                String surname = resultSet.getString("surname");
                LocalDate birthdate = resultSet.getDate("birthdate").toLocalDate();

                User user = new User(lastname, surname, birthdate);
                user.setId(ID);

                result.add(user);
            }
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    private Set<User> obtainSetAfterQuery(String query){
        Set<User> result = new HashSet<>();
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            result = createListOfResult(resultSet);
            connection.close();
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    private void updateUserAfterQuery(String query) throws RepositoryException{
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(query);
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

}
