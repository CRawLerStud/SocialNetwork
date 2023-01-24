package com.example.socialnetworkgui;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.utils.LoggedUserManager;
import com.example.socialnetworkgui.validators.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class SettingsController {

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
    Label messageLabel;

    @FXML
    Button changeSurnameButton;
    @FXML
    Button changeLastnameButton;
    @FXML
    Button changeBirthdateButton;
    @FXML
    Button changePasswordButton;

    @FXML
    TextField lastnameTextField;
    @FXML
    TextField surnameTextField;
    @FXML
    PasswordField passwordTextField;
    @FXML
    PasswordField confirmPasswordTextField;
    @FXML
    DatePicker birthdatePicker;

    @FXML
    public void initialize(){

        myProfileButton.setOnMouseEntered(event -> mouseEnteredButton(myProfileButton));
        myProfileButton.setOnMouseExited(event -> mouseExitedButton(myProfileButton));

        homeButton.setOnMouseEntered(event -> mouseEnteredButton(homeButton));
        homeButton.setOnMouseExited(event -> mouseExitedButton(homeButton));

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

        messageLabel.setText("");

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

    public void changeSurnameAction(){
        String email = LoggedUserManager.getUserMailFromFile();

        String surnameText = surnameTextField.getText();
        try{
            User accountUser = StartApplication.getController().findAccount(email).getUser();
            System.out.println(accountUser);
            StartApplication.getController().changeUserSurname(accountUser.getId(), surnameText);
            messageLabel.setStyle("-fx-alignment: center; -fx-text-fill: green");
            messageLabel.setText("Surname changed!");
        }
        catch(RepositoryException| ValidationException| SQLException e){
            System.out.println(e.getMessage());
            messageLabel.setStyle("-fx-alignment: center; -fx-text-fill: red");
            messageLabel.setText(e.getMessage());
        }
    }
    public void changeLastnameAction(){
        String email = LoggedUserManager.getUserMailFromFile();

        String lastnameText = lastnameTextField.getText();
        try{
            if(lastnameText == null)
                throw new ValidationException("Enter a lastname!");
            User accountUser = StartApplication.getController().findAccount(email).getUser();
            StartApplication.getController().changeUserLastname(accountUser.getId(), lastnameText);
            messageLabel.setStyle("-fx-alignment: center; -fx-text-fill: green");
            messageLabel.setText("Lastname changed!");
        }
        catch(RepositoryException| ValidationException| SQLException e){
            System.out.println(e.getMessage());
            messageLabel.setStyle("-fx-alignment: center; -fx-text-fill: red");
            messageLabel.setText(e.getMessage());
        }
    }
    public void changeBirthdateAction(){
        String email = LoggedUserManager.getUserMailFromFile();

        LocalDate birthdate = birthdatePicker.getValue();

        try{
            if(birthdate == null)
                throw new ValidationException("Enter a date!");
            User accountUser = StartApplication.getController().findAccount(email).getUser();
            StartApplication.getController().changeUserBirthdate(accountUser.getId(), birthdate);
            messageLabel.setStyle("-fx-alignment: center; -fx-text-fill: green");
            messageLabel.setText("Birthdate changed!");
        }
        catch(ValidationException|RepositoryException|SQLException e){
            System.out.println(e.getMessage());
            messageLabel.setStyle("-fx-alignment: center; -fx-text-fill: red");
            messageLabel.setText(e.getMessage());
        }
    }
    public void changePasswordAction(){
        try{
            String password = passwordTextField.getText();
            String confirmPassword = confirmPasswordTextField.getText();
            if(password.equals(confirmPassword)){
                String email = LoggedUserManager.getUserMailFromFile();
                StartApplication.getController().changeAccountPassword(email, password);
                messageLabel.setStyle("-fx-alignment: center; -fx-text-fill: green");
                messageLabel.setText("Password changed!");
            }
            else{
                throw new ValidationException("Passwords don't match!");
            }
        }
        catch(ValidationException|RepositoryException|SQLException e){
            System.out.println(e.getMessage());
            messageLabel.setStyle("-fx-alignment: center; -fx-text-fill: red");
            messageLabel.setText(e.getMessage());
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
