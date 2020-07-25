package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.Collection;
import java.util.List;

//Abstract, so cant be instantiated
public abstract class Piece {

    //Each piece has a position and an alliance (black or white)
    protected final int piecePosition;
    protected final Alliance pieceAlliance;

    //Check if it's first move of the piece
    //Required in cases of castling and pawn jump
    protected final boolean isFirstMove;

    //Get piecetype
    protected final PieceType pieceType;

    //HashCode for each piece
    private final int cachedHashCode;

    Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance){
        this.pieceType = pieceType;
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        isFirstMove = false;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31*result + pieceAlliance.hashCode();
        result = 31*result + piecePosition;
        result = 31*result + (isFirstMove?1:0);
        return result;
    }


    @Override
    public boolean equals(final Object object){
        if(this == object) return true;
        if(!(object instanceof  Piece)) return false;
        final Piece piece  = (Piece)object;
        return piecePosition == piece.getPiecePosition() && pieceType == piece.getPieceType()
                && pieceAlliance == piece.getPieceAlliance() && isFirstMove == piece.isFirstMove;
    }

    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }

    public abstract Piece movePiece(Move move);
    public abstract Collection<Move> calculateLegalMoves(final Board board);


    public boolean isFirstMove() {
        return this.isFirstMove;
    }
    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }
    public Integer getPiecePosition(){
        return this.piecePosition;
    }
    public PieceType getPieceType(){
        return this.pieceType;
    }

    public enum PieceType{
        PAWN("P"){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };


        private String pieceName;

        PieceType(final String pieceName){
            this.pieceName = pieceName;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }

        public abstract boolean isKing();

        public abstract boolean isRook();
    }
}
