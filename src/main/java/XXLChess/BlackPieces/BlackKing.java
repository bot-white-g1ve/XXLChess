package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;

public class BlackKing extends BlackPiece {
    public BlackKing(int x, int y, App p) {
        super(x, y,"src/main/resources/XXLChess/b-king.png", p, 0);
    }

    public void updateMovable(Piece[][] board) {
        movableList = ShowPieceMove.updateKingMovable(this, board);
        //this.illegalMoveAlphaCheck();
    }
}