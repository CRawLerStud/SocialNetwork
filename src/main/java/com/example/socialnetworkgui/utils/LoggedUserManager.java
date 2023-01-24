package com.example.socialnetworkgui.utils;

import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.models.chat.IndividualChat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class LoggedUserManager {

    public static void writeUserMailToFile(String email){
        try {
            FileWriter fw = new FileWriter("C:\\Facultate\\MAP\\SocialNetworkGUI\\src\\main\\java\\com\\example\\socialnetworkgui\\user.txt");
            fw.write(email);
            fw.close();
        }
        catch(IOException e){
            System.err.println("Error while writing in the signed in user file!");
            e.printStackTrace();
        }
    }
    public static String getUserMailFromFile(){
        try {
            File file = new File("C:\\Facultate\\MAP\\SocialNetworkGUI\\src\\main\\java\\com\\example\\socialnetworkgui\\user.txt");
            Scanner sc = new Scanner(file);

            return sc.next();
        }
        catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void writeChatDetailsToFile(IndividualChat chat, User user){
        try{
            FileWriter fw = new FileWriter("C:\\Facultate\\MAP\\SocialNetworkGUI\\src\\main\\java\\com\\example\\socialnetworkgui\\open_conversation.txt");
            if(chat.getUser1().equals(user)){
                fw.write(chat.getId() + "\n" + chat.getUser2().getSurname() + " " + chat.getUser2().getLastname());
            }
            else{
                fw.write(chat.getId() + "\n" + chat.getUser1().getSurname() + " " +chat.getUser1().getLastname());
            }
            fw.close();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static String getChatID(){
        try{
            File file = new File("C:\\Facultate\\MAP\\SocialNetworkGUI\\src\\main\\java\\com\\example\\socialnetworkgui\\open_conversation.txt");
            Scanner sc = new Scanner(file);

            return sc.nextLine();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static String getChatName(){
        try{
            File file = new File("C:\\Facultate\\MAP\\SocialNetworkGUI\\src\\main\\java\\com\\example\\socialnetworkgui\\open_conversation.txt");
            Scanner sc = new Scanner(file);

            sc.nextLine();
            return sc.nextLine();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
