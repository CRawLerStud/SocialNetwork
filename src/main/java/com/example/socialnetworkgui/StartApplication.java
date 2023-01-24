package com.example.socialnetworkgui;

import com.example.socialnetworkgui.controller.Controller;
import com.example.socialnetworkgui.repository.NotificationRepositoryDB;
import com.example.socialnetworkgui.repository.PostRepositoryDB;
import com.example.socialnetworkgui.repository.account.AccountRepositoryDB;
import com.example.socialnetworkgui.repository.account.UserRepositoryDB;
import com.example.socialnetworkgui.repository.chat.IndividualChatsRepositoryDB;
import com.example.socialnetworkgui.repository.chat.MessageRepositoryDB;
import com.example.socialnetworkgui.repository.friendship.FriendshipRepositoryDB;
import com.example.socialnetworkgui.repository.friendship.FriendshipRequestRepositoryDB;
import com.example.socialnetworkgui.service.NotificationService;
import com.example.socialnetworkgui.service.PostService;
import com.example.socialnetworkgui.service.account.AccountService;
import com.example.socialnetworkgui.service.account.UserService;
import com.example.socialnetworkgui.service.chat.IndividualChatService;
import com.example.socialnetworkgui.service.chat.MessageService;
import com.example.socialnetworkgui.service.friendship.FriendshipRequestService;
import com.example.socialnetworkgui.service.friendship.FriendshipService;
import com.example.socialnetworkgui.validators.PostValidator;
import com.example.socialnetworkgui.validators.account.AccountValidator;
import com.example.socialnetworkgui.validators.account.UserValidator;
import com.example.socialnetworkgui.validators.chat.IndividualChatValidator;
import com.example.socialnetworkgui.validators.chat.MessageValidator;
import com.example.socialnetworkgui.validators.friendship.FriendshipRequestValidator;
import com.example.socialnetworkgui.validators.friendship.FriendshipValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application{
    private static Controller controller;
    private static Stage mainStage;

    public static Stage getMainStage() {
        return mainStage;
    }

    public static Controller getController() {
        return controller;
    }

    @Override
    public void start(Stage stage) throws IOException {

        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        FriendshipRequestValidator friendshipRequestValidator = new FriendshipRequestValidator();
        AccountValidator accountValidator = new AccountValidator(userValidator);
        PostValidator postValidator = new PostValidator();
        IndividualChatValidator individualChatValidator = new IndividualChatValidator();
        MessageValidator messageValidator = new MessageValidator();

        String url = "jdbc:postgresql://localhost:5432/socialNetwork";
        String username = "postgres";
        String password = "postgres";

        FriendshipRepositoryDB friendshipRepositoryDB = new FriendshipRepositoryDB(url, username, password);
        UserRepositoryDB userRepositoryDB = new UserRepositoryDB(url, username, password);
        FriendshipRequestRepositoryDB friendshipRequestRepositoryDB = new FriendshipRequestRepositoryDB(url, username, password);
        AccountRepositoryDB accountRepositoryDB = new AccountRepositoryDB(url, username, password);
        PostRepositoryDB postRepositoryDB = new PostRepositoryDB(url, username, password);
        NotificationRepositoryDB notificationRepositoryDB = new NotificationRepositoryDB(url, username, password);
        IndividualChatsRepositoryDB individualChatsRepositoryDB = new IndividualChatsRepositoryDB(url, username, password);
        MessageRepositoryDB messageRepositoryDB = new MessageRepositoryDB(url, username, password);

        UserService userService = new UserService(userValidator, userRepositoryDB);
        FriendshipService friendshipService = new FriendshipService(friendshipValidator, friendshipRepositoryDB);
        FriendshipRequestService friendshipRequestService = new FriendshipRequestService(friendshipRequestValidator, friendshipRequestRepositoryDB);
        AccountService accountService = new AccountService(accountValidator, accountRepositoryDB);
        PostService postService = new PostService(postValidator, postRepositoryDB);
        NotificationService notificationService = new NotificationService(notificationRepositoryDB);
        IndividualChatService conversationService = new IndividualChatService(individualChatValidator, individualChatsRepositoryDB);
        MessageService messageService = new MessageService(messageValidator, messageRepositoryDB);

        controller = new Controller(
                friendshipService, userService, friendshipRequestService,
                accountService, postService, notificationService,
                conversationService, messageService
        );

        FXMLLoader loader = new FXMLLoader(getClass().getResource("modern/signIn_modern.fxml"));
        Scene signInScene = new Scene(loader.load(), 800, 600);

        mainStage = stage;

        stage.setResizable(false);
        stage.setScene(signInScene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
