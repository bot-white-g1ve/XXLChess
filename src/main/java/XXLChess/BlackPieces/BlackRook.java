package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;

public class BlackRook extends BlackPiece {
    public BlackRook(int x, int y, App p) {
        super(x,y,"src/main/resources/XXLChess/b-rook.png",p, 5.25);
    }

    public void updateMovable(Piece[][] board) {
        this.movableList = ShowPieceMove.updateRookMovable(this, board);
        //this.illegalMoveAlphaCheck();
    }
}