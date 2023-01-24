package com.example.socialnetworkgui;

import com.example.socialnetworkgui.models.Notification;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationsController {

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
    Button logoButton;
    @FXML
    ListView<Notification> notificationsListView;

    @FXML
    public void initialize(){
        notificationsListView.setCellFactory(searchUsersListView -> new ListCell<>() {
            @Override
            protected void updateItem(Notification item, boolean empty) {
                super.updateItem(item, empty);
                setStyle("-fx-background-radius: 15; -fx-pref-height: 50px");
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getText());
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

        chatButton.setOnMouseEntered(event -> mouseEnteredButton(chatButton));
        chatButton.setOnMouseExited(event -> mouseExitedButton(chatButton));

        settingsButton.setOnMouseEntered(event -> mouseEnteredButton(settingsButton));
        settingsButton.setOnMouseExited(event -> mouseExitedButton(settingsButton));

        logoButton.setOnMouseEntered(event -> mouseEnteredLogo());
        logoButton.setOnMouseExited(event -> mouseExitedLogo());

        loadNotifications();

        System.out.println("NOTIFICATIONS CONTROLLER INITIALIZED!");
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

    private void loadNotifications() {
        try {
            User accountUser = StartApplication.getController().findAccount(LoggedUserManager.getUserMailFromFile()).getUser();

            Iterable<Notification> notificationsIterable = StartApplication.getController().notificationForUser(accountUser);
            List<Notification> notificationList = new ArrayList<>();
            notificationsIterable.forEach(notificationList::add);

            if(notificationList.size() == 0){
                byte[] emojiByteCode = new byte[]{(byte)0xE2, (byte)0x9C, (byte)0x8B};
                String emoji = new String(emojiByteCode, StandardCharsets.UTF_8);

                notificationList.add(new Notification(-1000L, ("Hi, " + accountUser.getSurname() + emoji), accountUser));
                notificationList.add(new Notification(-1001L, ("There's nothing new here!"), accountUser));

                emojiByteCode = new byte[]{(byte)0xF0, (byte)0x9F, (byte)0x8C, (byte)0x9E};
                emoji = new String(emojiByteCode, StandardCharsets.UTF_8);

                notificationList.add(new Notification(-1002L, ("Hope you have a great day!" + emoji), accountUser));
            }

            ObservableList<Notification> notificationObservableList = FXCollections.observableList(notificationList);
            notificationsListView.setItems(notificationObservableList);

        }
        catch(RepositoryException | SQLException e){
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
