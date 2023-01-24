package com.example.socialnetworkgui.repository.chat;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.models.chat.IndividualChat;
import com.example.socialnetworkgui.models.chat.Message;
import com.example.socialnetworkgui.repository.Repository;
import com.example.socialnetworkgui.repository.RepositoryException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MessageRepositoryDB implements Repository<Long, Message> {

    private final String url;
    private final String username;
    private final String password;

    public MessageRepositoryDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Long save(Message entity) throws RepositoryException, SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "INSERT INTO messages(sender, chat_id, \"message\", sent_time) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, entity.getSender().getId());
        statement.setString(2, entity.getChat().getId());
        statement.setString(3, entity.getMessage());
        statement.setTimestamp(4, Timestamp.valueOf(entity.getSentTime()));

        statement.executeUpdate();
        Long ID = null;

        ResultSet generatedKeys = statement.getGeneratedKeys();
        if(generatedKeys.next())
            ID = generatedKeys.getLong(1);
        connection.close();
        return ID;
    }

    @Override
    public Message delete(Long aLong) throws RepositoryException, SQLException {
        Message deletedMessage = findOne(aLong);

        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "DELETE FROM messages WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, aLong);

        statement.executeUpdate();

        connection.close();
        return deletedMessage;
    }

    @Override
    public Message findOne(Long ID) throws RepositoryException, SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT * FROM messages WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);

        statement.setLong(1, ID);

        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            Long senderID = resultSet.getLong("sender");
            String conversationID = resultSet.getString("chat_id");
            String message = resultSet.getString("message");
            LocalDateTime sent_time = resultSet.getTimestamp("sent_time").toLocalDateTime();

            User sender= getUser(senderID);
            IndividualChat chat = getIndividualChat(conversationID);


            resultSet.close();
            connection.close();

            return new Message(ID, sender, chat, message, sent_time);
        }

        resultSet.close();
        connection.close();
        throw new RepositoryException("Message does not exist!");
    }

    @Override
    public Iterable<Message> findAll() throws SQLException {
        Set<Message> messageSet = new HashSet<>();
        Map<Long, User> userMap = new HashMap<>();
        Map<String, IndividualChat> chatMap = new HashMap<>();

        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT * FROM messages";
        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){

            Long ID = resultSet.getLong("id");

            User sender;

            Long senderID = resultSet.getLong("sender");
            if(userMap.containsKey(senderID)){
                sender = userMap.get(senderID);
            }
            else{
                sender = getUser(senderID);
                userMap.put(senderID, sender);
            }

            IndividualChat chat;

            String chat_id = resultSet.getString("chat_id");
            if(chatMap.containsKey(chat_id)){
                chat = chatMap.get(chat_id);
            }
            else{
                chat = getIndividualChat(chat_id);
                chatMap.put(chat_id, chat);
            }

            String message = resultSet.getString("message");
            LocalDateTime sent_time = resultSet.getTimestamp("sent_time").toLocalDateTime();

            messageSet.add(new Message(ID, sender, chat, message, sent_time));
        }

        resultSet.close();
        connection.close();
        return messageSet;
    }

    public Iterable<Message> findAllMessagesForChat(IndividualChat chat) throws SQLException{
        Set<Message> messageSet = new HashSet<>();
        Map<Long, User> users = new HashMap<>();

        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT * FROM messages WHERE chat_id = ? ORDER BY sent_time";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, chat.getId());

        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            Long ID =  resultSet.getLong("id");
            Long senderID = resultSet.getLong("sender");
            String message = resultSet.getString("message");
            LocalDateTime sent_time = resultSet.getTimestamp("sent_time").toLocalDateTime();

            User sender;

            if(users.containsKey(senderID)){
                sender = users.get(senderID);
            }
            else{
                sender = getUser(senderID);
                users.put(senderID, sender);
            }

            messageSet.add(new Message(ID, sender, chat, message, sent_time));
        }

        resultSet.close();
        connection.close();
        return messageSet;
    }

    public Message getLastMessageForChat(IndividualChat chat) throws SQLException, RepositoryException{
        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT * FROM messages WHERE chat_id = ? ORDER BY sent_time DESC LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, chat.getId());

        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            Long ID  = resultSet.getLong("id");
            Long senderID = resultSet.getLong("sender");
            String message = resultSet.getString("message");
            LocalDateTime sent_time = resultSet.getTimestamp("sent_time").toLocalDateTime();

            User sender = getUser(senderID);

            resultSet.close();
            connection.close();
            return new Message(ID, sender, chat, message, sent_time);
        }

        resultSet.close();
        connection.close();
        return null;
    }

    private IndividualChat getIndividualChat(String conversationID) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);

        String query = "SELECT * FROM individual_chats WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, conversationID);

        ResultSet result = statement.executeQuery();
        if(result.next()){
            Long user1ID = result.getLong("user1");
            Long user2ID = result.getLong("user2");

            User user1 = getUser(user1ID);
            User user2 = getUser(user2ID);

            result.close();
            connection.close();
            return new IndividualChat(conversationID, user1, user2);
        }

        result.close();
        connection.close();
        return null;
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
