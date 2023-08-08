package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;

public class BlackPawn extends BlackPiece {
    public BlackPawn(int x, int y, App p) {
        super(x,y,"src/main/resources/XXLChess/b-pawn.png", p, 1);
    }

    public void updateMovable(Piece[][] board){
        movableList = ShowPieceMove.updatePawnMovable(this, board);
        //this.illegalMoveAlphaCheck();
    }
}