package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;

public class BlackBishop extends BlackPiece {
    public BlackBishop(int x,int y, App p) {
        super(x,y,"src/main/resources/XXLChess/b-bishop.png", p, 3.625);
    }

    public void updateMovable(Piece[][] board) {
        movableList = ShowPieceMove.updateBishopMovable(this, board);
        //this.illegalMoveAlphaCheck();
    }
}