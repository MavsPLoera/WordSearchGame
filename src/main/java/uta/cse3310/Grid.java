package uta.cse3310;

import java.time.Duration;

public class Grid {
    public String[] wordList;
    public WordLocation[] wordIndices;
    public char[][] grid;
    public Duration timeToCreate;
    public float density;
    public float[] randomness;
    public int minimumRandomChar;
    public int maximumRandomChar;

    public static Grid createGrid(int size) {
        throw new UnsupportedOperationException();
    }

    public boolean checkStartEnd(Point start, Point end) {
        throw new UnsupportedOperationException();
    }
}
