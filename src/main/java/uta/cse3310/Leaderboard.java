package uta.cse3310;

import java.util.ArrayList;

public class Leaderboard {
    private ArrayList<LeaderboardUser> leaderboardList;

    public int getScore(String name) {
        throw new UnsupportedOperationException();
    }
    public int getLowestScore(){
        int lowestScore = 0 ;
        for (int i = 0; i < leaderboardList.size(); i++){
            LeaderboardUser tempuser = leaderboardList.get(i);
            if(lowestScore < tempuser.score){
                lowestScore = tempuser.score;
            }
        }
        return lowestScore;
    }

    public void addScore(String name, int value) {
        LeaderboardUser tempLeaderboardUser = new LeaderboardUser(name, value);
        leaderboardList.add(tempLeaderboardUser);

    }

    public void removeScore(String name, int value) {
        LeaderboardUser tempLeaderboardUser = new LeaderboardUser(name, value);
        leaderboardList.remove(tempLeaderboardUser);
        
    }

    public LeaderboardUser[] getTopUsers(int count) {
        throw new UnsupportedOperationException();
    }
}
