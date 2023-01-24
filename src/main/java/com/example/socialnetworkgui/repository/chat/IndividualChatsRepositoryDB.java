package com.example.socialnetworkgui.repository.chat;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.models.chat.IndividualChat;
import com.example.socialnetworkgui.repository.Repository;
import com.example.socialnetworkgui.repository.RepositoryException;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IndividualChatsRepositoryDB implements Repository<String, IndividualChat> {

    private final String url;
    private final String username;
    private final String password;

    public IndividualChatsRepositoryDB(String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public String save(IndividualChat entity) throws RepositoryException, SQLException {
        try{
            findOne(entity.getId());
            throw new RepositoryException("Chat exists!");
        }
        catch(RepositoryException e){
            if(e.getMessage().equals("Chat does not exist!")){
                Connection connection = DriverManager.getConnection(url, username, password);

                String query = "INSERT INTO individual_chats(id, user1, user2) VALUES (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);

                statement.setString(1, entity.getId());
                statement.setLong(2, entity.getUser1().getId());
                statement.setLong(3, entity.getUser2().getId());

                statement.executeUpdate();
                connection.close();

                return entity.getId();
            }
            else{
                throw new RepositoryException(e.getMessage());
            }
        }
    }

    @Override
    public IndividualChat delete(String s) throws RepositoryException, SQLException {
        IndividualChat deletedChat = findOne(s);

        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "DELETE FROM individual_chats WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, s);

        statement.executeUpdate();

        connection.close();
        return deletedChat;
    }

    @Override
    public IndividualChat findOne(String s) throws RepositoryException, SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT * FROM individual_chats WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, s);

        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){

            String ID =  resultSet.getString("id");
            Long user1ID = resultSet.getLong("user1");
            Long user2ID = resultSet.getLong("user2");

            User user1 = getUser(user1ID);
            User user2 = getUser(user2ID);

            resultSet.close();
            connection.close();
            return new IndividualChat(ID, user1, user2);
        }

        resultSet.close();
        connection.close();
        throw new RepositoryException("Chat does not exist!");
    }

    @Override
    public Iterable<IndividualChat> findAll() throws SQLException {
        Set<IndividualChat> chats = new HashSet<>();

        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT * FROM individual_chats";
        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            String ID =  resultSet.getString("id");
            Long user1ID = resultSet.getLong("user1");
            Long user2ID = resultSet.getLong("user2");

            User user1 = getUser(user1ID);
            User user2 = getUser(user2ID);

            chats.add(new IndividualChat(ID, user1, user2));
        }

        resultSet.close();
        connection.close();
        return chats;
    }

    public Iterable<IndividualChat> findAllChatsForUser(User user) throws SQLException, RepositoryException{
        Set<IndividualChat> chats = new HashSet<>();
        Map<Long, User> users = new HashMap<>();
        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT * FROM individual_chats WHERE user1 = ? OR user2 = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, user.getId());
        statement.setLong(2, user.getId());

        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            String ID = resultSet.getString("id");
            Long user1ID = resultSet.getLong("user1");
            Long user2ID = resultSet.getLong("user2");

            User user1;
            User user2;

            if(users.containsKey(user1ID)){
                user1 = users.get(user1ID);
            }
            else{
                user1 = getUser(user1ID);
                users.put(user1ID, user1);
            }

            if(users.containsKey(user2ID)){
                user2 = users.get(user2ID);
            }
            else{
                user2 = getUser(user2ID);
                users.put(user2ID, user2);
            }

            chats.add(new IndividualChat(ID, user1, user2));
        }

        resultSet.close();
        connection.close();
        return chats;
    }

    private User getUser(Long user1ID) throws SQLException{
        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT * FROM users WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, user1ID);

        ResultSet result = statement.executeQuery();
        if(result.next()){
            String surname = result.getString("surname");
            String lastname = result.getString("lastname");
            LocalDate birthdate = result.getDate("birthdate").toLocalDate();

            result.close();
            connection.close();
            return new User(user1ID, lastname, surname, birthdate);
        }

        result.close();
        connection.close();
        return null;
    }
}
