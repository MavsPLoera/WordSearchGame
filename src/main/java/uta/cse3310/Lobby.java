package uta.cse3310;

import java.util.ArrayList;

public class Lobby {
    public ArrayList<User> usersInLobby;
    public int playerSize;

    private boolean sufficientPlayers() {
        //create new game and add players in player list to lobby
        
        if(usersInLobby.size() > playerSize){
            Game newinstance = new Game(usersInLobby);
            activeGames.add(newinstance);
            //Need to add to active games in App

            usersInLobby.clear();
            return true;
        }

        //implementsome way to update frontend somehow here 
        
        return false;
    }
    void adduser(User user){
        usersInLobby.add(user);
        sufficientPlayers();
        // have a check to make sure we arent alreayd in lobby
        return;
    }
    void removeUser(){
        return;
    }
}
