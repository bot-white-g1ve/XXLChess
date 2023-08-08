package XXLChess;

import processing.core.*;
import java.util.Arrays;

/**
 * This class is used to simulating what will happen when one move is performed.
 */
public class Simulator{
    public Piece[][] fakeBoard = new Piece[App.BOARD_WIDTH][App.BOARD_WIDTH];;
    int CS = App.CELLSIZE;
    private App parent;

    public int whiteKingCellX = -1;
    public int whiteKingCellY = -1;
    public int blackKingCellX = -1;
    public int blackKingCellY = -1;

    public Simulator(App app){
        this.parent = app;
    }

    /**
     * Check if one side's kill is under attack after one move.
     * @param piece The piece being moved.
     * @param targetCellX The target position's x coordinate.
     * @param targetCellY The target position's y coordinate.
     * @return If this move is legal or not.
     */
    public boolean pieceMoveLegalCheck(Piece piece, int targetCellX, int targetCellY){
        Class<?> enemy = (piece instanceof WhitePiece) ? BlackPiece.class : WhitePiece.class;
        int myKingCellX = (piece instanceof WhitePiece)?whiteKingCellX : blackKingCellX;
        int myKingCellY = (piece instanceof WhitePiece)?whiteKingCellY : blackKingCellY;

        for (int i = 0; i < parent.board.length; i++) {
            fakeBoard[i] = Arrays.copyOf(parent.board[i], parent.board[i].length);
        }
        fakeBoard[targetCellY][targetCellX] = piece;
        fakeBoard[piece.y / CS][piece.x / CS] = null;

        for (int i = 0; i < parent.BOARD_WIDTH; i++) {
            for (int j = 0; j < parent.BOARD_WIDTH; j++) {
                if (enemy.isInstance(fakeBoard[i][j])) {
                    fakeBoard[i][j].updateMovable(fakeBoard);
                    for (int[] enemyMovableCell : fakeBoard[i][j].movableList) {
                        if (!(piece instanceof WhiteKing || piece instanceof BlackKing) && enemyMovableCell[0] == myKingCellX && enemyMovableCell[1] == myKingCellY) {
                            return false;
                        }else if ((piece instanceof WhiteKing || piece instanceof BlackKing) && enemyMovableCell[0] == targetCellX && enemyMovableCell[1] == targetCellY){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

}