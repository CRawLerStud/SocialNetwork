package com.example.socialnetworkgui;

import com.example.socialnetworkgui.models.account.Account;
import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.utils.LoggedUserManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfilePageController {

    @FXML
    Label lastnameProfileText;
    @FXML
    Label surnameProfileText;
    @FXML
    Label birthdateProfileText;
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
    ListView<User> friendsListViewProfile;
    @FXML
    Button logoButton;

    @FXML
    public void initialize(){
        System.out.println("PROFILE CONTROLLER INITIALIZED!");

        friendsListViewProfile.setCellFactory(friendsListViewProfile -> new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setStyle("-fx-background-radius: 15");
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getSurname() + " " + item.getLastname());
                }
            }
        });

        loadProfile();

        homeButton.setOnMouseEntered(event -> mouseEnteredButton(homeButton));
        homeButton.setOnMouseExited(event -> mouseExitedButton(homeButton));

        myProfileButton.setOnMouseEntered(event -> mouseEnteredButton(myProfileButton));
        myProfileButton.setOnMouseExited(event -> mouseExitedButton(myProfileButton));

        notificationButton.setOnMouseEntered(event -> mouseEnteredButton(notificationButton));
        notificationButton.setOnMouseExited(event -> mouseExitedButton(notificationButton));

        chatButton.setOnMouseEntered(event -> mouseEnteredButton(chatButton));
        chatButton.setOnMouseExited(event -> mouseExitedButton(chatButton));

        settingsButton.setOnMouseEntered(event -> mouseEnteredButton(settingsButton));
        settingsButton.setOnMouseExited(event -> mouseExitedButton(settingsButton));

        signOutButton.setOnMouseEntered(event -> mouseEnteredButton(signOutButton));
        signOutButton.setOnMouseExited(event -> mouseExitedButton(signOutButton));

        logoButton.setOnMouseEntered(event -> mouseEnteredLogo());
        logoButton.setOnMouseExited(event -> mouseExitedLogo());
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

    private void loadProfile(){

        try {
            String email = LoggedUserManager.getUserMailFromFile();
            Account account = StartApplication.getController().findAccount(email);
            User accountUser = account.getUser();

            lastnameProfileText.setText(accountUser.getLastname());
            surnameProfileText.setText(accountUser.getSurname());
            birthdateProfileText.setText(accountUser.getBirthDate().toString() + " (" + accountUser.getYears()+ " years old)");


            List<User> userFriends = new ArrayList<>();
            for(User friend : accountUser.allFriends())
                userFriends.add(friend);
            ObservableList<User> observableList = FXCollections.observableList(userFriends);
            friendsListViewProfile.setItems(observableList);

        }
        catch(RepositoryException | SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void removeFriendAction(){
        try {
            Long selectedUserID = friendsListViewProfile.getSelectionModel().getSelectedItem().getId();
            String email = LoggedUserManager.getUserMailFromFile();
            Long myId = StartApplication.getController().findAccount(email).getUser().getId();

            Long user1ID = Math.min(selectedUserID, myId);
            Long user2ID = Math.max(selectedUserID, myId);

            Pair<Long, Long> friendshipID = new Pair<>(user1ID, user2ID);
            StartApplication.getController().deleteFriendship(friendshipID);
            System.out.println("DELETED FRIENDSHIP");

            Account account = StartApplication.getController().findAccount(email);
            User accountUser = account.getUser();

            List<User> userFriends = new ArrayList<>();
            accountUser.allFriends().forEach(userFriends::add);
            ObservableList<User> observableList = FXCollections.observableList(userFriends);
            friendsListViewProfile.setItems(observableList);
        }
        catch(SQLException|RepositoryException e){
            System.out.println(e.getMessage());
        }
    }
    public void showFriendRequests(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modern/friendRequests_modern.fxml"));
            Scene friendRequestsScene = new Scene(loader.load(), 800, 600);

            StartApplication.getMainStage().setScene(friendRequestsScene);
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
