package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;

public class BlackKnight extends BlackPiece {
    public BlackKnight(int x,int y, App p) {
        super(x,y,"src/main/resources/XXLChess/b-knight.png",p, 2);
    }

    public void updateMovable(Piece[][] board) {
        movableList = ShowPieceMove.updateKnightMovable(this, board);
        //this.illegalMoveAlphaCheck();
    }
}