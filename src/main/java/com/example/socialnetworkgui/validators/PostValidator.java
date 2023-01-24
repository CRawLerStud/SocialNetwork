package com.example.socialnetworkgui.validators;

import com.example.socialnetworkgui.models.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostValidator implements Validator<Post>{

    @Override
    public void validate(Post entity) throws ValidationException {
        if(entity.getUser() == null)
            throw new ValidationException("Invalid user!");

        String text = entity.getText();
        Pattern pattern = Pattern.compile(".{5,1000}");
        Matcher matcher = pattern.matcher(text);

        if (!(matcher.matches())) {
            if(text.length() < 5){
                throw new ValidationException("Text is too short!(at least 5)");
            }
            else{
                throw new ValidationException("Text is too long!(maximum 1000 chars)");
            }
        }

        checkBadWords(text);

    }

    private void checkBadWords(String text) throws ValidationException{
        List<String> badWords = new ArrayList<>();
        badWords.add("dick");
        badWords.add("pussy");
        badWords.add("fuck");
        badWords.add("shit");
        badWords.add("cunt");
        badWords.add("piece of shit");
        badWords.add("asshole");
        badWords.add("kill yourself");

        for(String badWord : badWords){
            if(text.toLowerCase().contains(badWord)){
                throw new ValidationException("Text contains bad words!");
            }
        }
    }
}
