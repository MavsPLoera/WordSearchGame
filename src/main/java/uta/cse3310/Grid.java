package uta.cse3310;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Grid {
    public String[] wordList;
    public WordLocation[] wordIndices;
    public GridItem[][] grid;
    public Duration timeToCreate;
    public float density;
    public float[] randomness;
    public int minimumRandomChar;
    public int maximumRandomChar;

    private enum DIRECTION {
        VERTICALUP, DIAGONALUPRIGHT, HORIZONTALRIGHT, DIAGONALDOWNRIGHT, VERTICALDOWN, DIAGONALDOWNLEFT, HORIZONTALLEFT, DIGAONALUPLEFT
    }

    private Grid() {}

    public static Grid createGrid(int rowNumber, int columnNumber) {
        var startingTime = Instant.now();

        var grid = new Grid();
        grid.grid = new GridItem[rowNumber][columnNumber];


        grid.timeToCreate = Duration.between(startingTime, Instant.now());

        return grid;
    }

    public String checkStartEnd(Point start, Point end) {
        for(int i = 0; i < wordList.length; i++) {
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

    public boolean checkWordList()
    {
        int wordsFound = 0;

        //Iterate through all start points of the valid words and check if are found by atleast 1 player. Might be some weird edge cases but otherwise should be fine.
        for(int i = 0; i < wordIndices.length; i++) {
            //Point samplePoint = new Point();
            //samplePoint.x = wordIndices[i].start.x;
            //samplePoint.y = wordIndices[i].start.y;
            
            if(grid[wordIndices[i].start.x][wordIndices[i].start.y].foundBy.size() == 0)
                wordsFound++;
        }
        
        //Case of all words have been found.
        if(wordsFound == wordList.length)
            return true;
            
        return false;
    }
    
}
