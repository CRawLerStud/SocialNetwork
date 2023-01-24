package com.example.socialnetworkgui.validators.chat;

import com.example.socialnetworkgui.models.chat.Message;
import com.example.socialnetworkgui.validators.ValidationException;
import com.example.socialnetworkgui.validators.Validator;

public class MessageValidator implements Validator<Message> {

    @Override
    public void validate(Message entity) throws ValidationException {
        if(entity.getSender() == null)
            throw new ValidationException("Invalid user!");
        if(entity.getMessage().length() <= 0)
            throw new ValidationException("Message is too short!");
    }
}
