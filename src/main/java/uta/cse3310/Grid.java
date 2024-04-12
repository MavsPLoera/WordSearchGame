package uta.cse3310;

import java.util.ArrayList;
import java.util.Random;

public class Grid {
    public String[] wordList;
    public WordLocation[] wordIndices;
    public GridItem[][] grid;
    public float[] randomness;
    public int minimumRandomChar;
    public int maximumRandomChar;
    public ArrayList<String> addwordList;//convert to string array later 
    public ArrayList<WordLocation> wordLocations;//convert to WordLocation array late

    private Random random = new Random();
    //long seed = 1234567L;                    //Generates a consitant for repeatablility of bugs
    //private Random random = new Random(seed);//
    
    {
        addwordList = new ArrayList<>();
        wordLocations = new ArrayList<>();
    }
    private enum DIRECTION {
        VERTICALUP, DIAGONALUPRIGHT, HORIZONTALRIGHT, DIAGONALDOWNRIGHT, VERTICALDOWN, DIAGONALDOWNLEFT, HORIZONTALLEFT, DIGAONALUPLEFT
    }

    private Grid() {}

    public static Grid createGrid(int rowNumber, int columnNumber) {

        var grid = new Grid();
        grid.grid = new GridItem[rowNumber][columnNumber];
        // Initialize each grid item with a space characterx
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                grid.grid[i][j] = new GridItem(' '); 
            }
        }


        return grid;
    }

    public boolean addWord(String word) {
        int attempts = 100;  // Limit the number of placement attempts
        while (attempts-- > 0) {
            int row = random.nextInt(grid.length);
            int col = random.nextInt(grid[0].length);

            int directionIndex = random.nextInt(8);  

            DIRECTION[] directions = DIRECTION.values(); 
            DIRECTION direction = directions[directionIndex];

            if (canPlaceWord(word, row, col, direction)) {
                placeWord(word, row, col, direction);
                addwordList.add(word);
                return true;
            }
        }
        return false;  
    }

    private void placeWord(String word, int row, int col, DIRECTION direction) {
        Point start = new Point(col, row);
        Point end = null; 
        for (int i = 0; i < word.length(); i++) {
            int currentRow = row;
            int currentCol = col;

            switch (direction) {
                case HORIZONTALRIGHT:
                    currentCol += i;
                    break;
                case HORIZONTALLEFT:
                    currentCol -= i;
                    break;
                case VERTICALDOWN:
                    currentRow += i;
                    break;
                case VERTICALUP:
                    currentRow -= i;
                    break;
                case DIAGONALDOWNRIGHT:
                    currentRow += i;
                    currentCol += i;
                    break;
                case DIAGONALUPRIGHT:
                    currentRow -= i;
                    currentCol += i;
                    break;
                case DIAGONALDOWNLEFT:
                    currentRow += i;
                    currentCol -= i;
                    break;
                case DIGAONALUPLEFT:
                    currentRow -= i;
                    currentCol -= i;
                    break;
            }

            grid[currentRow][currentCol].letter = word.charAt(i);
            end = new Point(currentCol, currentRow);
        }
        if (end != null) {
            wordLocations.add(new WordLocation(start, end));
            // y means row 
            // x mean col
        }
    }
    
    private boolean canPlaceWord(String word, int row, int col, DIRECTION direction) {
        int len = word.length();
    
        // Check bounds based on direction
        switch (direction) {
            case HORIZONTALRIGHT:
                if (col + len > grid[0].length) return false;
                break;
            case HORIZONTALLEFT:
                if (col - len < -1) return false;
                break;
            case VERTICALDOWN:
                if (row + len > grid.length) return false;
                break;
            case VERTICALUP:
                if (row - len < -1) return false;
                break;
            case DIAGONALDOWNRIGHT:
                if (row + len > grid.length || col + len > grid[0].length) return false;
                break;
            case DIAGONALUPRIGHT:
                if (row - len < -1 || col + len > grid[0].length) return false;
                break;
            case DIAGONALDOWNLEFT:
                if (row + len > grid.length || col - len < -1) return false;
                break;
            case DIGAONALUPLEFT:
                if (row - len < -1 || col - len < -1) return false;
                break;
        }

        
        // Check for letter conflicts
        for (int i = 0; i < len; i++) {
            int currentRow = row;
            int currentCol = col;

            switch (direction) {
                case HORIZONTALRIGHT:
                    currentCol += i;
                    break;
                case HORIZONTALLEFT:
                    currentCol -= i;
                    break;
                case VERTICALDOWN:
                    currentRow += i;
                    break;
                case VERTICALUP:
                    currentRow -= i;
                    break;
                case DIAGONALDOWNRIGHT:
                    currentRow += i;
                    currentCol += i;
                    break;
                case DIAGONALUPRIGHT:
                    currentRow -= i;
                    currentCol += i;
                    break;
                case DIAGONALDOWNLEFT:
                    currentRow += i;
                    currentCol -= i;
                    break;
                case DIGAONALUPLEFT:
                    currentRow -= i;
                    currentCol -= i;
                    break;
            }
            //when looking for somewhere to put the word we will check its it empty or the letter is the same as what we need
            if (grid[currentRow][currentCol].letter != ' ' && grid[currentRow][currentCol].letter != word.charAt(i)) {
                return false; 
            }
        }

        return true; // No conflicts; the word can be placed
    }

    public void fillEmptySpaces() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].letter == ' ') {  // Assuming ' ' represents an empty space
                    grid[i][j].letter = '_';  // Fill with _ for debugging
                    char randomChar = (char) (random.nextInt(26) + 'a');
                    grid[i][j].letter = randomChar;
                }
            }
        }
    }
    
    public void printGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j].letter + " "); 
            }
            System.out.println(); 
        }
        System.out.println(addwordList.size()); 
        System.out.println(addwordList.toString()); 
        System.out.println("\nWord Locations:");
        for (WordLocation location : wordLocations) {
            System.out.println("Word at (" + location.start.x + ", " + location.start.y + 
                               ") to (" + location.end.x + ", " + location.end.y + ")");
        }

    }

    public String checkStartEnd(Point start, Point end) {
        for(int i = 0; i < addwordList.size(); i++) {
            //Check for forward input and backward input of the word. Will automatically fail if the word has already been found. (Becareful the wordIndices being checked match up with the word potentially being sent.)
            if((grid[wordIndices[i].start.x][wordIndices[i].start.y].foundBy.size() == 0) && ((start.x == wordIndices[i].start.x) && (start.y == wordIndices[i].start.y)) && ((end.x == wordIndices[i].end.x) && (end.y == wordIndices[i].end.y)))
                return wordList[i];

            if((grid[wordIndices[i].start.x][wordIndices[i].start.y].foundBy.size() == 0) && ((end.x == wordIndices[i].start.x) && (end.y == wordIndices[i].start.y)) && ((start.x == wordIndices[i].end.x) && (start.y == wordIndices[i].end.y)))
                return wordList[i];
        }
        return null;
    }

    public void wordFound(Point start, Point end, int color) {
        Point temp = new Point(); //Temp point if needed.

        switch(checkDirection(start, end)) {
            case VERTICALUP:
                //Swap start and end then iterate over y-cord upwards and highlight + setFound for each grid letter
                temp = end;
                end = start;
                start = temp;

                for(int y_cord = start.y; y_cord >= end.y; y_cord--) {
                    //start.x and end.x should be the same
                    grid[start.x][y_cord].foundBy.add(color);
                }

                //Might need to swap back start end
                start = end;
                end = temp;
                break;
            case DIAGONALUPRIGHT:
                //Iterate over x + y-cord upwards and highlight + setFound for each grid letter
                for(int x_cord = start.x; x_cord <= end.x; x_cord++) {
                    for(int y_cord = start.y; y_cord <= end.y; y_cord++) {
                        grid[x_cord][y_cord].foundBy.add(color);
                    }
                }
                break;
            case HORIZONTALRIGHT:
                //Iterate over x-cord upwards and highlight + setFound for each grid letter
                for(int x_cord = start.x; x_cord <= end.x; x_cord++) {
                    //start.y should be the same as end.y
                    grid[x_cord][start.y].foundBy.add(color);
                }
                break;
            case DIAGONALDOWNRIGHT:
                //Iterate over positive x-cord and negative y-cord
                for(int x_cord = start.x; x_cord <= end.x; x_cord++) {
                    for(int y_cord = start.y; y_cord >= end.y; y_cord--) {
                        grid[x_cord][y_cord].foundBy.add(color);
                    }
                }
                break;
            case VERTICALDOWN:
                //iterate over y-cord upwards and highlight + setFound for each grid letter
                for(int y_cord = start.y; y_cord >= end.y; y_cord--) {
                    //start.x and end.x should be the same
                    grid[start.x][y_cord].foundBy.add(color);
                }
                break;
            case DIAGONALDOWNLEFT:
                //swap start and end, Iterate over x + y-cord upwards and highlight + setFound for each grid letter
                temp = end;
                end = start;
                start = temp;

                for(int x_cord = start.x; x_cord <= end.x; x_cord++) {
                    for(int y_cord = start.y; y_cord <= end.y; y_cord++) {
                        grid[x_cord][y_cord].foundBy.add(color);
                    }
                }

                //Might need to swap back start end
                start = end;
                end = temp;
                break;
            case HORIZONTALLEFT:
                //Swap start and end, Iterate over positive x-cord and highlight
                temp = end;
                end = start;
                start = temp;

                for(int x_cord = start.x; x_cord <= end.x; x_cord++) {
                    for(int y_cord = start.y; y_cord <= end.y; y_cord++) {
                        grid[x_cord][y_cord].foundBy.add(color);
                    }
                }
                
                //Might need to swap back start end
                start = end;
                end = temp;
                break;
            case DIGAONALUPLEFT:
                //swap start and end, Iterate over positive x-cord and negative y-cord
                temp = end;
                end = start;
                start = temp;
                
                for(int x_cord = start.x; x_cord <= end.x; x_cord++) {
                    for(int y_cord = start.y; y_cord >= end.y; y_cord--) {
                        grid[x_cord][y_cord].foundBy.add(color);
                    }
                }
                
                //Might need to start and end
                start = end;
                end = temp;
                break;
            default:
                System.out.println("ERROR IN CHECK DIRECTION");
        }
    }

    private DIRECTION checkDirection(Point start, Point end) {
        //Get slope. Uses the start as a pivot point.
        int x_points = end.x -start.x;
        int y_points = end.y - start.y;

        //Check slope
        if(x_points == 0 && y_points >= 1) {
            return DIRECTION.VERTICALUP;
        }
        else if(x_points >= 1 && y_points >= 1) {
            return DIRECTION.DIAGONALUPRIGHT;
        }
        else if(x_points >= 1 && y_points == 0) {
            return DIRECTION.HORIZONTALRIGHT;
        }
        else if(x_points >= 1 && y_points <= -1) {
            return DIRECTION.DIAGONALDOWNRIGHT;
        }
        else if(x_points == 0 && y_points <= -1) {
            return DIRECTION.VERTICALDOWN;
        }
        else if(x_points <= -1 && y_points <= -1) {
            return DIRECTION.DIAGONALDOWNLEFT;
        }
        else if(x_points <= -1 && y_points == 0) {
            return DIRECTION.HORIZONTALLEFT;
        }
        else {
            return DIRECTION.DIGAONALUPLEFT;
        }
    }

    public void addSelection(Point point, int color) {
        grid[point.x][point.y].selectedBy.add(color);
    }

    public void removeSelection(Point point, int color) {
        //Get size of the selectedBy arrayList. If the color (number) is in the list remove.
        for(int i = 0; i < grid[point.x][point.y].selectedBy.size(); i++) {
            if(grid[point.x][point.y].selectedBy.get(i) == color)
                grid[point.x][point.y].selectedBy.remove(i);
        }
    }

    //Used for gameover seqeucne if result is true.
    public boolean checkWordList() {
        int wordsFound = 0;

        //Iterate through all start points of the valid words and check if are found by atleast 1 player. Might be some weird edge cases but otherwise should be fine.
        for(int i = 0; i < wordIndices.length; i++) {      
            if(grid[wordIndices[i].start.x][wordIndices[i].start.y].foundBy.size() == 0)
                wordsFound++;
        }
        
        //Case of all words have been found.
        if(wordsFound == wordList.length)
            return true;
            
        return false;
    }

    public boolean checkWordFound(int numChoice) { 
        if(grid[wordIndices[numChoice].start.x][wordIndices[numChoice].start.y].foundBy.size() == 0)
            return true;

        return false;
    }
    
}
