package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.models.Notification;
import com.example.socialnetworkgui.models.Post;
import com.example.socialnetworkgui.models.account.Account;
import com.example.socialnetworkgui.models.account.User;
import com.example.socialnetworkgui.models.chat.IndividualChat;
import com.example.socialnetworkgui.models.chat.Message;
import com.example.socialnetworkgui.models.friendship.Friendship;
import com.example.socialnetworkgui.models.friendship.FriendshipRequest;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.NotificationService;
import com.example.socialnetworkgui.service.PostService;
import com.example.socialnetworkgui.service.account.AccountService;
import com.example.socialnetworkgui.service.account.UserService;
import com.example.socialnetworkgui.service.chat.IndividualChatService;
import com.example.socialnetworkgui.service.chat.MessageService;
import com.example.socialnetworkgui.service.friendship.FriendshipRequestService;
import com.example.socialnetworkgui.service.friendship.FriendshipService;
import com.example.socialnetworkgui.utils.CommunityUtils;
import com.example.socialnetworkgui.validators.ValidationException;
import javafx.util.Pair;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class Controller {

    private final FriendshipService friendships;
    private final UserService users;
    private final FriendshipRequestService friendshipRequests;
    private final AccountService accounts;
    private final PostService posts;
    private final NotificationService notifications;
    private final IndividualChatService chats;
    private final MessageService messages;

    public Controller(FriendshipService friendships, UserService users,
                      FriendshipRequestService friendshipRequests, AccountService accounts,
                      PostService posts, NotificationService notifications,
                      IndividualChatService chats, MessageService messages){
        this.friendships = friendships;
        this.users = users;
        this.friendshipRequests = friendshipRequests;
        this.accounts = accounts;
        this.posts = posts;
        this.notifications = notifications;
        this.chats = chats;
        this.messages = messages;
    }

    //USERS CONTROLLER PART

    public Long addUser(User newUser) throws RepositoryException, SQLException, ValidationException{
        return users.save(newUser);
    }

    public User deleteUser(Long ID) throws RepositoryException, SQLException{
        User deletedUser = findUser(ID);
        friendships.removeUserFriendships(deletedUser);
        friendshipRequests.removeAllFriendshipRequestsForUser(deletedUser);
        return users.delete(ID);
    }

    public User findUser(Long ID) throws RepositoryException, SQLException{
        return users.findOne(ID);
    }

    public Iterable<User> findAllUsers() throws SQLException{
        return users.findAll();
    }

    public User changeUserBirthdate(Long ID, LocalDate newBirthdate) throws ValidationException, RepositoryException, SQLException{
        return users.changeUserBirthdate(ID, newBirthdate);
    }

    public User changeUserLastname(Long ID, String newLastname) throws ValidationException,RepositoryException, SQLException{
        return users.changeUserLastname(ID, newLastname);
    }

    public User changeUserSurname(Long ID, String newSurname) throws ValidationException, RepositoryException, SQLException{
        return users.changeUserSurname(ID, newSurname);
    }

    public Set<User> usersWithLastname(String lastname) {
        return users.usersWithLastname(lastname);
    }

    public Set<User> usersWithSurname(String surname) {
        return users.usersWithSurname(surname);
    }

    public Set<User> usersWithFullname(String lastname, String surname) {
        return users.usersWithFullname(lastname, surname);
    }

    public Set<User> usersOlderThan(int minimumAge){
        return users.usersOlderThan(minimumAge);
    }

    public Set<User> usersWhoseLastnameStartsWith(String sequence){
        return users.usersWhoseLastnameStartsWith(sequence);
    }

    public Set<User> usersWhoseSurnameStartsWith(String sequence){
        return users.usersWhoseSurnameStartsWith(sequence);
    }

    public Set<User> getAllFriendsForUser(User user) throws SQLException{
        return users.getAllFriendsForUser(user);
    }

    //FRIENDSHIP CONTROLLER PART

    public Pair<Long, Long> addFriendship(Friendship newFriendship) throws RepositoryException, SQLException, ValidationException{
        return friendships.save(newFriendship);
    }

    public Friendship deleteFriendship(Pair<Long, Long> friendshipID) throws RepositoryException, SQLException{
        return friendships.delete(friendshipID);
    }

    public Friendship findFriendship(Pair<Long, Long> friendshipID) throws RepositoryException, SQLException{
        return friendships.findOne(friendshipID);
    }

    public Iterable<Friendship> findAllFriendships() throws SQLException{
        return friendships.findAll();
    }

    public Pair<Long, Long> addFriendshipRequest(FriendshipRequest newFriendshipRequest) throws ValidationException, RepositoryException, SQLException{

        try{
            friendships.findOne(newFriendshipRequest.getId());
        }
        catch(RepositoryException e){
            Pair<Long, Long> pairID = friendshipRequests.save(newFriendshipRequest);
            friendshipRequestNotification(newFriendshipRequest);
            return pairID;
        }
        throw new RepositoryException("Friendship existent!");
    }

    //FRIENDSHIP REQUEST CONTROLLER PART

    public FriendshipRequest deleteFriendshipRequest(Pair<Long, Long> friendshipRequestID) throws RepositoryException, SQLException{
        return friendshipRequests.delete(friendshipRequestID);
    }

    public FriendshipRequest findFriendshipRequest(Pair<Long, Long> friendshipRequestID) throws RepositoryException, SQLException{
        return friendshipRequests.findOne(friendshipRequestID);
    }

    public Iterable<FriendshipRequest> findAllFriendshipRequests() throws SQLException{
        return friendshipRequests.findAll();
    }

    public FriendshipRequest acceptFriendshipRequest(FriendshipRequest friendshipRequest) throws ValidationException, RepositoryException, SQLException{
        FriendshipRequest acceptedFriendshipRequest = friendshipRequests.acceptFriendshipRequest(friendshipRequest);
        responseFriendshipRequestNotification(acceptedFriendshipRequest);
        deleteFriendshipRequest(friendshipRequest.getId());
        return acceptedFriendshipRequest;
    }

    public FriendshipRequest refuseFriendshipRequest(FriendshipRequest friendshipRequest) throws RepositoryException, SQLException{
        FriendshipRequest refusedFriendshipRequest = friendshipRequests.refuseFriendshipRequest(friendshipRequest);
        responseFriendshipRequestNotification(refusedFriendshipRequest);
        deleteFriendshipRequest(friendshipRequest.getId());
        return refusedFriendshipRequest;
    }

    public Iterable<FriendshipRequest> friendshipRequestsForUser(User user) throws SQLException{
        return friendshipRequests.friendshipRequestsForUser(user);
    }

    public Iterable<FriendshipRequest> pendingFriendshipRequestsForUser(User user) throws SQLException{
        return friendshipRequests.pendingFriendshipRequestsForUser(user);
    }

    //COMUNITY PART

    public List<List<User>> discoverCommunities() throws SQLException{
        return CommunityUtils.discoverCommunities(users.findAll());
    }

    public List<User> mostSociableCommunity() throws SQLException{
        return CommunityUtils.mostSociableCommunity(users.findAll());
    }

    //ACCOUNTS CONTROLLER PART

    public String addAccount(Account account) throws ValidationException, RepositoryException, SQLException{
        return accounts.save(account);
    }

    public Account deleteAccount(String email) throws RepositoryException, SQLException{
        Account deletedAccount = findAccount(email);
        this.deleteUser(deletedAccount.getUser().getId());
        accounts.delete(email);
        return deletedAccount;
    }

    public Account findAccount(String email) throws RepositoryException, SQLException{
        return accounts.findOne(email);
    }

    public Iterable<Account> findAllAccounts() throws SQLException{
        return accounts.findAll();
    }

    public void changeAccountPassword(String email, String newPassword) throws SQLException, RepositoryException, ValidationException{
        accounts.changeAccountPassword(email, newPassword);
    }

    //POSTS CONTROLLER PART

    public Long addPost(Post post) throws ValidationException, RepositoryException, SQLException{
        return posts.save(post);
    }

    public Post deletePost(Long postID) throws RepositoryException, SQLException{
        return posts.delete(postID);
    }

    public Post findPost(Long postID) throws RepositoryException, SQLException{
        return posts.findOne(postID);
    }

    public Iterable<Post> findAllPosts() throws SQLException{
        return posts.findAll();
    }

    //NOTIFICATIONS CONTROLLER PART

    public void friendshipRequestNotification(FriendshipRequest friendshipRequest) throws SQLException, RepositoryException{
        notifications.friendshipRequestNotification(friendshipRequest);
    }

    public void responseFriendshipRequestNotification(FriendshipRequest friendshipRequest) throws SQLException, RepositoryException{
        notifications.responseFriendshipRequestNotification(friendshipRequest);
    }

    public void messageNotification(Message message) throws SQLException, RepositoryException{
        notifications.messageNotification(message);
    }

    public Iterable<Notification> notificationForUser(User user) throws SQLException {
        return notifications.notificationForUser(user);
    }

    //chats controller part
    public String addChat(IndividualChat chat) throws ValidationException, SQLException, RepositoryException{
        return chats.save(chat);
    }
    public IndividualChat deleteChat(String ID) throws SQLException, RepositoryException{
        return chats.delete(ID);
    }

    public IndividualChat findChat(String ID) throws SQLException, RepositoryException{
        return chats.findOne(ID);
    }

    public Iterable<IndividualChat> getAllChatsForUser(User user) throws SQLException, RepositoryException{
        return chats.findAllChatsForUser(user);
    }

    //messages controller part

    public Long addMessage(Message message) throws ValidationException, SQLException, RepositoryException{
        return messages.save(message);
    }

    public Iterable<Message> findAllMessagesForChat(IndividualChat chat) throws SQLException{
        return messages.getMessagesForConversation(chat);
    }

    public Message getLastMessageForChat(IndividualChat chat) throws SQLException, RepositoryException{
        return messages.getLastMessageForChat(chat);
    }

}
