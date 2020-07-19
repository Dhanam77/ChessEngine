package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    Player(final Board board, final Collection<Move> legalMoves, Collection<Move> opponentMoves){
        this.board = board;
        this.legalMoves = legalMoves;
        this.playerKing = establishKing();
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    private static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> opponentMoves) {
        final List<Move> attackMoves = new ArrayList<>();
        for(final Move move : opponentMoves){
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }

        return Collections.unmodifiableList(attackMoves);
    }


    protected King establishKing(){
        for(Piece piece : getActivePieces()){
            if(piece.getPieceType().isKing()){
                return (King)piece;
            }
        }
        throw new RuntimeException("Not a valid board");
    }

    public boolean isLegalMove(final Move move){
        return this.legalMoves.contains(move);
    }

    public boolean isInCheck(){
        return this.isInCheck;
    }
    public boolean isInCheckMate(){
        return this.isInCheck && !hasEscapeMoves();
    }

    public boolean isInStaleMate(){
        return !this.isInCheck && !hasEscapeMoves();
    }

    protected boolean hasEscapeMoves(){
        for(final Move move : this.legalMoves){
            final MoveTransition transition = makeMove(move);
        }
    }

    public boolean isCastled(){
        return false;
    }

    public MoveTransition makeMove(final Move move){
        return null;
    }


    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();


}
