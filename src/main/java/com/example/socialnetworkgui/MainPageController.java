package com.example.socialnetworkgui;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.models.friendship.FriendshipRequest;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.utils.LoggedUserManager;
import com.example.socialnetworkgui.validators.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainPageController {


    @FXML
    TextField searchTextField;
    @FXML
    Button chatButton;
    @FXML
    Button settingsButton;
    @FXML
    Button homeButton;
    @FXML
    Button myProfileButton;
    @FXML
    Button notificationButton;
    @FXML
    Button signOutButton;
    @FXML
    Button addFriendButton;

    @FXML
    Button cancelFriendButton;
    @FXML
    ListView<User> searchUsersListView;
    @FXML
    Label messageLabel;
    @FXML
    Button logoButton;

    @FXML
    public void initialize(){

        searchUsersListView.setCellFactory(searchUsersListView -> new ListCell<>(){
            @Override
            protected void updateItem(User item, boolean empty){
                super.updateItem(item, empty);
                setStyle("-fx-background-radius: 15");
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getSurname() + " " + item.getLastname());
                }
            }
        });

        homeButton.setOnMouseEntered(event -> mouseEnteredButton(homeButton));
        homeButton.setOnMouseExited(event -> mouseExitedButton(homeButton));

        myProfileButton.setOnMouseEntered(event -> mouseEnteredButton(myProfileButton));
        myProfileButton.setOnMouseExited(event -> mouseExitedButton(myProfileButton));

        notificationButton.setOnMouseEntered(event -> mouseEnteredButton(notificationButton));
        notificationButton.setOnMouseExited(event -> mouseExitedButton(notificationButton));

        signOutButton.setOnMouseEntered(event -> mouseEnteredButton(signOutButton));
        signOutButton.setOnMouseExited(event -> mouseExitedButton(signOutButton));

        logoButton.setOnMouseEntered(event -> mouseEnteredLogo());
        logoButton.setOnMouseExited(event -> mouseExitedLogo());

        settingsButton.setOnMouseEntered(event -> mouseEnteredButton(settingsButton));
        settingsButton.setOnMouseExited(event -> mouseExitedButton(settingsButton));

        chatButton.setOnMouseEntered(event -> mouseEnteredButton(chatButton));
        chatButton.setOnMouseExited(event -> mouseExitedButton(chatButton));

        messageLabel.setText("");

        searchUsersListView.setVisible(false);
        addFriendButton.setVisible(false);
        cancelFriendButton.setVisible(false);

        System.out.println("MAIN PAGE CONTROLLER INITIALIZED!");

    }

    public void cancelFriendAction(){
        try{
            User selectedUser = searchUsersListView.getSelectionModel().getSelectedItem();
            if(selectedUser != null) {
                String email = LoggedUserManager.getUserMailFromFile();
                User signedInUser = StartApplication.getController().findAccount(email).getUser();
                Pair<Long, Long> ID = new Pair<>((Math.min(selectedUser.getId(), signedInUser.getId())),
                        (Math.max(selectedUser.getId(), signedInUser.getId())));
                FriendshipRequest request =
                        StartApplication.getController().findFriendshipRequest(ID);
                if(request.getSender().equals(signedInUser)){
                    StartApplication.getController().deleteFriendshipRequest(ID);
                    messageLabel.setStyle("-fx-text-fill: green; -fx-alignment: center");
                    messageLabel.setText("The friendship request has been cancelled!");
                }
                else{
                    messageLabel.setStyle("-fx-text-fill: red; -fx-alignment: center");
                    messageLabel.setText("You are not the sender of the friendship request!");
                }
            }
        }
        catch(SQLException| RepositoryException e){
            messageLabel.setStyle("-fx-text-fill: red; -fx-alignment: center");
            messageLabel.setText(e.getMessage());
        }
    }
    public void sendFriendRequestAction(){
        try{
            User selectedUser = searchUsersListView.getSelectionModel().getSelectedItem();
            if(selectedUser != null) {
                String email = LoggedUserManager.getUserMailFromFile();
                User signedInUser = StartApplication.getController().findAccount(email).getUser();

                FriendshipRequest friendshipRequest = new FriendshipRequest(selectedUser, signedInUser, signedInUser);
                StartApplication.getController().addFriendshipRequest(friendshipRequest);
                messageLabel.setStyle("-fx-text-fill: green; -fx-alignment: center");
                messageLabel.setText("Friendship Request Sent!");
            }
        }
        catch(SQLException | RepositoryException | ValidationException e){
            messageLabel.setStyle("-fx-text-fill: red; -fx-alignment: center");
            messageLabel.setText(e.getMessage());
        }
    }
    public void searchUsers() {
        String sequence = searchTextField.getText();
        System.out.println(sequence);

        List<User> resultList;

        if(sequence.contains(" ")){
            List<String> names = List.of(sequence.split(" "));
            Set<User> result = new HashSet<>();
            for(String name : names){
                result.addAll(StartApplication.getController().usersWhoseLastnameStartsWith(name));
                result.addAll(StartApplication.getController().usersWhoseSurnameStartsWith(name));
            }
            resultList = new ArrayList<>(result);
        }
        else {

            Set<User> result = StartApplication.getController().usersWhoseSurnameStartsWith(sequence);
            result.forEach(System.out::println);
            result.addAll(StartApplication.getController().usersWhoseLastnameStartsWith(sequence));
            result.forEach(System.out::println);

            resultList = new ArrayList<>(result);
        }

        searchUsersListView.setVisible(true);
        addFriendButton.setVisible(true);
        cancelFriendButton.setVisible(true);

        ObservableList<User> searchUserObservable = FXCollections.observableList(resultList);
        searchUsersListView.setItems(searchUserObservable);

    }
    public void signOutAction(){
        try {
            LoggedUserManager.writeUserMailToFile("");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modern/signIn_modern.fxml"));
            Scene backScene = new Scene(loader.load(), 800, 600);
            StartApplication.getMainStage().setScene(backScene);
            StartApplication.getMainStage().setFullScreen(true);
            StartApplication.getMainStage().setFullScreen(false);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public void showMyProfile(){
        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("modern/profile_modern.fxml"));
            Scene profileScene = new Scene(loader.load(), 800, 600);
            StartApplication.getMainStage().setScene(profileScene);
            StartApplication.getMainStage().setFullScreen(true);
            StartApplication.getMainStage().setFullScreen(false);

        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public void loadHomeScene(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modern/mainScene_modern.fxml"));
            Scene mainScene = new Scene(loader.load(), 800, 600);
            StartApplication.getMainStage().setScene(mainScene);
            StartApplication.getMainStage().setFullScreen(true);
            StartApplication.getMainStage().setFullScreen(false);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public void showNotifications(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modern/notifications_modern.fxml"));
            Scene mainScene = new Scene(loader.load(), 800, 600);
            StartApplication.getMainStage().setScene(mainScene);
            StartApplication.getMainStage().setFullScreen(true);
            StartApplication.getMainStage().setFullScreen(false);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public void loadSettingsScene(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modern/settings_modern.fxml"));
            Scene mainScene = new Scene(loader.load(), 800, 600);
            StartApplication.getMainStage().setScene(mainScene);
            StartApplication.getMainStage().setFullScreen(true);
            StartApplication.getMainStage().setFullScreen(false);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public void loadChatAction(){
        try{
            FXMLLoader loadre = new FXMLLoader(getClass().getResource("modern/chat_modern.fxml"));
            Scene newScene = new Scene(loadre.load(), 800, 600);
            StartApplication.getMainStage().setScene(newScene);
            StartApplication.getMainStage().setFullScreen(true);
            StartApplication.getMainStage().setFullScreen(false);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }


    private void mouseEnteredLogo(){
        StartApplication.getMainStage().getScene().setCursor(Cursor.HAND);
    }
    private void mouseExitedLogo(){
        StartApplication.getMainStage().getScene().setCursor(Cursor.DEFAULT);
    }
    private void mouseEnteredButton(Button button) {
        button.setStyle("-fx-font-size: 15px; -fx-background-color: rgba(0, 0, 0, 0.1); -fx-text-fill: #D9FCFC");
        StartApplication.getMainStage().getScene().setCursor(Cursor.HAND);
    }
    private void mouseExitedButton(Button button) {
        button.setStyle("-fx-font-size: 15px; -fx-background-color: transparent; -fx-text-fill: #D9FCFC;");
        StartApplication.getMainStage().getScene().setCursor(Cursor.DEFAULT);
    }
}
