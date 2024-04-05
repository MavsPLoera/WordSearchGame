package uta.cse3310;

import java.util.ArrayList;

public class Game {
    public ArrayList<User> players;
    public Grid grid;
    public int totalGameTime = 300; //Timer set for 5 minutes
    public boolean gameOver = false;

    public Game(ArrayList<User> lobby)
    {
        players = new ArrayList<User>(lobby);

        //Set colors
        for(int i = 0; i < players.size(); i++)
        {
            var player = players.get(i);
            player.setColor(i + 1);
        }

        grid = Grid.createGrid(20, 20);
    }

    public void gameOver() {
        for(User temp: players)
        {
            temp.addGameScoreToTotalScore();
            temp.currentGame = null;
        }
        gameOver = true;
    }

    public void displayHint() {
        //Find word in the current word list
        //Call addselection
        //Change 0 to a random number generator between 0 and the size of wordList
        //Not finished but example on how to call
        //Needs to take into account words that have been found or not.

        //Chooses random number between 0 to grid.wordList.length
        int random = (int)(Math.random() + (grid.wordList.length));
        grid.addSelection( grid.wordIndices[random].start, 5); //5 will be the hint color
    }

    public void validateAttempt(User attempter, Point start, Point end) {
        String wordFound = grid.checkStartEnd(start , end);

        if(wordFound != null)
        {
            attempter.addToCurrentScore(wordFound.length());

            //Removes word from the wordlist and changes the word to "is found"
            grid.wordFound(start, end, attempter.color);
        }

        //Might not need the removeSelection in the else can just have it after the if and let wordFound not remove the selection
        grid.removeSelection(start, attempter.color);
    }

    public void input(User user, Point selection) {
        //Change point in grid to selected/unselected
        System.out.println("Received input from Player " + user.name + " at point " + "(" + selection.x + ", " + selection.y + ")." );

        if(user.selectedPoint != null)
        {
            //point comes from the user. Assumed that the user has already made a selection
            System.out.println("Validating user: " + user.name + " selected points" + "(" + user.selectedPoint.x + ", " + user.selectedPoint.y + ") " + "and " + "(" + selection.x + ", " + selection.y + ").");
            validateAttempt(user, user.selectedPoint, selection);
            user.selectedPoint = null;
        }
        else
        {
            System.out.println("Highlighting point" + "(" + selection.x + ", " + selection.y + ") with the color " + user.color + "." );
            grid.addSelection(selection, user.color);
            user.selectedPoint = selection;
        }
    }

    //Will be used for one timer that is implemented in App. Might have issue with the game time being negative.
    public void tick() {
        if(totalGameTime == 0 || grid.checkWordList()) //add something that checks || wordList == 0
            gameOver();

        if(((totalGameTime % 30) == 0) && (totalGameTime != 300))
            displayHint();

        totalGameTime--; 
    }
}
