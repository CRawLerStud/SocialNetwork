package com.example.socialnetworkgui;

import com.example.socialnetworkgui.models.account.Account;
import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.validators.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class RegisterController {

    @FXML
    Label registerMessageLabel;
    @FXML
    TextField emailTextField;
    @FXML
    PasswordField registerPasswordTextField;
    @FXML
    PasswordField confirmPasswordTextField;
    @FXML
    TextField lastnameTextField;
    @FXML
    TextField surnameTextField;
    @FXML
    DatePicker birthdatePicker;

    @FXML
    public void initialize(){
        System.out.println("REGISTER CONTROLLER INITIALIZED!");
        registerMessageLabel.setText("");
    }

    @FXML
    public void registerAccountAction(){

        User user = null;

        try {
            String email = emailTextField.getText();
            String password = registerPasswordTextField.getText();
            String confirmPassword = confirmPasswordTextField.getText();
            String lastname = lastnameTextField.getText();
            String surname = surnameTextField.getText();
            LocalDate birthdate = birthdatePicker.getValue();

            if (password.equals(confirmPassword)) {
                Long userID = StartApplication.getController().addUser(new User(lastname, surname, birthdate));

                System.out.println(userID);
                User accountUser = StartApplication.getController().findUser(userID);

                user = accountUser;

                Account newAccount = new Account(email, password, accountUser);
                StartApplication.getController().addAccount(newAccount);

                registerMessageLabel.setText("Account registered!");
                registerMessageLabel.setFont(Font.font(20));
                registerMessageLabel.setTextFill(Paint.valueOf("green"));


            }
        }
        catch(RepositoryException | SQLException | ValidationException e){
            registerMessageLabel.setText(e.getMessage());
            registerMessageLabel.setFont(Font.font(20));
            registerMessageLabel.setTextFill(Paint.valueOf("red"));
            try {
                if (user != null)
                    StartApplication.getController().deleteUser(user.getId());
            }
            catch(SQLException|RepositoryException e2){
                System.out.println(e.getMessage());
            }
        }

    }

    @FXML
    public void backSceneAction(){
        try {
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
}
