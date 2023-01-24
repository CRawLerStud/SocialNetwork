package com.example.socialnetworkgui.service;

import com.example.socialnetworkgui.models.Post;
import com.example.socialnetworkgui.repository.PostRepositoryDB;
import com.example.socialnetworkgui.validators.Validator;

public class PostService extends ServiceImplementation<Long, Post>{

    PostRepositoryDB repository;

    public PostService(Validator<Post> validator, PostRepositoryDB repository) {
        super(validator, repository);
        this.repository = repository;
    }
}
