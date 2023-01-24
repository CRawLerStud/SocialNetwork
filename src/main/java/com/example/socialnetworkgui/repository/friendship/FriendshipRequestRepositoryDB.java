package com.example.socialnetworkgui.repository.friendship;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.models.friendship.FriendshipRequest;
import com.example.socialnetworkgui.repository.Repository;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.utils.RowsCalculator;
import javafx.util.Pair;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class FriendshipRequestRepositoryDB implements Repository<Pair<Long, Long>, FriendshipRequest> {

    String url;
    String username;
    String password;

    public FriendshipRequestRepositoryDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Pair<Long, Long> save(FriendshipRequest entity) throws RepositoryException, SQLException {

        Connection connection = DriverManager.getConnection(url, username, password);

        String findQuery = "SELECT * FROM \"friendshipRequests\" WHERE \"user1ID\" = ? AND \"user2ID\" = ?";
        PreparedStatement findStatement = connection.prepareStatement(findQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

        findStatement.setLong(1, entity.getId().getKey());
        findStatement.setLong(2, entity.getId().getValue());

        ResultSet findResult = findStatement.executeQuery();
        int noRows = RowsCalculator.getNoRows(findResult);
        System.out.println(noRows + " AM CALCULAT! " + entity.getUser1().getId() + " " + entity.getUser2().getId());
        if(noRows == 0) {

            String query =
                    "INSERT INTO \"friendshipRequests\"(\"user1ID\", \"user2ID\", status, sender, \"sentTime\")" +
                            " VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            long user1ID = Math.min(entity.getUser1().getId(), entity.getUser2().getId());
            long user2ID = Math.max(entity.getUser1().getId(), entity.getUser2().getId());
            long senderID = entity.getSender().getId();

            statement.setLong(1, user1ID);
            statement.setLong(2, user2ID);
            statement.setString(3, entity.getStatus());
            statement.setLong(4, senderID);
            statement.setTimestamp(5, Timestamp.valueOf(entity.getSentTime()));

            statement.executeUpdate();

            connection.close();
            return new Pair<>(user1ID, user2ID);
        }
        connection.close();
        throw new RepositoryException("Friendship Request already exists!");

    }

    @Override
    public FriendshipRequest delete(Pair<Long, Long> pairID) throws RepositoryException, SQLException {
       Connection connection = DriverManager.getConnection(url, username, password);

       FriendshipRequest deletedRequest = findOne(pairID);

       String query = "DELETE FROM \"friendshipRequests\" " +
               "WHERE \"user1ID\" = ? AND \"user2ID\" = ?";
       PreparedStatement statement = connection.prepareStatement(query);

        long user1ID = Math.min(pairID.getKey(), pairID.getValue());
        long user2ID = Math.max(pairID.getKey(), pairID.getValue());

       statement.setLong(1, user1ID);
       statement.setLong(2, user2ID);

       statement.executeUpdate();

       connection.close();

       return deletedRequest;
    }

    @Override
    public FriendshipRequest findOne(Pair<Long, Long> pairID) throws RepositoryException, SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT status, sender, \"sentTime\" " +
                "FROM \"friendshipRequests\" " +
                "WHERE \"user1ID\" = ? AND \"user2ID\" = ?";

        PreparedStatement statement = connection.prepareStatement(query);

        long user1ID = Math.min(pairID.getKey(), pairID.getValue());
        long user2ID = Math.max(pairID.getKey(), pairID.getValue());

        statement.setLong(1, user1ID);
        statement.setLong(2, user2ID);

        ResultSet resultSet = statement.executeQuery();

        if(resultSet.next()){
            String status = resultSet.getString("status");
            Long senderID = resultSet.getLong("sender");
            LocalDateTime sentTime = resultSet.getTimestamp("sentTime").toLocalDateTime();

            User user1 = getUser(user1ID);
            User user2 = getUser(user2ID);
            User sender = getUser(senderID);

            connection.close();
            return new FriendshipRequest(user1, user2, status, sender, sentTime);
        }
        connection.close();
        throw new RepositoryException("Friendship Request does not exist!");
    }

    @Override
    public Iterable<FriendshipRequest> findAll() throws SQLException {
        Set<FriendshipRequest> rez = new HashSet<>();

        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT * " +
                "FROM \"friendshipRequests\"";
        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()){
            Long user1ID = resultSet.getLong("user1ID");
            Long user2ID = resultSet.getLong("user2ID");
            String status = resultSet.getString("status");
            Long senderID = resultSet.getLong("sender");
            LocalDateTime sentTime = resultSet.getTimestamp("sentTime").toLocalDateTime();

            User user1 = getUser(user1ID);
            User user2 = getUser(user2ID);
            User sender = getUser(senderID);

            FriendshipRequest friendshipRequest = new FriendshipRequest(user1, user2, status, sender, sentTime);

            rez.add(friendshipRequest);
        }

        connection.close();
        return rez;
    }

    public FriendshipRequest acceptFriendshipRequest(FriendshipRequest friendshipRequest) throws SQLException, RepositoryException{
        Connection connection = DriverManager.getConnection(url, username, password);

        if(findOne(friendshipRequest.getId()) != null){
            friendshipRequest.setStatus("Accepted");
            Long user1 = friendshipRequest.getId().getKey();
            Long user2 = friendshipRequest.getId().getValue();
            String query = "UPDATE \"friendshipRequests\" SET status = ? WHERE \"user1ID\" = ? AND \"user2ID\" = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, friendshipRequest.getStatus());
            statement.setLong(2, user1);
            statement.setLong(3, user2);

            statement.executeUpdate();

            connection.close();
            return friendshipRequest;
        }
        throw new RepositoryException("Friendship Request does not exist!");
    }

    public FriendshipRequest refuseFriendshipRequest(FriendshipRequest friendshipRequest) throws SQLException, RepositoryException{
        Connection connection = DriverManager.getConnection(url, username, password);

        if(findOne(friendshipRequest.getId()) != null){
            friendshipRequest.setStatus("Refused");
            Long user1 = friendshipRequest.getId().getKey();
            Long user2 = friendshipRequest.getId().getValue();
            String query = "UPDATE \"friendshipRequests\" SET status = ? WHERE \"user1ID\" = ? AND \"user2ID\" = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, friendshipRequest.getStatus());
            statement.setLong(2, user1);
            statement.setLong(3, user2);

            statement.executeUpdate();
            connection.close();
            return friendshipRequest;
        }
        connection.close();
        throw new RepositoryException("Friendship Request does not exist!");
    }

    public void removeAllFriendshipRequestsForUser(User user) throws SQLException, RepositoryException{
        List<FriendshipRequest> friendshipRequests = new ArrayList<>();
        for(FriendshipRequest request : findAll()){
            if(request.getUser1().equals(user) || request.getUser2().equals(user)) {
                friendshipRequests.add(request);
            }
        }

        for(FriendshipRequest request: friendshipRequests){
            this.delete(request.getId());
        }
    }

    public Iterable<FriendshipRequest> friendshipRequestsForUser(User user) throws SQLException{
        Set<FriendshipRequest> friendshipRequestsForUser = new HashSet<>();
        Map<Long, User> mapIdUser = new HashMap<>();

        Long userID = user.getId();

        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "SELECT * FROM \"friendshipRequests\" " +
                "WHERE sender != ? and (\"user1ID\" = ? or \"user2ID\" = ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, userID);
        statement.setLong(2, userID);
        statement.setLong(3, userID);

        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            Long user1ID = resultSet.getLong("user1ID");
            User user1;
            if(mapIdUser.containsKey(user1ID))
                user1 = mapIdUser.get(user1ID);
            else{
                user1 = getUser(user1ID);
                mapIdUser.put(user1ID, user1);
            }

            Long user2ID = resultSet.getLong("user2ID");
            User user2;

            if(mapIdUser.containsKey(user2ID))
                user2 = mapIdUser.get(user2ID);
            else{
                user2 = getUser(user2ID);
                mapIdUser.put(user2ID, user2);
            }

            String status = resultSet.getString("status");
            Long sender = resultSet.getLong("sender");
            User senderUser;
            if(user1ID.equals(sender))
                senderUser = user1;
            else{
                senderUser = user2;
            }

            LocalDateTime sentTime = resultSet.getTimestamp("sentTime").toLocalDateTime();

            friendshipRequestsForUser.add(new FriendshipRequest(user1, user2, status, senderUser, sentTime));
        }

        return friendshipRequestsForUser;
    }

    public Iterable<FriendshipRequest> pendingFriendshipRequestsForUser(User user) throws SQLException{
        Set<FriendshipRequest> friendshipRequestsForUser = new HashSet<>();
        Map<Long, User> mapIdUser = new HashMap<>();

        Long userID = user.getId();

        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "SELECT * FROM \"friendshipRequests\" " +
                "WHERE sender != ? and (\"user1ID\" = ? or \"user2ID\" = ?) and status = 'Pending'";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, userID);
        statement.setLong(2, userID);
        statement.setLong(3, userID);

        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            Long user1ID = resultSet.getLong("user1ID");
            User user1;
            if(mapIdUser.containsKey(user1ID))
                user1 = mapIdUser.get(user1ID);
            else{
                user1 = getUser(user1ID);
                mapIdUser.put(user1ID, user1);
            }

            Long user2ID = resultSet.getLong("user2ID");
            User user2;

            if(mapIdUser.containsKey(user2ID))
                user2 = mapIdUser.get(user2ID);
            else{
                user2 = getUser(user2ID);
                mapIdUser.put(user2ID, user2);
            }

            String status = resultSet.getString("status");
            Long sender = resultSet.getLong("sender");
            User senderUser;
            if(user1ID.equals(sender))
                senderUser = user1;
            else{
                senderUser = user2;
            }

            LocalDateTime sentTime = resultSet.getTimestamp("sentTime").toLocalDateTime();

            friendshipRequestsForUser.add(new FriendshipRequest(user1, user2, status, senderUser, sentTime));
        }

        return friendshipRequestsForUser;
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
