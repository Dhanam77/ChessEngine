package com.chess.gui;

import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static com.chess.engine.board.BoardUtils.NUM_TILES;

public class Table {

    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private static final Dimension FRAME_DIMENSION= new Dimension(600,600);
    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");

    public Table(){
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
    }

    private class TilePanel extends JPanel{

        private final int tileCoordinate;

        TilePanel(final BoardPanel boardPanel, final int tileCoordinate){
            super(new GridBagLayout());
            this.tileCoordinate = tileCoordinate;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            validate();

        }

        private void assignTileColor() {
            if(BoardUtils.FIRST_ROW[this.tileCoordinate]
            || BoardUtils.THIRD_ROW[this.tileCoordinate]
            || BoardUtils.SEVENTH_ROW[this.tileCoordinate]
                    || BoardUtils.FIFTH_ROW[this.tileCoordinate]){
                if(tileCoordinate % 2 == 0) setBackground(lightTileColor);
                else setBackground(darkTileColor);
            }
            else{
                if(tileCoordinate % 2 == 0) setBackground(darkTileColor);
                else setBackground(lightTileColor);
            }
        }

    }




}

