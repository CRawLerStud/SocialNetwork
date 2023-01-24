package com.example.socialnetworkgui;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.models.chat.IndividualChat;
import com.example.socialnetworkgui.models.chat.Message;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.utils.LoggedUserManager;
import com.example.socialnetworkgui.validators.ValidationException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ConversationController {

    public class MyRunnableTask implements Runnable{
        public void run(){
            Platform.runLater(() -> loadMessages());
        }
    }

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> future = executor.scheduleAtFixedRate(new MyRunnableTask(), 0, 1, TimeUnit.SECONDS);

    @FXML
    Button conversationNameButton;
    @FXML
    Button sendMessageButton;
    @FXML
    TextField messageTextField;
    @FXML
    ListView<Message> messageListView;
    @FXML
    Button backButton;

    @FXML
    public void initialize(){
        sendMessageButton.setOnMouseEntered(event -> mouseEnteredButton(sendMessageButton));
        sendMessageButton.setOnMouseExited(event -> mouseExitedButton(sendMessageButton));

        backButton.setOnMouseEntered(event -> mouseEnteredButton(backButton));
        backButton.setOnMouseExited(event -> mouseExitedButton(backButton));

        String conversationName = LoggedUserManager.getChatName();
        conversationNameButton.setText(conversationName);

        try {
            User accountUser = StartApplication.getController().findAccount(LoggedUserManager.getUserMailFromFile()).getUser();

            messageListView.setCellFactory(messageListView -> new ListCell<>(){
                @Override
                protected void updateItem(Message item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item == null){
                        setText(null);
                    }
                    else{
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        String text;
                        String sent_time = item.getSentTime().format(formatter);
                        if(accountUser.equals(item.getSender())){
                            text = "You\n " + item.getMessage() + "\n" + sent_time;
                            setStyle("-fx-alignment: center_right");
                        }
                        else{
                            text = item.getSender().getSurname() + " " + item.getSender().getLastname() + "\n" +
                                    item.getMessage() + "\n" + sent_time;
                            setStyle("-fx-alignment: center_left");
                        }
                        setText(text);
                    }
                }
            });

        }
        catch(SQLException|RepositoryException e){
            System.out.println(e.getMessage());
        }

        loadMessages();

        System.out.println("CONVERSATION CONTROLLER INITIALIZED!");
    }

    @FXML
    public void backAction(){
        try{
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

    private void loadMessages() {
        try {
            IndividualChat chat = StartApplication.getController().findChat(LoggedUserManager.getChatID());
            Iterable<Message> messages = StartApplication.getController().findAllMessagesForChat(chat);
            List<Message> messageList = new ArrayList<>();
            messages.forEach(messageList::add);

            ObservableList<Message> messageObservableList = FXCollections.observableList(messageList);
            messageListView.setItems(messageObservableList);
            System.out.println("messages loaded!");
        }
        catch(SQLException| RepositoryException e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void sendMessageAction(){
        try{
            User accountUser = StartApplication.getController().findAccount(LoggedUserManager.getUserMailFromFile()).getUser();
            IndividualChat chat = StartApplication.getController().findChat(LoggedUserManager.getChatID());
            String text = messageTextField.getText();
            System.out.println(text);
            Message newMessage = new Message(accountUser, chat, text, LocalDateTime.now());
            System.out.println(newMessage);
            StartApplication.getController().addMessage(newMessage);
            StartApplication.getController().messageNotification(newMessage);
            loadMessages();
            messageTextField.clear();
        }
        catch(SQLException | RepositoryException | ValidationException e){
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
