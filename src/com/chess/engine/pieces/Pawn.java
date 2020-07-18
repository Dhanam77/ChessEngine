package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Pawn extends Piece{

    private static final int[] CANDIDATE_MOVES = {7,8,9,16};

    public Pawn(Alliance pieceAlliance,int piecePosition) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        int candidateDestinationCoordinate;
        for(int currentOffset : CANDIDATE_MOVES){
            candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection()*currentOffset);

            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }
            if(currentOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
            } else if(currentOffset == 16 && this.isFirstMove() && (BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                    ((BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite()))){
                int candidateBehindDestination = this.piecePosition + (this.pieceAlliance.getDirection()*8);
                if(!board.getTile(candidateDestinationCoordinate).isTileOccupied() &&
                        !board.getTile(candidateBehindDestination).isTileOccupied()){
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));

                }
            }
            else if(currentOffset == 7 && ((BoardUtils.EIGHT_COLUMN[piecePosition]&& getPieceAlliance().isWhite() )||
                    (BoardUtils.FIRST_COLUMN[piecePosition]&& getPieceAlliance().isBlack()))){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(pieceOnCandidate.getPieceAlliance() != this.pieceAlliance){
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }
            }
            else if(currentOffset == 9 && ((BoardUtils.FIRST_COLUMN[piecePosition]&& getPieceAlliance().isWhite() )||
                    (BoardUtils.EIGHT_COLUMN[piecePosition]&& getPieceAlliance().isBlack()))){
                final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied())
                {
                    if(pieceOnCandidate.getPieceAlliance() != this.pieceAlliance){
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }

            }

        }


        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }
}
