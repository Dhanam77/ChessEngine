package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Tile {
    private final int tileCoordinate;
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    private Tile(final int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }


    public int getTileCoordinate(){
        return this.tileCoordinate;
    }

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTilesMap = new HashMap<>();
        for(int i = 0;i < BoardUtils.NUM_TILES; i++){
            emptyTilesMap.put(i,new EmptyTile(i));
        }
        //Unmodifiable map so that no one can clear/add elements
        return Collections.unmodifiableMap(emptyTilesMap);
    }

    //Declared abstract methods because each subclass will have different implementation
    //Since the methods are abstract, it must be implemented in subclasses
    public abstract boolean isTileOccupied();
    public abstract Piece getPiece();


    public static Tile createTile(final int tileCoordinate, final Piece piece){
        return piece != null?new OccupiedTile(tileCoordinate, piece): new EmptyTile(tileCoordinate);
    }

    //Final class so it cant be extended
    public static final class EmptyTile extends Tile{

        private EmptyTile(final int tileCoordinate){
            super(tileCoordinate);  //Calls constructor of parent class
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;    //Since it is empty tile
        }

        //Empty tiles are represented as "-"
        @Override
        public String toString(){
            return "-";
        }
    }

    //Final class so it cant be extended
    public static final class OccupiedTile extends Tile {

        //Since, occupied tile, we take the piece on this tile
        private final Piece pieceOnTile;

        private OccupiedTile(int tileCoordinate,final Piece pieceOnTile){
            super(tileCoordinate);  //Calls constructor of parent class
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public String toString(){
            //Black pieces will be denoted in lower case
            return getPiece().getPieceAlliance().isBlack()? getPiece().toString().toLowerCase():getPiece().toString();
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return pieceOnTile;
        }
    }


}
