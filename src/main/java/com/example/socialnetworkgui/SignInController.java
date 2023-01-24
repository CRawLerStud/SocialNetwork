package com.example.socialnetworkgui;

import com.example.socialnetworkgui.models.account.Account;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.utils.LoggedUserManager;
import com.example.socialnetworkgui.validators.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.io.IOException;
import java.sql.SQLException;

public class SignInController {

    @FXML
    TextField usernameTextField;
    @FXML
    PasswordField passwordTextField;
    @FXML
    Label messageLabel;

    @FXML
    public void initialize(){
        System.out.println("SIGN IN CONTROLLER INITIALIZED!");
        messageLabel.setText("");
    }

    @FXML
    public void signInAction(){
        try{
            String email = usernameTextField.getText();
            String password = passwordTextField.getText();

            Account account = StartApplication.getController().findAccount(email);
            if(account.getPassword().equals(password)){
                messageLabel.setText("");
                LoggedUserManager.writeUserMailToFile(email);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("modern/mainScene_modern.fxml"));
                Scene mainScene= new Scene(fxmlLoader.load(), 800, 600);
                StartApplication.getMainStage().setScene(mainScene);
                StartApplication.getMainStage().setFullScreen(true);
                StartApplication.getMainStage().setFullScreen(false);
            }
            else{
                throw new ValidationException("Invalid password!");
            }
        }
        catch(RepositoryException | SQLException | ValidationException | IOException e){
            messageLabel.setText(e.getMessage());
            messageLabel.setFont(Font.font(20));
            messageLabel.setTextFill(Paint.valueOf("red"));
        }
    }

    @FXML
    public void registerAction(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("modern/register_modern.fxml"));
            Scene registerScene = new Scene(fxmlLoader.load(), 800, 600);
            StartApplication.getMainStage().setScene(registerScene);
            StartApplication.getMainStage().setFullScreen(true);
            StartApplication.getMainStage().setFullScreen(false);
        }
        catch(IOException e){
            System.out.println("Register window does not open because: " + e.getMessage());
        }
    }

}
