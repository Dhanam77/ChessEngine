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

public class King extends Piece {

    private static final int[] CANDIDATE_MOVES_OFFSET = {-9,-8,-7,-1,1,7,8,9};

    public King(Alliance pieceAlliance,int piecePosition) {
        super(PieceType.KING, piecePosition, pieceAlliance);
    }

    @Override
    public King movePiece(final Move move) {
        return new King(move.getMovedPiece().pieceAlliance, move.getDestinationCoordinate());
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        int candidateDestinationCoordinate;
        //Check if there are no exclusions. If there, move on to next offset

        for(final int currentOffset : CANDIDATE_MOVES_OFFSET) {
            candidateDestinationCoordinate = this.piecePosition + currentOffset;

            if (isFirstColumnExclusion(this.piecePosition, currentOffset) ||
                    isEighthColumnExclusion(this.piecePosition, currentOffset)) {
                continue;
            }
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                //First check if tile is occupied. If not, then add
                //else check if it's same team or different. If different, add.
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new Move.AttackingMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] &&
                ((candidateOffset == -9) || (candidateOffset == -1) || (candidateOffset == 7));
    }

    private static boolean isEighthColumnExclusion(final int currentPosition,
                                                   final int candidateOffset) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && ((candidateOffset == -7) ||
                (candidateOffset == 1) || (candidateOffset == 9));
    }

    @Override
    public String toString(){
        return PieceType.KING.toString();
    }
}
