package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;

public class WhiteRook extends WhitePiece {
    
    public WhiteRook(int x, int y, App p) {
        super(x,y,"src/main/resources/XXLChess/w-rook.png", p, 5.25);
    }

    public void updateMovable(Piece[][] board) {
        this.movableList = ShowPieceMove.updateRookMovable(this, board);
        //this.illegalMoveAlphaCheck();
    }
}