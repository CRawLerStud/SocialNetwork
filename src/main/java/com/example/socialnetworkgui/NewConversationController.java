package com.example.socialnetworkgui;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.models.chat.IndividualChat;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.utils.LoggedUserManager;
import com.example.socialnetworkgui.validators.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NewConversationController {

    @FXML
    ComboBox<User> userComboBox;
    @FXML
    Button startConversationButton;
    @FXML
    Label nameLabel;
    @FXML
    Label messageLabel;
    @FXML
    Button backButton;

    @FXML
    public void initialize(){
        startConversationButton.setOnMouseEntered(event -> mouseEnteredButton(startConversationButton));
        startConversationButton.setOnMouseExited(event -> mouseExitedButton(startConversationButton));

        try {
            User accountUser = StartApplication.getController().findAccount(LoggedUserManager.getUserMailFromFile()).getUser();
            nameLabel.setText(accountUser.getSurname() + " " + accountUser.getLastname());
        }
        catch(RepositoryException | SQLException e){
            System.out.println(e.getMessage());
        }

        userComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setText(null);
                } else {
                    setText(item.getSurname() + " " + item.getLastname());
                }
            }
        });

        userComboBox.setCellFactory(userComboBox -> new ListCell<>(){
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null){
                    setText(null);
                }
                else{
                    setText(item.getSurname() + " " + item.getLastname());
                }
            }
        });

        loadUsersForComboBox();
    }

    private void mouseEnteredButton(Button button) {
        button.setStyle("-fx-font-size: 15px; -fx-background-color: rgba(0, 0, 0, 0.1); -fx-text-fill: #D9FCFC");
        StartApplication.getMainStage().getScene().setCursor(Cursor.HAND);
    }
    private void mouseExitedButton(Button button) {
        button.setStyle("-fx-font-size: 15px; -fx-background-color: transparent; -fx-text-fill: #D9FCFC;");
        StartApplication.getMainStage().getScene().setCursor(Cursor.DEFAULT);
    }

    @FXML
    public void startConversationAction(){
        try{
            User selectedUser = userComboBox.getSelectionModel().getSelectedItem();
            User accountUser = StartApplication.getController().findAccount(LoggedUserManager.getUserMailFromFile()).getUser();
            IndividualChat newChat = new IndividualChat(selectedUser, accountUser);
            StartApplication.getController().addChat(newChat);

            backAction();
        }
        catch(RepositoryException | SQLException | ValidationException e){
            messageLabel.setText(e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red");
        }
    }

    @FXML
    public void backAction(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modern/chat_modern.fxml"));
            Scene newScene = new Scene(loader.load(), 800, 600);

            StartApplication.getMainStage().setScene(newScene);
            StartApplication.getMainStage().setFullScreen(true);
            StartApplication.getMainStage().setFullScreen(false);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    private void loadUsersForComboBox(){
        try {
            User accountUser = StartApplication.getController().findAccount(LoggedUserManager.getUserMailFromFile()).getUser();
            Set<User> userSet = StartApplication.getController().getAllFriendsForUser(accountUser);
            List<User> users = new ArrayList<>(userSet);

            ObservableList<User> userObservableList = FXCollections.observableList(users);
            userComboBox.setItems(userObservableList);
        }
        catch(SQLException|RepositoryException e){
            System.out.println(e.getMessage());
        }
    }

}
