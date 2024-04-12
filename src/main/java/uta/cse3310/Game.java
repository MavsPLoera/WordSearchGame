package uta.cse3310;

import java.util.ArrayList;

public class Game {
    public ArrayList<User> players;
    public Grid grid;
    public int totalGameTime = 300; //Timer set for 5 minutes
    public boolean gameOver = false;
    public static int gridSize = 20; 
    public static int totalCells = gridSize * gridSize;
    public static int wordCountLimit = 5; // max total word in the grid or 60% whichever is first 
    public static double maxDensity = 0.6;  //  maximum density (60%)

    public Game(ArrayList<User> lobby)
    {
        int placedWordsCount = 0;
        int filledCells = 0;
        players = new ArrayList<User>(lobby);

        //Set colors
        for(int i = 0; i < players.size(); i++)
        {
            var player = players.get(i);
            player.setColor(i + 1);
        }

        grid = Grid.createGrid(20, 20);

        for (String word : App.wordsfromfile) {
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
        int random = (int)(Math.random() + (grid.wordList.length));

        //Will only highlight the start of the word as of now.
        while(grid.checkWordFound(random) != true) {
            random = (int)(Math.random() + (grid.wordList.length));
        }
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
    }

    //Will be used for one timer that is implemented in App. Might have issue with the game time being negative or what to do with a game when it is finished.
    public void tick() {
        if(totalGameTime == 0 || grid.checkWordList())
            gameOver();

        if(((totalGameTime % 30) == 0) && (totalGameTime != 300))
            displayHint();

        totalGameTime--; 
    }
}
