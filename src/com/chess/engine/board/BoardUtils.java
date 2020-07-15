package com.chess.engine.board;

public class BoardUtils {

    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;


    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHT_COLUMN = initColumn(7);

    //Initialize boolean array and set true to particular columns
    private static boolean[] initColumn(int column) {
        final boolean[] col = new boolean[NUM_TILES];
        do{
            col[column] = true;
            column += NUM_TILES_PER_ROW;
        }while(column<NUM_TILES);

        return col;
    }



    public static boolean isValidTileCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate < 64;
    }
}
