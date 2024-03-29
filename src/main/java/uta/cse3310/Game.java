package uta.cse3310;

import java.time.Instant;
import java.util.ArrayList;

public class Game {
    public Instant timeOfGameStart;
    public Instant timeOfLastFind;
    public ArrayList<User> players;
    public Grid grid;

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

        //Set up timers
    }

    public void displayHint() {

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
            System.out.println("Validating users " + user.name + "selected points" + "( " + user.selectedPoint.x + ", " + user.selectedPoint.y + ") " + "and " + "( " + selection.x + ", " + selection.y + ").");
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
