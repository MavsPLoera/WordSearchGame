package uta.cse3310;

import org.java_websocket.WebSocket;

public class User {
    public String name;
    // 0 outside of a game
    public int color;
    public int currentGameScore;
    public int totalScore;
    // null when user is disconnected
    public WebSocket socket;
    // null when not playing a game
    public Game currentGame;
    public Point selectedPoint;

    public void addToCurrentScore(int score){
        currentGameScore += score;
    }
    
    public void addTogameScore(int value) {
        currentGameScore += value; 
    }
    public void SetColor(int color){
        color = this.color;
    }
    public void addToTotalScore(){
        totalScore += currentGameScore;
        currentGameScore = 0;
    }

}
