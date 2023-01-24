package com.example.socialnetworkgui.service.friendship;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.models.friendship.Friendship;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.repository.friendship.FriendshipRepositoryDB;
import com.example.socialnetworkgui.service.ServiceImplementation;
import com.example.socialnetworkgui.validators.Validator;
import javafx.util.Pair;

import java.sql.SQLException;

public class FriendshipService extends ServiceImplementation<Pair<Long, Long>, Friendship> {

    FriendshipRepositoryDB repositoryDB;

    public FriendshipService(Validator<Friendship> validator, FriendshipRepositoryDB repositoryDB) {
        super(validator, repositoryDB);
        this.repositoryDB = repositoryDB;
    }

    public void removeUserFriendships(User user) throws SQLException, RepositoryException{
        repositoryDB.removeUserFriendships(user);
    }
}
