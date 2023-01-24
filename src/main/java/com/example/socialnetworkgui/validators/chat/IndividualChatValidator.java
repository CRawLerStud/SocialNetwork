package com.example.socialnetworkgui.validators.chat;

import com.example.socialnetworkgui.models.chat.IndividualChat;
import com.example.socialnetworkgui.validators.ValidationException;
import com.example.socialnetworkgui.validators.Validator;

public class IndividualChatValidator implements Validator<IndividualChat> {

    @Override
    public void validate(IndividualChat entity) throws ValidationException {
        if(entity.getUser1() == null || entity.getUser2() == null ||
                (entity.getUser2().equals(entity.getUser1())))
            throw new ValidationException("Invalid individual chat!");
    }
}
