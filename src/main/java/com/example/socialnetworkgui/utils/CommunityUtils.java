package com.example.socialnetworkgui.utils;

import com.example.socialnetworkgui.models.account.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommunityUtils {


    /**
     * Returns the connected component (list of users) that has the longest path from a graph
     *
     * @param users all the nodes(users)
     * @return the community that has the longest path(list of users)
     */
    public static List<User> mostSociableCommunity(Iterable<User> users) {
        List<List<User>> communities = discoverCommunities(users);
        List<User> result = null;
        int maximumLength = -1;

        for (List<User> community : communities) {
            int longestPathCommunity = longestPathCommunity(community);
            if (longestPathCommunity > maximumLength) {
                result = community;
                maximumLength = longestPathCommunity;
            }
        }

        return result;
    }

    /**
     * Calculates the longest path in a connected component
     *
     * @param community the connected component
     * @return the length of the longest path
     */
    private static int longestPathCommunity(List<User> community) {
        int maximumLength = -1;
        for (User user : community) {
            HashMap<User, Boolean> visitedUsers = new HashMap<>();
            for (User communityMember : community) {
                visitedUsers.put(communityMember, false);
            }
            int length = dfsLength(user, visitedUsers, 0, 0);
            if (length > maximumLength)
                maximumLength = length;
        }
        return maximumLength;
    }

    /**
     * Calculates the longest path from a node in a connected component
     *
     * @param user          the source node
     * @param visitedUsers  a map with already visited users
     * @param i             the current length
     * @param maximumLength the maximum reached length
     * @return the maximum length from the source node(user)
     */
    private static int dfsLength(User user, HashMap<User, Boolean> visitedUsers, int i, int maximumLength) {
        visitedUsers.put(user, true);
        if (i > maximumLength)
            maximumLength = i;
        for (User friend : user.allFriends()) {
            if (!visitedUsers.get(friend)) {
                return dfsLength(friend, visitedUsers, i + 1, maximumLength);
            }
        }
        return maximumLength;
    }

    /**
     * Discovers all the connected components(list of users) from a graph
     *
     * @param users all the nodes(node = user)
     * @return a list with all the connected components
     */
    public static List<List<User>> discoverCommunities(Iterable<User> users) {
        List<List<User>> result = new ArrayList<>();
        HashMap<User, Boolean> visitedUsers = new HashMap<>();
        for (User user : users)
            visitedUsers.put(user, false);

        for (User user : users) {
            if (!visitedUsers.get(user)) {
                List<User> communityList = new ArrayList<>();
                result.add(DFSUtil(user, visitedUsers, communityList));
            }
        }
        return result;
    }

    /**
     * Discovers all the nodes that are part of the same connected component with the source node(user)
     *
     * @param user         the source node
     * @param visitedUsers a map with visited users
     * @param result       the discovered connected component
     * @return the discovered connected component from the source node(user)
     */
    private static List<User> DFSUtil(User user, HashMap<User, Boolean> visitedUsers, List<User> result) {
        result.add(user);
        visitedUsers.put(user, true);
        for (User friend : user.allFriends()) {
            if (!visitedUsers.get(friend)) {
                DFSUtil(friend, visitedUsers, result);
            }
        }
        return result;
    }
}