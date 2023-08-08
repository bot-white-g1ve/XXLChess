package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;

public class WhiteArchBishop extends WhitePiece{
    
    public WhiteArchBishop(int x, int y, App p) {
        super(x, y,"src/main/resources/XXLChess/w-archbishop.png", p, 7.5);
    }

    public void updateMovable(Piece[][] board) {
        movableList = ShowPieceMove.updateBishopMovable(this, board);
        movableList.addAll(ShowPieceMove.updateKnightMovable(this, board));
        //this.illegalMoveAlphaCheck();
    }
}