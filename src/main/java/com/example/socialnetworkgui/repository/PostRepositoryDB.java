package com.example.socialnetworkgui.repository;

import com.example.socialnetworkgui.models.Post;
import com.example.socialnetworkgui.models.account.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PostRepositoryDB implements Repository<Long, Post>{

    private final String url;
    private final String username;
    private final String password;


    public PostRepositoryDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Long save(Post entity) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);

        Long userID = entity.getUser().getId();
        String text = entity.getText();
        LocalDateTime postTime = entity.getPostTime();

        String query = "INSERT INTO posts(\"userID\", \"text\", \"postTime\") values (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        statement.setLong(1, userID);
        statement.setString(2, text);
        statement.setTimestamp(3, Timestamp.valueOf(postTime));

        statement.executeUpdate();

        ResultSet key = statement.getGeneratedKeys();
        Long postID = null;
        if(key.next())
            postID = key.getLong(1);

        connection.close();
        return postID;
    }

    @Override
    public Post delete(Long aLong) throws RepositoryException, SQLException {
        Post deletedPost = findOne(aLong);

        Connection connection = DriverManager.getConnection(url, username, password);

        if(deletedPost == null)
            throw new RepositoryException("Entity is not existent!");

        String query = "DELETE FROM posts WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, aLong);

        statement.executeUpdate();

        connection.close();

        return deletedPost;
    }

    @Override
    public Post findOne(Long aLong) throws RepositoryException, SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT * FROM posts WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, aLong);

        ResultSet result = statement.executeQuery();
        if(result.next()){
            long userID = result.getLong("userID");
            String text = result.getString("text");
            LocalDateTime postTime = result.getTimestamp("postTime").toLocalDateTime();

            String userQuery = "SELECT * FROM users WHERE id = ?";
            PreparedStatement userStatement = connection.prepareStatement(userQuery);
            userStatement.setLong(1, userID);

            ResultSet resultSet = userStatement.executeQuery();

            String lastname = resultSet.getString("lastname");
            String surname = resultSet.getString("surname");
            LocalDate birthdate = resultSet.getDate("birthdate").toLocalDate();

            User foundUser = new User(userID, lastname, surname, birthdate);

            connection.close();
            return new Post(foundUser, text, postTime);
        }

        connection.close();
        return null;
    }

    @Override
    public Iterable<Post> findAll() throws SQLException {
        Set<Post> postSet = new HashSet<>();

        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT * FROM posts";
        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            long userID = resultSet.getLong("userID");
            String text = resultSet.getString("text");
            LocalDateTime postTime = resultSet.getTimestamp("postTime").toLocalDateTime();

            String userQuery = "SELECT * FROM users WHERE id = ?";
            PreparedStatement userStatement = connection.prepareStatement(userQuery);
            userStatement.setLong(1, userID);

            ResultSet userResultSet = userStatement.executeQuery();

            String lastname = userResultSet.getString("lastname");
            String surname = userResultSet.getString("surname");
            LocalDate birthdate = userResultSet.getDate("birthdate").toLocalDate();

            User foundUser = new User(userID, lastname, surname, birthdate);

            postSet.add(new Post(foundUser, text, postTime));
        }

        return postSet;
    }
}
