package com.chess.engine.board;

public class BoardUtils {

    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;


    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHT_COLUMN = initColumn(7);

    public static final boolean[] EIGHT_RANK = initRow(0);
    public static final boolean[] SEVENTH_RANK = initRow(8);
    public static final boolean[] SIXTH_RANK = initRow(16);
    public static final boolean[] FIFTH_RANK = initRow(24);
    public static final boolean[] FOURTH_RANK = initRow(32);
    public static final boolean[] THIRD_RANK = initRow(40);
    public static final boolean[] SECOND_RANK = initRow(48);
    public static final boolean[] FIRST_RANK = initRow(56);

    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[NUM_TILES];

        do{
            row[rowNumber] = true;
            rowNumber++;
        }while(rowNumber % NUM_TILES_PER_ROW != 0);

        return row;
    }

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
