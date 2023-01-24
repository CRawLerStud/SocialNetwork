package com.example.socialnetworkgui;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.models.friendship.Friendship;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestsController {

    @FXML
    ListView<FriendshipRequest> friendRequestsListView;
    @FXML
    Label friendRequestsMessageLabel;

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
    public void initialize(){

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

        friendRequestsListView.setCellFactory(friendRequestsListView -> new ListCell<>(){
            @Override
            protected void updateItem(FriendshipRequest item, boolean empty) {
                super.updateItem(item, empty);
                setStyle("-fx-background-radius: 15");
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getSender().getLastname() + " " + item.getSender().getSurname() + " | " + item.getStatus() + " | " + item.getSentTime().toString());
                }
            }
        });

        friendRequestsMessageLabel.setStyle("-fx-text-alignment: center");

        loadFriendRequests();

        System.out.println("FRIEND REQUEST CONTROLLER INITIALIZED");
    }
    public void refuseFriendRequest(){
        FriendshipRequest selectedFriendshipRequest = friendRequestsListView.getSelectionModel().getSelectedItem();
        if(selectedFriendshipRequest != null){
            try{
                StartApplication.getController().refuseFriendshipRequest(selectedFriendshipRequest);

                refreshFriendshipRequestsObservableList();
            }
            catch(RepositoryException|SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
    public void acceptFriendRequest(){
        FriendshipRequest selectedFriendshipRequest = friendRequestsListView.getSelectionModel().getSelectedItem();
        if(selectedFriendshipRequest != null){
            if(selectedFriendshipRequest.getStatus().equals("Pending")) {
                try{
                User user1 = selectedFriendshipRequest.getUser1();
                User user2 = selectedFriendshipRequest.getUser2();
                Friendship newFriendship = new Friendship(user1, user2);
                StartApplication.getController().addFriendship(newFriendship);
                StartApplication.getController().acceptFriendshipRequest(selectedFriendshipRequest);

                refreshFriendshipRequestsObservableList();
                }
                catch (SQLException|RepositoryException|ValidationException e){
                    System.out.println(e.getMessage());
                }
            }
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
    private void refreshFriendshipRequestsObservableList() throws SQLException, RepositoryException{
        User accountUser = StartApplication.getController().findAccount(LoggedUserManager.getUserMailFromFile()).getUser();

        List<FriendshipRequest> friendshipRequestList = new ArrayList<>();
        Iterable<FriendshipRequest> friendshipRequests = StartApplication.getController().findAllFriendshipRequests();
        for(FriendshipRequest request : friendshipRequests){
            if((!request.getSender().equals(accountUser)) && (request.getUser1().equals(accountUser) || request.getUser2().equals(accountUser))){
                friendshipRequestList.add(request);
            }
        }

        ObservableList<FriendshipRequest> friendshipRequestObservableList = FXCollections.observableList(friendshipRequestList);
        friendRequestsListView.setItems(friendshipRequestObservableList);
    }
    private void loadFriendRequests(){

        try {
            User accountUser = StartApplication.getController().findAccount(LoggedUserManager.getUserMailFromFile()).getUser();

            List<FriendshipRequest> friendshipRequestList = new ArrayList<>();
            Iterable<FriendshipRequest> friendshipRequests = StartApplication.getController().pendingFriendshipRequestsForUser(accountUser);
            friendshipRequests.forEach(friendshipRequestList::add);

            if(friendshipRequestList.size() == 0){
                friendRequestsMessageLabel.setText("There's no new friendship request for you yet!");
            }
            else{
                friendRequestsMessageLabel.setText("Friendship Requests: " + friendshipRequestList.size());
            }

            ObservableList<FriendshipRequest> friendshipRequestObservableList = FXCollections.observableList(friendshipRequestList);
            friendRequestsListView.setItems(friendshipRequestObservableList);

        }
        catch(RepositoryException|SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
