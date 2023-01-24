package com.example.socialnetworkgui;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.models.chat.IndividualChat;
import com.example.socialnetworkgui.models.chat.Message;
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

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChatController {

    @FXML
    Button startConversationButton;
    @FXML
    Button deleteConversationButton;
    @FXML
    Button exitButton;
    @FXML
    ListView<IndividualChat> conversationsListView;
    @FXML
    Label nameLabel;

    @FXML
    public void initialize(){
        startConversationButton.setOnMouseEntered(event -> mouseEnteredButton(startConversationButton));
        startConversationButton.setOnMouseExited(event -> mouseExitedButton(startConversationButton));

        deleteConversationButton.setOnMouseEntered(event -> mouseEnteredButton(deleteConversationButton));
        deleteConversationButton.setOnMouseExited(event -> mouseExitedButton(deleteConversationButton));

        exitButton.setOnMouseEntered(event -> mouseEnteredButton(exitButton));
        exitButton.setOnMouseExited(event -> mouseExitedButton(exitButton));

        conversationsListView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                IndividualChat chat = conversationsListView.getSelectionModel().getSelectedItem();
                loadSelectedConversation(chat);
            }
        });

        conversationsListView.setCellFactory(conversationsListView -> new ListCell<>(){
            @Override
            protected void updateItem(IndividualChat item, boolean empty) {
                super.updateItem(item, empty);
                setStyle("-fx-pref-height: 75px");
                if(item == null){
                    setText(null);
                }
                else {
                    try {
                        String text = "";

                        User accountUser = StartApplication.getController().
                                findAccount(LoggedUserManager.getUserMailFromFile()).getUser();

                        Message lastMessage = StartApplication.getController().getLastMessageForChat(item);

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

                        if (accountUser.equals(item.getUser1())) {
                            text += item.getUser2().getSurname() + " " + item.getUser2().getLastname();
                        } else {
                            text += item.getUser1().getSurname() + " " + item.getUser1().getLastname();
                        }

                        if(lastMessage != null)
                            text += "\n" + lastMessage.getMessage() + "\n" + lastMessage.getSentTime().format(formatter);
                        else
                            text += "\nNo message yet!";

                        setText(text);
                    }
                    catch(SQLException|RepositoryException e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        });

        loadConversations();

        try {
            User accountUser = StartApplication.getController().
                    findAccount(LoggedUserManager.getUserMailFromFile()).getUser();
            nameLabel.setText(accountUser.getSurname() + " " + accountUser.getLastname());
        }
        catch(SQLException|RepositoryException e){
            System.out.println(e.getMessage());
        }

        System.out.println("Chat Controller Initialized!");
    }

    private void loadConversations(){
        try {
            String userEmail = LoggedUserManager.getUserMailFromFile();
            User accountUser = StartApplication.getController().findAccount(userEmail).getUser();

            Iterable<IndividualChat> chatsIterable = StartApplication.getController().getAllChatsForUser(accountUser);
            List<IndividualChat> chats = new ArrayList<>();
            chatsIterable.forEach(chats::add);

            ObservableList<IndividualChat> individualChatObservableList = FXCollections.observableList(chats);
            conversationsListView.setItems(individualChatObservableList);
        }
        catch(SQLException| RepositoryException e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void startConversationAction(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modern/new_conversation_modern.fxml"));
            Scene newScene = new Scene(loader.load(), 800, 600);
            StartApplication.getMainStage().setScene(newScene);
            StartApplication.getMainStage().setFullScreen(true);
            StartApplication.getMainStage().setFullScreen(false);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void deleteConversationAction(){
        try {
            IndividualChat selectedChat = conversationsListView.getSelectionModel().getSelectedItem();
            StartApplication.getController().deleteChat(selectedChat.getId());
            loadConversations();
        }
        catch(SQLException|RepositoryException e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void exitAction(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modern/mainScene_modern.fxml"));
            Scene newScene = new Scene(loader.load(), 800, 600);
            StartApplication.getMainStage().setScene(newScene);
            StartApplication.getMainStage().setFullScreen(true);
            StartApplication.getMainStage().setFullScreen(false);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    private void loadSelectedConversation(IndividualChat conversation) {
        try {
            User accountUser = StartApplication.getController().findAccount(LoggedUserManager.getUserMailFromFile()).getUser();
            LoggedUserManager.writeChatDetailsToFile(conversation, accountUser);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("modern/conversation_modern.fxml"));
            Scene newScene = new Scene(loader.load(), 800, 600);
            StartApplication.getMainStage().setScene(newScene);
            StartApplication.getMainStage().setFullScreen(true);
            StartApplication.getMainStage().setFullScreen(false);
        }
        catch(SQLException|RepositoryException|IOException e){
            System.out.println(e.getMessage());
        }
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
