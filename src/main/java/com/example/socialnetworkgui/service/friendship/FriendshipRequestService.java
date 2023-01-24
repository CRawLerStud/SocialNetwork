package com.example.socialnetworkgui.service.friendship;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.models.friendship.FriendshipRequest;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.repository.friendship.FriendshipRequestRepositoryDB;
import com.example.socialnetworkgui.service.ServiceImplementation;
import com.example.socialnetworkgui.validators.Validator;
import javafx.util.Pair;

import java.sql.SQLException;

public class FriendshipRequestService extends ServiceImplementation<Pair<Long, Long>, FriendshipRequest> {

    FriendshipRequestRepositoryDB repository;

    public FriendshipRequestService(Validator<FriendshipRequest> validator, FriendshipRequestRepositoryDB repository) {
        super(validator, repository);
        this.repository = repository;
    }

    public FriendshipRequest acceptFriendshipRequest(FriendshipRequest friendshipRequest) throws SQLException, RepositoryException{
        return repository.acceptFriendshipRequest(friendshipRequest);
    }

    public FriendshipRequest refuseFriendshipRequest(FriendshipRequest friendshipRequest) throws SQLException, RepositoryException{
        return repository.refuseFriendshipRequest(friendshipRequest);
    }

    public void removeAllFriendshipRequestsForUser(User user) throws SQLException, RepositoryException{
        repository.removeAllFriendshipRequestsForUser(user);
    }

    public Iterable<FriendshipRequest> friendshipRequestsForUser(User user) throws SQLException{
        return repository.friendshipRequestsForUser(user);
    }

    public Iterable<FriendshipRequest> pendingFriendshipRequestsForUser(User user) throws SQLException{
        return repository.pendingFriendshipRequestsForUser(user);
    }

}
