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

public class Rook extends Piece {

    private static final int[] CANDIDATE_MOVES_OFFSET = {-8, -1, 1, 8};


    public Rook(Alliance pieceAlliance,int piecePosition) {
        super(piecePosition, pieceAlliance);
    }


    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        List<Move> legalMoves = new ArrayList<>();
        int candidateDestinationCoordinate;

        //Loop through all offsets
        for (int currentOffset : CANDIDATE_MOVES_OFFSET) {
            candidateDestinationCoordinate = this.piecePosition;

            //Keep going in direction of one offset until valid
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(currentOffset, candidateDestinationCoordinate) ||
                        isEighthColumnExclusion(currentOffset, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentOffset;
                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                    //First check if tile is occupied. If not, then keep moving and adding
                    //else check if it's same team or different. If different, add.
                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    } else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                        if (this.pieceAlliance != pieceAlliance) {
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
                ((currentCandidate == -1)));
    }

    private static boolean isEighthColumnExclusion(final int currentCandidate,
                                                   final int candidateDestinationCoordinate) {
        return BoardUtils.EIGHT_COLUMN[candidateDestinationCoordinate] &&
                ((currentCandidate == 1));
    }
}