package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public int getCurrentCoordinate(){
        return this.movedPiece.getPiecePosition();
    }

    //Execute a move and then return a new board instead of mutating the existiong board
    public Board execute() {

        //Create new builder object
        final Board.Builder builder = new Board.Builder();
        //set current players pieces as it is
        for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
            //If the current piece is not the moved piece, then copy it exactly to the new board
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        //Set opponents pieces as it is
        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }

        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    public Piece getMovedPiece(){
        return this.movedPiece;
    }

    //A move which does not attack any opponent piece
    public static final class MajorMove extends Move {
        public MajorMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static class AttackingMove extends Move {
        Piece attackedPiece;

        public AttackingMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute() {
            return null;
        }
    }

    public static final class PawnMove extends Move {
        public PawnMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }
    public static class PawnAttackMove extends AttackingMove {
        public PawnAttackMove(Board board, Piece movedPiece, int destinationCoordinate,Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate,attackedPiece);
        }


    }
    public static final class PawnEnPassantAttackMove extends PawnAttackMove {
        public PawnEnPassantAttackMove(Board board, Piece movedPiece, int destinationCoordinate,Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate,attackedPiece);
        }
    }
    public static final class PawnJump extends Move {
        public PawnJump(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }
    public static final class CastleMove extends Move {
        public CastleMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }
    public static final class KingSideCastleMove extends Move {
        public KingSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }
    public static final class QueenSideCastleMove extends Move {
        public QueenSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }
    public static final class NullMove extends Move {
        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute(){
            throw new RuntimeException("Cannot execute null move");
        }
    }

    public static class MoveFactory {

        private  MoveFactory(){
            throw new RuntimeException("Cannot instantiate");
        }

        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate){
            for(final Move move: board.getAllLegalMoves()){
                if(move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }

}
