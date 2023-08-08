package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;

public class WhiteKnightKing extends WhitePiece {
    
    public WhiteKnightKing(int x, int y, App p) {
        super(x, y,"src/main/resources/XXLChess/w-knight-king.png", p, 5);
    }

    public void updateMovable(Piece[][] board) {
        movableList = ShowPieceMove.updateKnightMovable(this, board);
        movableList.addAll(ShowPieceMove.updateKingMovable(this, board));
        //this.illegalMoveAlphaCheck();
    }
}