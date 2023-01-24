package com.example.socialnetworkgui.service.chat;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.models.chat.IndividualChat;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.repository.chat.IndividualChatsRepositoryDB;
import com.example.socialnetworkgui.service.ServiceImplementation;
import com.example.socialnetworkgui.validators.Validator;

import java.sql.SQLException;

public class IndividualChatService extends ServiceImplementation<String, IndividualChat> {

    private final IndividualChatsRepositoryDB repository;

    public IndividualChatService(Validator<IndividualChat> validator, IndividualChatsRepositoryDB repository) {
        super(validator, repository);
        this.repository = repository;
    }

    public Iterable<IndividualChat> findAllChatsForUser(User user) throws SQLException, RepositoryException{
        return repository.findAllChatsForUser(user);
    }
}
