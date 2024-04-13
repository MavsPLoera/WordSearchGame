package uta.cse3310;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Grid {
    public ArrayList<WordLocation> wordIndices = new ArrayList<>();
    public GridItem[][] grid;
    public float[] randomness;
    public int minimumRandomChar;
    public int maximumRandomChar;

    private static final long seed = 1234567L;                    //Generates a consitant for repeatablility of bugs
    public static boolean useSeed = false;
    public transient Random random = useSeed ? new Random(seed) : new Random();
    //private Random random = new Random(seed);//

    static {
        if (System.getenv("USE_SEED") != null)
            useSeed = true;
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

            Direction[] directions = Direction.values(); 
            Direction direction = directions[directionIndex];

            if (canPlaceWord(word, row, col, direction)) {
                var location = new WordLocation(word, new Point(row, col), null, direction);
                placeWord(word, row, col, direction, location);
                wordIndices.add(location);
                return true;
            }
        }
        return false;  
    }

    private void placeWord(String word, int row, int col, Direction direction, WordLocation location) {
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

            var item = grid[currentRow][currentCol];
            item.letter = word.charAt(i);
            location.letters.add(item);
            location.end = new Point(currentRow, currentCol);
        }
    }
    
    private boolean canPlaceWord(String word, int row, int col, Direction direction) {
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
        int counter = 0;
        ArrayList<Character> alphabet = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            alphabet.add(c);
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].letter == ' ') {  // Assuming ' ' represents an empty space
                    char randomChar = alphabet.get(random.nextInt(20));

                    grid[i][j].letter = randomChar;
                    if(counter++ < 26){
                       Collections.shuffle(alphabet,random);
                       counter = 0 ;
                    }
                    grid[i][j].letter = '_';  // Fill with _ for debugging

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
        System.out.println("\nWord Locations:");
        for (WordLocation location : wordIndices) {
            System.out.println("Word at (" + location.start.x + ", " + location.start.y + 
                               ") to (" + location.end.x + ", " + location.end.y + ")");
        }

    }

    public WordLocation checkStartEnd(Point start, Point end) {
        for (int i = 0; i < wordIndices.size(); i++) {
            var word = wordIndices.get(i);
            if (start.equals(word.start) && end.equals(word.end)) {
                return wordIndices.get(i);
            }
        }
        return null;
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
        return wordIndices.size() == 0;
    }
    
}
