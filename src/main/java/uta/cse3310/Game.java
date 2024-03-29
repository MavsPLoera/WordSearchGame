package uta.cse3310;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
    public Timer GameStart;
    public ArrayList<User> players;
    public Grid grid;
    public int totalGameTime = 300; //Timer set for 5 minutes

    public Game(ArrayList<User> lobby)
    {
        players = new ArrayList<User>(lobby);

        //Set colors
        for(int i = 0; i < players.size(); i++)
        {
            var player = players.get(i);
            player.color = i + 1;
        }

        grid = Grid.createGrid(20, 20);

        //Set up timers. Need to think about case of when all the words are found.
        GameStart = new Timer();

        TimerTask task = new TimerTask() {

            @Override
            public void run(){    //Prevent gameover from running twice
                if(totalGameTime == 0) //add something that checks || wordList == 0
                    gameOver();
                    GameStart.cancel();

                if(((totalGameTime % 30) == 0) && (totalGameTime != 300))
                    displayHint();

                System.out.println("Countdown timer: ");
                totalGameTime--;   
            }
        };

        //Count down timer for Game
        GameStart.scheduleAtFixedRate(task, 0 , 1000);
    }

    public void gameOver(){
        User temp = new User(); //store player into temp User
        for(int i = 0; i < players.size(); i++)
        {
            temp = players.get(i);
            temp.currentGame = null;
            temp.addGameScoreToTotalScore();
        }
    }

    public void displayHint() {
        //Find word in the current word list
        //Call addselection 
    }

    public void validateAttempt(User attempter, Point start, Point end) {
        String wordFound = grid.checkStartEnd(start , end);

        if(wordFound != null)
        {
            attempter.addToCurrentScore(wordFound.length());

            //Removes word from the wordlist and changes the word to "is found"
            grid.wordFound(start, end, attempter.color);
        }
        else
        {
            //Might need to add another remove selection for the newly selected point
            grid.removeSelection(start, attempter.color);
            attempter.selectedPoint = null;
        }
    }

    public void input(User user, Point selection) {
        //Change point in grid to selected/unselected
        //Need to check if the user.selectedPoint == the same Point
        System.out.println("Received input from Player " + user.name + " at point " + "( " + selection.x + ", " + selection.y + ")." );

        if(user.selectedPoint != null)
        {
            //point comes from the user. Assumed that the user has already made a selection
            System.out.println("Validating user: " + user.name + "selected points" + "( " + user.selectedPoint.x + ", " + user.selectedPoint.y + ") " + "and " + "( " + selection.x + ", " + selection.y + ").");
            validateAttempt(user, user.selectedPoint, selection);
            user.selectedPoint = null;
        }
        else
        {
            System.out.println("Highlighting point" + "( " + selection.x + ", " + selection.y + ") with the color " + user.color + "." );
            grid.addSelection(selection, user.color);
            user.selectedPoint = selection;
        }
    }
}
