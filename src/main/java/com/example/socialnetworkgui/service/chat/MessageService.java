package com.example.socialnetworkgui.service.chat;

import com.example.socialnetworkgui.models.chat.IndividualChat;
import com.example.socialnetworkgui.models.chat.Message;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.repository.chat.MessageRepositoryDB;
import com.example.socialnetworkgui.service.ServiceImplementation;
import com.example.socialnetworkgui.validators.Validator;

import java.sql.SQLException;

public class MessageService extends ServiceImplementation<Long, Message> {

    private final MessageRepositoryDB repository;

    public MessageService(Validator<Message> validator, MessageRepositoryDB repository) {
        super(validator, repository);
        this.repository = repository;
    }

    public Iterable<Message> getMessagesForConversation(IndividualChat chat) throws SQLException{
        return repository.findAllMessagesForChat(chat);
    }

    public Message getLastMessageForChat(IndividualChat chat) throws SQLException, RepositoryException{
        return repository.getLastMessageForChat(chat);
    }
}
