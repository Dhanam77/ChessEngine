package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;
import java.util.List;

//Abstract, so cant be instantiated
public abstract class Piece {

    //Each piece has a position and an alliance (black or white)
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;

    Piece(final int piecePosition, final Alliance pieceAlliance){
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        isFirstMove = false;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);
}
