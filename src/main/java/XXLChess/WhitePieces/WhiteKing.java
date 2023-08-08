package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;

public class WhiteKing extends WhitePiece {
    
    public WhiteKing(int x,int y, App p) {
        super(x,y,"src/main/resources/XXLChess/w-king.png", p, 0);
    }

    public void updateMovable(Piece[][] board) {
        movableList = ShowPieceMove.updateKingMovable(this, board);
        //this.illegalMoveAlphaCheck();
    }
}