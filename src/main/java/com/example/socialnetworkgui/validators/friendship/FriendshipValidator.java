package com.example.socialnetworkgui.validators.friendship;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.models.friendship.Friendship;
import com.example.socialnetworkgui.validators.ValidationException;
import com.example.socialnetworkgui.validators.Validator;

public class FriendshipValidator implements Validator<Friendship> {

    @Override
    public void validate(Friendship entity) throws ValidationException {
        User user1 = entity.getUser1();
        User user2 = entity.getUser2();
        if(user1.equals(user2) || user2 == null)
            throw new ValidationException("Invalid Friendship!");
    }
}
