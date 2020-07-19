package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Queen extends Piece {
    private static final int[] CANDIDATE_MOVES_OFFSET = {-9,-8,-7,-1,1,8,7,9};

    public Queen(Alliance pieceAlliance,int piecePosition) {
        super(PieceType.QUEEN, piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        List<Move> legalMoves = new ArrayList<>();
        int candidateDestinationCoordinate;

        //Loop through all offsets
        for(int currentOffset : CANDIDATE_MOVES_OFFSET){
            candidateDestinationCoordinate = this.piecePosition;

            //Keep going in direction of one offset until valid
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                if (isFirstColumnExclusion(currentOffset, candidateDestinationCoordinate) ||
                        isEighthColumnExclusion(currentOffset, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentOffset;
                if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                    //First check if tile is occupied. If not, then keep moving and adding
                    //else check if it's same team or different. If different, add.
                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    } else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                        if(this.pieceAlliance != pieceAlliance){
                            legalMoves.add(new Move.AttackingMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }

                        // Break because of one piece as an obstacle
                        break;
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }
    private static boolean isFirstColumnExclusion(final int currentCandidate,
                                                  final int candidateDestinationCoordinate) {
        return (BoardUtils.FIRST_COLUMN[candidateDestinationCoordinate] &&
                ((currentCandidate == -9) || (currentCandidate == 7) || (currentCandidate == -1)));
    }

    private static boolean isEighthColumnExclusion(final int currentCandidate,
                                                   final int candidateDestinationCoordinate) {
        return BoardUtils.EIGHT_COLUMN[candidateDestinationCoordinate] &&
                ((currentCandidate == -7) || (currentCandidate == 9) || (currentCandidate == -1));
    }
    @Override
    public String toString(){
        return PieceType.QUEEN.toString();
    }
}
