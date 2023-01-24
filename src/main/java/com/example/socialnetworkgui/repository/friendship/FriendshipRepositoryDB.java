package com.example.socialnetworkgui.repository.friendship;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.models.friendship.Friendship;
import com.example.socialnetworkgui.repository.Repository;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.utils.RowsCalculator;
import javafx.util.Pair;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendshipRepositoryDB implements Repository<Pair<Long, Long>, Friendship> {

    private final String url;
    private final String username;
    private final String password;

    public FriendshipRepositoryDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Pair<Long, Long> save(Friendship entity) throws RepositoryException, SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);

        String findQuery = "SELECT * FROM friendships WHERE \"user1ID\" = ? AND \"user2ID\" = ?";
        PreparedStatement findStatement = connection.prepareStatement(findQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

        findStatement.setLong(1, entity.getId().getKey());
        findStatement.setLong(2, entity.getId().getValue());

        ResultSet findResult = findStatement.executeQuery();
        int noRows = RowsCalculator.getNoRows(findResult);
        if(noRows == 0) {

            String query = "INSERT INTO friendships(\"user1ID\", \"user2ID\", \"friendsFrom\") VALUES (?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);

            long user1ID = Math.min(entity.getUser1().getId(), entity.getUser2().getId());
            long user2ID = Math.max(entity.getUser1().getId(), entity.getUser2().getId());

            statement.setLong(1, user1ID);
            statement.setLong(2, user2ID);
            statement.setTimestamp(3, Timestamp.valueOf(entity.getFriendsFrom()));

            statement.executeUpdate();

            connection.close();

            return new Pair<>(user1ID, user2ID);
        }

        connection.close();
        throw new RepositoryException("Friendship exists!");
    }

    @Override
    public Friendship delete(Pair<Long, Long> pairID) throws RepositoryException, SQLException {

        Connection connection = DriverManager.getConnection(url, username, password);

        Friendship deletedFriendship = findOne(pairID);

        long user1ID = Math.min(pairID.getKey(), pairID.getValue());
        long user2ID = Math.max(pairID.getKey(), pairID.getValue());

        String query = "DELETE FROM friendships WHERE \"user1ID\" = ? AND \"user2ID\" = ?";
        PreparedStatement statement = connection.prepareStatement(query);

        statement.setLong(1, user1ID);
        statement.setLong(2, user2ID);

        statement.executeUpdate();

        connection.close();

        return deletedFriendship;
    }

    @Override
    public Friendship findOne(Pair<Long, Long> pairID) throws RepositoryException, SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT * FROM friendships WHERE \"user1ID\" = ? AND \"user2ID\" = ?";
        PreparedStatement statement = connection.prepareStatement(query);

        long user1ID = Math.min(pairID.getKey(), pairID.getValue());
        long user2ID = Math.max(pairID.getKey(), pairID.getValue());

        statement.setLong(1, user1ID);
        statement.setLong(2, user2ID);

        ResultSet result = statement.executeQuery();


        if(result.next()){
            LocalDateTime friendsFrom = result.getTimestamp("friendsFrom").toLocalDateTime();

            connection.close();
            return getFriendshipFromDB(user1ID, user2ID, friendsFrom);
        }

        connection.close();
        throw new RepositoryException("Friendship does not exist!");
    }

    @Override
    public Iterable<Friendship> findAll() throws SQLException{
        Set<Friendship> result = new HashSet<>();

        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT * FROM friendships";
        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()){
            long user1ID = resultSet.getLong("user1ID");
            long user2ID = resultSet.getLong("user2ID");
            LocalDateTime friendsFrom = resultSet.getTimestamp("friendsFrom").toLocalDateTime();

            Friendship newFriendship = getFriendshipFromDB(user1ID, user2ID, friendsFrom);
            result.add(newFriendship);
        }

        connection.close();
        return result;
    }

    public void removeUserFriendships(User user) throws RepositoryException, SQLException{
        List<Friendship> friendships = new ArrayList<>();
        for(Friendship friendship : findAll()){
            if(friendship.getUser1().equals(user) || friendship.getUser2().equals(user)) {
                friendships.add(friendship);
            }
        }

        for(Friendship friendship: friendships){
            this.delete(friendship.getId());
        }
    }

    private Friendship getFriendshipFromDB(Long user1ID, Long user2ID, LocalDateTime friendsFrom) throws SQLException{
        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT lastname, surname, birthdate FROM users WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);

        statement.setLong(1, user1ID);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        String lastname = resultSet.getString(1);
        String surname = resultSet.getString(2);
        LocalDate birthdate = resultSet.getDate(3).toLocalDate();

        User user1 = new User(lastname, surname, birthdate);
        user1.setId(user1ID);

        statement.setLong(1, user2ID);

        resultSet = statement.executeQuery();
        resultSet.next();

        lastname = resultSet.getString(1);
        surname = resultSet.getString(2);
        birthdate = resultSet.getDate(3).toLocalDate();

        User user2 = new User(lastname, surname, birthdate);
        user2.setId(user2ID);

        connection.close();

        return new Friendship(user1, user2, friendsFrom);
    }

}