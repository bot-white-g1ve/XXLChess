package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;

public class BlackAmazon extends BlackPiece {
    public BlackAmazon(int x, int y, App p) {
        super(x,y,"src/main/resources/XXLChess/b-amazon.png", p, 12);
    }

    public void updateMovable(Piece[][] board) {
        movableList = ShowPieceMove.updateBishopMovable(this, board);
        movableList.addAll(ShowPieceMove.updateKnightMovable(this, board));
        movableList.addAll(ShowPieceMove.updateRookMovable(this, board));
        //this.illegalMoveAlphaCheck();
    }
}