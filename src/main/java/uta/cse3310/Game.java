package uta.cse3310;

import java.util.ArrayList;

import uta.cse3310.Events.EventHolder;

import java.time.Duration;
import java.time.Instant;

public class Game {
    public ArrayList<User> players;
    public Grid grid;
    public int totalGameTime = 300; //Timer set for 5 minutes
    public boolean gameOver = false;
    public static int gridSize = 20; 
    public static int totalCells = gridSize * gridSize;
    public static int wordCountLimit = 30; // max total word in the grid or 60% whichever is first 
    public static double maxDensity = 0.6;  //  maximum density (60%)

    public Game(ArrayList<User> lobby)
    {
        var startingTime = Instant.now();
        int placedWordsCount = 0;
        int filledCells = 0;
        players = new ArrayList<User>(lobby);

        //Set colors
        for(int i = 0; i < players.size(); i++)
        {
            var player = players.get(i);
            player.setColor(i + 1);
            System.out.println("did the thing");
            player.currentGame = this;
        }

        grid = Grid.createGrid(20, 20);

        while (true) {
            var word = App.wordsfromfile.get(grid.random.nextInt(App.wordsfromfile.size()));

            if (placedWordsCount >= wordCountLimit || (double)filledCells / totalCells > maxDensity) {
                break;  // Stop adding words if limit or max density is reached
            }

            boolean added = grid.addWord(word);
            if (added) {
                placedWordsCount++;
                filledCells += word.length();
            } else {
                System.out.println("Failed to add word: " + word);
            }
        }
        
        grid.fillEmptySpaces();
        Duration timeToCreate = Duration.between(startingTime, Instant.now());
        System.out.println(timeToCreate.toString());
        
        grid.printGrid();
    }

    public void gameOver() {
        //Add scores from game to each Users totalScore. Signal gameOver for App to remove the game.
        for(User temp: players)
        {
            temp.addGameScoreToTotalScore();
            temp.currentGame = null;
        }
        gameOver = true;
    }

    public void displayHint() {
        //Chooses random number between 0 to grid.wordList.length
        int random = (int)(Math.random() + (grid.wordIndices.size()));
        GridItem checkSelected = grid.grid[grid.wordIndices.get(random).start.x][grid.wordIndices.get(random).start.y];

        //Highlights start of a word. Might need to check if the word is currently selected by to get a unique word not selected by a player.
        while(checkSelected.foundBy.size() != 0) {
            random = (int)(Math.random() + (grid.wordIndices.size()));
        }
        grid.addSelection( grid.wordIndices.get(random).start, 5); //5 will be the hint color
    }

    public void validateAttempt(User attempter, Point start, Point end) {
        WordLocation wordFound = grid.checkStartEnd(start , end);

        if(wordFound != null)
        {
            attempter.addToCurrentScore(wordFound.word.length());

            for (var item : wordFound.letters) {
                item.foundBy.add(attempter.color);
            }

            grid.wordIndices.remove(wordFound);
        }

        //Remove selection from word grid.
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
        sendUpdate();
    }

    public void sendUpdate() {
        var event = App.gson.toJson(new EventHolder<>("GameResponse", this));
        for (var user : players) {
            user.socket.send(event);
        }
    }

    //Will be used for one timer that is implemented in App. Might have issue with the game time being negative or what to do with a game when it is finished.
    public void tick() {
        if(totalGameTime <= 0 || grid.checkWordList()) { 
            gameOver();
            sendUpdate();
        }
        else if(((totalGameTime % 30) == 0) && (totalGameTime != 300)) {
            displayHint();
            sendUpdate();
        }

        totalGameTime--; 
    }
}
