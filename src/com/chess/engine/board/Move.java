package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

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

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;

        result = result*prime + (this.destinationCoordinate);
        result = result*prime + (this.movedPiece.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object object){
        if(this == object){
            return true;
        }
        if(!(object instanceof Move)){
            return false;
        }
        final Move otherMove = (Move)object;
        return getDestinationCoordinate() == ((Move) object).getDestinationCoordinate()
                && getMovedPiece().equals(otherMove.getMovedPiece());
    }


    public boolean isAttack(){
        return false;
    }
    public boolean isCastlingMove(){
        return false;
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

        @Override
        public boolean isAttack(){
            return true;
        }

        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }

        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }
        @Override
        public boolean equals(final Object object){
            if(this == object){
                return true;
            }
            if(!(object instanceof Move)){
                return false;
            }
            final AttackingMove otherMove = (AttackingMove) object;
            return super.equals(otherMove) && getAttackedPiece().equals(otherMove.getAttackedPiece());
        }
    }

    public static final class PawnMove extends Move {
        public PawnMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);

        }
        //This method is used to execute a new move
        @Override
        public Board execute(){

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

            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);

            builder.setPiece(movedPawn);
            builder.setEnpassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
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
    public static class CastleMove extends Move {

        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;



        public CastleMove(Board board, Piece movedPiece, int destinationCoordinate,
                          Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;

        }
        public Rook getCastleRook(){
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove(){
            return true;
        }

        @Override
        public Board execute(){
            //Create new builder object
            final Board.Builder builder = new Board.Builder();
            //set current players pieces as it is
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                //If the current piece is not the moved piece, then copy it exactly to the new board
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            //Set opponents pieces as it is
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRook.getPieceAlliance(),this.castleRookDestination));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

            return builder.build();
        }

    }
    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate,
                                  Rook rook, int rookStart, int rookDestination) {
            super(board, movedPiece, destinationCoordinate, rook, rookStart, rookDestination);
        }

        @Override
        public String toString(){
            return "0-0";
        }
    }
    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate,
                                   Rook rook, int rookStart, int rookDestination) {
            super(board, movedPiece, destinationCoordinate, rook, rookStart, rookDestination);


        }
        @Override
        public String toString(){
            return "0-0-0";
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
