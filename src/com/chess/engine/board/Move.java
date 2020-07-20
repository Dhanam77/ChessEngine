package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;


    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    //This method will execute a move, which in turn will create a new board
    public abstract Board execute();

    public Piece getMovedPiece(){
        return this.movedPiece;
    }

    //A move which does not attack any opponent piece
    public static final class MajorMove extends Move {
        public MajorMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }


        //Execute a move and then return a new board instead of mutating the existiong board
        @Override
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
    }

    public static final class AttackingMove extends Move {
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
}
