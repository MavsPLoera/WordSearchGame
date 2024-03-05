package uta.cse3310;

import org.java_websocket.WebSocket;

public class User {
    public String name;
    // 0 outside of a game
    public int color;
    // 0 outside of a game
    public int score;
    public int gameScore;
    // null when user is disconnected
    public WebSocket socket;
    // null when not playing a game
    public Game currentGame;

    public void addToScore(int value) {

    }

    public void addToGameScore(int value) {

    }
}
