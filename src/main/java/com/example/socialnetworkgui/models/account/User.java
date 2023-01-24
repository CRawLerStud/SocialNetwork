package com.example.socialnetworkgui.models.account;

import com.example.socialnetworkgui.models.Entity;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User extends Entity<Long> {
    private String lastname;
    private String surname;
    private LocalDate birthDate;

    public User(Long ID, String lastname, String surname, LocalDate birthDate) {
        this.lastname = lastname;
        this.surname = surname;
        this.birthDate = birthDate;
        this.setId(ID);
    }

    public User(String lastname, String surname, LocalDate birthDate) {
        this.lastname = lastname;
        this.surname = surname;
        this.birthDate = birthDate;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Returns an iterable with all the user's friends
     * @return all the user's friends
     */
    public Iterable<User> allFriends(){
        Set<User> result = new HashSet<>();
        try{
            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/socialNetwork",
                    "postgres",
                    "postgres"
            );
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT users.id, users.lastname, users.surname, users.birthdate " +
                            "FROM friendships " +
                            "INNER JOIN users ON " +
                            "(users.id = friendships.\"user1ID\" AND friendships.\"user1ID\" != ? )" +
                            " OR "+
                            "(users.id = friendships.\"user2ID\" AND friendships.\"user2ID\" != ? )" +
                            "WHERE \"user1ID\" = ? OR \"user2ID\" = ? "
            );
            Long ID = this.getId();
            statement.setLong(1, ID);
            statement.setLong(2, ID);
            statement.setLong(3, ID);
            statement.setLong(4, ID);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Long userID = resultSet.getLong("id");
                String lastname = resultSet.getString("lastname");
                String surname = resultSet.getString("surname");
                LocalDate birthdate = resultSet.getDate("birthdate").toLocalDate();

                User friend = new User(lastname, surname, birthdate);
                friend.setId(userID);

                result.add(friend);
            }
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Calculate and return the age of the user
     * @return the age of the user
     */
    public int getYears(){
        return Math.abs(Period.between(birthDate, LocalDate.now()).getYears());
    }

    @Override
    public String toString() {
        return this.getId() + ";" + this.getLastname() + ";" + this.getSurname() + ";" + this.getBirthDate().toString();
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.getId());
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(user.getId(), this.getId());
    }
}

