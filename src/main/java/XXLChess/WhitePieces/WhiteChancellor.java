package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;

public class WhiteChancellor extends WhitePiece {
    
    public WhiteChancellor(int x,int y, App p) {
        super(x,y,"src/main/resources/XXLChess/w-chancellor.png", p, 8.5);
    }

    public void updateMovable(Piece[][] board) {
        movableList = ShowPieceMove.updateKnightMovable(this, board);
        movableList.addAll(ShowPieceMove.updateRookMovable(this, board));
        //this.illegalMoveAlphaCheck();
    }
}