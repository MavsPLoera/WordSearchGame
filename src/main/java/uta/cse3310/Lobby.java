package uta.cse3310;

import java.util.ArrayList;

public class Lobby {
    public ArrayList<User> usersInLobby = new ArrayList<>();
    public int playerSize;

    public Lobby(int size) {
        playerSize = size;
    }

    private boolean sufficientPlayers() {
        //create new game and add players in player list to lobby
        
        if(usersInLobby.size() >= playerSize){
            Game newinstance = new Game(usersInLobby);
            //Need to add to active games in App. might just need to return the bool then create game in App

            usersInLobby.clear();
            return true;
        }

        //implementsome way to update frontend somehow here 
        
        return false;
    }
    void addUser(User user) {
        usersInLobby.add(user);
        sufficientPlayers();
        // have a check to make sure we arent already in lobby
    }
    void removeUser(User user) {
        usersInLobby.remove(user);
    }
}
