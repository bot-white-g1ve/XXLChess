package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;

public abstract class BlackPiece extends Piece {
    public BlackPiece(int x, int y, String path, App p, double value) {
        super(x, y, path, p, value);
    }

    public abstract void updateMovable(Piece[][] board);
}