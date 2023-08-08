package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;

public class BlackCamel extends BlackPiece {
    public BlackCamel(int x,int y, App p) {
        super(x,y,"src/main/resources/XXLChess/b-camel.png",p, 2);
    }

    public void updateMovable(Piece[][] board) {
        movableList = ShowPieceMove.updateCamelMovable(this, board);
        //this.illegalMoveAlphaCheck();
    }
}