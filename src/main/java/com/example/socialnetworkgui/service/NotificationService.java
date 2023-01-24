package com.example.socialnetworkgui.service;

import com.example.socialnetworkgui.models.Notification;
import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.models.chat.IndividualChat;
import com.example.socialnetworkgui.models.chat.Message;
import com.example.socialnetworkgui.models.friendship.FriendshipRequest;
import com.example.socialnetworkgui.repository.NotificationRepositoryDB;
import com.example.socialnetworkgui.repository.RepositoryException;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class NotificationService{

    private final NotificationRepositoryDB repositoryDB;

    public NotificationService(NotificationRepositoryDB repositoryDB) {
        this.repositoryDB = repositoryDB;
    }

    public Long save(Notification notification) throws RepositoryException, SQLException{
        return repositoryDB.save(notification);
    }

    public Notification delete(Long notificationID) throws RepositoryException, SQLException{
        return repositoryDB.delete(notificationID);
    }

    public Notification findOne(Long notificationID) throws RepositoryException, SQLException{
        return repositoryDB.findOne(notificationID);
    }

    public Iterable<Notification> findAll() throws SQLException{
        return repositoryDB.findAll();
    }

    public void friendshipRequestNotification(FriendshipRequest friendshipRequest) throws SQLException, RepositoryException{
        User sender = friendshipRequest.getSender();
        User receiver;
        if(friendshipRequest.getUser1().equals(sender)){
            receiver = friendshipRequest.getUser2();
        }
        else{
            receiver = friendshipRequest.getUser1();
        }
        String text = sender.getSurname() + " " + sender.getLastname() + " sent you a friendship request!";
        Notification newNotification = new Notification(text, receiver);
        repositoryDB.save(newNotification);
    }

    public void responseFriendshipRequestNotification(FriendshipRequest friendshipRequest) throws SQLException, RepositoryException{
        User sender = friendshipRequest.getSender();
        User receiver;
        if(friendshipRequest.getUser1().equals(sender)){
            receiver = friendshipRequest.getUser2();
        }
        else{
            receiver = friendshipRequest.getUser1();
        }
        String status = friendshipRequest.getStatus();
        String text = receiver.getSurname() + " " + receiver.getLastname() + " ";
        if(status.equals("Accepted")){
            text += "accepted ";
        }
        else if(status.equals("Refused")){
            text += "refused ";
        }
        text += "your friendship!";

        Notification newNotification = new Notification(text, sender);
        repositoryDB.save(newNotification);
    }

    public void messageNotification(Message message) throws SQLException, RepositoryException{
        User sender = message.getSender();
        IndividualChat chat = message.getChat();
        User receiver;

        if(chat.getUser1().equals(sender)){
            receiver = chat.getUser2();
        }
        else{
            receiver = chat.getUser1();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        String notificationText = sender.getSurname() + " " + sender.getLastname() +
                " sent you a message: \"" +
                message.getMessage() + "\" at " +
                message.getSentTime().format(formatter);
        Notification newNotification = new Notification(notificationText, receiver);
        repositoryDB.save(newNotification);
    }

    public Iterable<Notification> notificationForUser(User user) throws SQLException{
        return repositoryDB.notificationForUser(user);
    }
}
