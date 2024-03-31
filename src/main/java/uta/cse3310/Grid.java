package uta.cse3310;

import java.time.Duration;
import java.time.Instant;

public class Grid {
    public String[] wordList;
    public WordLocation[] wordIndices;
    public GridItem[][] grid;
    public Duration timeToCreate;
    public float density;
    public float[] randomness;
    public int minimumRandomChar;
    public int maximumRandomChar;

    private Grid() {}

    public static Grid createGrid(int rowNumber, int columnNumber) {
        var startingTime = Instant.now();

        var grid = new Grid();
        grid.grid = new GridItem[rowNumber][columnNumber];


        grid.timeToCreate = Duration.between(startingTime, Instant.now());

        return grid;
    }

    public String checkStartEnd(Point start, Point end) {
        return " ";
    }

    public void wordFound(Point start, Point end, int color) {

    }

    public void addSelection(Point point, int color) {

    }

    public void removeSelection(Point point, int color) {

    }
}
