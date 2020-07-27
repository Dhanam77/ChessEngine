package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.chess.engine.board.BoardUtils.NUM_TILES;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {

    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private final JFrame gameFrame;

    private final BoardPanel boardPanel;

    private Tile sourceTile, destinationTile;
    private Piece humanMovedPiece;
    private static final Dimension FRAME_DIMENSION= new Dimension(600,600);

    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");

    private Board chessBoard;

    private static final String defaultPieceImagesPath = "C:\\Users\\dhanam\\IdeaProjects\\Chess\\images\\icons\\plain\\";

    public Table(){
        //Initialise the configuration
        this.chessBoard = Board.createStandardBoard();

        this.gameFrame = new JFrame("ChessAI");
        this.gameFrame.setLayout(new BorderLayout());
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        final JMenuBar menuBar = new JMenuBar();
        populateMenuBar(menuBar);
        this.gameFrame.setJMenuBar(menuBar);
        this.gameFrame.setSize(FRAME_DIMENSION);
        this.gameFrame.setVisible(true);
    }

    private void populateMenuBar(JMenuBar menuBar) {
        menuBar.add(createFileMenu());
    }

    // Create menubar
    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        final JMenuItem exitMenu = new JMenuItem("Exit Game");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open PGN File");
            }
        });
        exitMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(openPGN);
        fileMenu.add(exitMenu);
        return fileMenu;
    }

    private class BoardPanel extends JPanel{
        final List<TilePanel> boardTiles;
        BoardPanel(){
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();

            for(int i = 0;i < NUM_TILES; i++){
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);

                //Add to JPanel
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
        public void drawBoard(Board board){
            removeAll();
            for(final TilePanel tilePanel: boardTiles){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    private class TilePanel extends JPanel{

        private final int tileCoordinate;

        TilePanel(final BoardPanel boardPanel, final int tileCoordinate){
            super(new GridBagLayout());
            this.tileCoordinate = tileCoordinate;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignImageOnPiece(chessBoard);
            validate();

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    if(isRightMouseButton(e)){
                        clearTiles();
                    }
                    else if(isLeftMouseButton(e)){
                        if(sourceTile == null){
                            sourceTile = chessBoard.getTile(tileCoordinate);
                            humanMovedPiece = sourceTile.getPiece();
                            if(humanMovedPiece == null){
                                sourceTile = null;
                            }
                        }
                        else{
                            destinationTile = chessBoard.getTile(tileCoordinate);
                            final Move move = Move.MoveFactory.createMove(chessBoard,sourceTile.getTileCoordinate(),
                                    destinationTile.getTileCoordinate());
                            final MoveTransition moveTransition = chessBoard.currentPlayer().makeMove(move);
                            if(moveTransition.getMoveStatus().isDone()){
                                chessBoard = moveTransition.getTransitionBoard();
                            }
                            clearTiles();
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }

                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }
        public void drawTile(final Board board){
            assignTileColor();
            assignImageOnPiece(board);
            validate();
            repaint();
        }

        private void assignImageOnPiece(final Board board) {
            this.removeAll();
            if(board.getTile(this.tileCoordinate).isTileOccupied()){
                try{
                    System.out.println(defaultPieceImagesPath +
                            board.getTile(this.tileCoordinate).getPiece().getPieceAlliance().toString().substring(0,1) +
                            board.getTile(tileCoordinate).getPiece().toString() + ".gif");
                    final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath +
                            board.getTile(this.tileCoordinate).getPiece().getPieceAlliance().toString().substring(0,1) +
                            board.getTile(tileCoordinate).getPiece().toString() + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                }catch(IOException e){
                    e.printStackTrace();
                }

            }
        }

        private void assignTileColor() {
            if(BoardUtils.EIGHT_RANK[this.tileCoordinate]
            || BoardUtils.SIXTH_RANK[this.tileCoordinate]
            || BoardUtils.FOURTH_RANK[this.tileCoordinate]
                    || BoardUtils.SECOND_RANK[this.tileCoordinate]){
                if(tileCoordinate % 2 == 0) setBackground(lightTileColor);
                else setBackground(darkTileColor);
            }
            else{
                if(tileCoordinate % 2 == 0) setBackground(darkTileColor);
                else setBackground(lightTileColor);
            }
        }

    }

    private void clearTiles() {
        sourceTile = null;
        humanMovedPiece = null;
        destinationTile = null;
    }


}


