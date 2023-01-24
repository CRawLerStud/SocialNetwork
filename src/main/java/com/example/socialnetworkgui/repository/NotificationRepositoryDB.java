package com.example.socialnetworkgui.repository;

import com.example.socialnetworkgui.models.Notification;
import com.example.socialnetworkgui.models.account.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class NotificationRepositoryDB implements Repository<Long, Notification> {

    private final String url;
    private final String username;
    private final String password;

    public NotificationRepositoryDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Long save(Notification entity) throws RepositoryException, SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "INSERT INTO notifications (\"text\", user_id) values (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, entity.getText());
        statement.setLong(2, entity.getUser().getId());

        statement.executeUpdate();

        ResultSet key = statement.getGeneratedKeys();
        Long notificationID = null;
        if(key.next())
            notificationID = key.getLong(1);

        connection.close();
        return notificationID;
    }

    @Override
    public Notification delete(Long aLong) throws RepositoryException, SQLException {
        Notification deletedNotification = findOne(aLong);

        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "DELETE FROM notifications WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, aLong);

        statement.executeUpdate();

        connection.close();
        return deletedNotification;
    }

    @Override
    public Notification findOne(Long aLong) throws RepositoryException, SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "SELECT * FROM notifications WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, aLong);

        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            String text = resultSet.getString("text");
            long userId = resultSet.getLong("user_id");

            String userQuery = "SELECT * FROM users WHERE id = ?";
            PreparedStatement userStatement = connection.prepareStatement(userQuery);
            userStatement.setLong(1, userId);

            ResultSet userResultSet = userStatement.executeQuery();
            userResultSet.next();

            String userLastname = userResultSet.getString("lastname");
            String userSurname = userResultSet.getString("surname");
            LocalDate birthdate = userResultSet.getDate("birthdate").toLocalDate();

            connection.close();

            User notificationUser = new User(userId, userLastname, userSurname, birthdate);

            return new Notification(aLong, text, notificationUser);
        }

        connection.close();
        return null;
    }

    @Override
    public Iterable<Notification> findAll() throws SQLException {
        Set<Notification> notifications = new HashSet<>();

        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "SELECT * FROM notifications";
        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()){
            Long notificationID = resultSet.getLong("id");
            String text = resultSet.getString("text");
            long userID = resultSet.getLong("user_id");

            String userQuery = "SELECT * FROM users WHERE id = ?";
            PreparedStatement userStatement = connection.prepareStatement(userQuery);
            userStatement.setLong(1, userID);

            ResultSet userResultSet = userStatement.executeQuery();
            userResultSet.next();

            String userLastname = userResultSet.getString("lastname");
            String userSurname = userResultSet.getString("surname");
            LocalDate birthdate = userResultSet.getDate("birthdate").toLocalDate();

            User notificationUser = new User(userID, userLastname, userSurname, birthdate);

            notifications.add(new Notification(notificationID, text, notificationUser));
        }
        connection.close();
        return notifications;
    }

    public Iterable<Notification> notificationForUser(User user) throws SQLException{

        Set<Notification> notificationsForUser = new HashSet<>();

        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT id, \"text\" FROM notifications WHERE user_id = ? ORDER BY id DESC";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, user.getId());

        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            String text = resultSet.getString("text");
            Long ID = resultSet.getLong("id");

            notificationsForUser.add(new Notification(ID, text, user));
        }

        return notificationsForUser;
    }
}
