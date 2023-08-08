package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;

public class WhiteQueen extends WhitePiece {
    
    public WhiteQueen(int x, int y, App p) {
        super(x,y,"src/main/resources/XXLChess/w-queen.png", p, 9.5);
    }

    public void updateMovable(Piece[][] board) {
        movableList = ShowPieceMove.updateBishopMovable(this, board);
        movableList.addAll(ShowPieceMove.updateRookMovable(this, board));
        //this.illegalMoveAlphaCheck();
    }
}