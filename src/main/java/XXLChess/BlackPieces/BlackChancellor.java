package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;

public class BlackChancellor extends BlackPiece {
    public BlackChancellor(int x, int y, App p) {
        super(x,y,"src/main/resources/XXLChess/b-chancellor.png", p, 8.5);
    }

    public void updateMovable(Piece[][] board) {
        movableList = ShowPieceMove.updateKnightMovable(this, board);
        movableList.addAll(ShowPieceMove.updateRookMovable(this, board));
        //this.illegalMoveAlphaCheck();
    }
}