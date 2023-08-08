package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;

/**
 * This is the general abstract class for all pieces.
 */
public abstract class Piece {
    protected List<int[]> movableList;
    protected int x;
    protected int y;
    protected boolean moved = false;
    protected String path;
    public PImage sprite;
    private App parent;
    public double value;

    int CS = App.CELLSIZE;
    double PM = App.piece_movement_speed;
    int MMT = App.max_movement_time;

    /**
     * Constructor
     * @param x The x position of the piece initially.
     * @param y The y position of the piece initially.
     * @param path The path for the piece's image.
     * @param parent The main class app's instance.
     * @param value The value of the piece.
     */
    public Piece(int x, int y, String path, App parent, double value) {
        this.x = x;
        this.y = y;
        this.path = path;
        this.parent = parent;
        this.sprite = parent.loadImage(path);
        this.sprite.resize(CS, CS);
        this.value = value;
    }

    /**
     * Preprocess for a piece's move.
     * @param selectedCellX The target position's x coordinate.
     * @param selectedCellY The target position's y coordinate.
     * @return If this move can be processed.
     */
    public boolean preMove(int selectedCellX, int selectedCellY){
        boolean found = false;
        for (int[] movableCell : movableList) {
            if (movableCell[0] == selectedCellX && movableCell[1] == selectedCellY) {
                found = true;
            }
        }
        if (found == false){
            return false;
        }

        if (parent.checked == true) {
            boolean legalOrNot = parent.simulator.pieceMoveLegalCheck(this, selectedCellX, selectedCellY);
            if (legalOrNot == false){
                parent.illegalMoveCountDown = App.FPS * 3;
                return false;
            }
        }

        for (int[] movableCell : movableList) {
            if (movableCell[0] == selectedCellX && movableCell[1] == selectedCellY) {
                double displacement = Math.sqrt(Math.pow(selectedCellX * CS - this.x, 2) + Math.pow(selectedCellY * CS - this.y, 2));
                parent.core.requiredFrames = (int) Math.round(displacement / PM);
                if (parent.core.requiredFrames > MMT) {
                    parent.core.requiredFrames = MMT;
                }
                parent.core.speedX = (int) Math.round((selectedCellX * CS - this.x) / parent.core.requiredFrames);
                parent.core.speedY = (int) Math.round((selectedCellY * CS - this.y) / parent.core.requiredFrames);
                return true;
            }
        }
        return false;
    }

    public void illegalMoveCheck(){
        if ((parent.checked == false)||(parent.checked == true && (this instanceof WhiteKing || this instanceof BlackKing))){
            Iterator<int[]> iterator = this.movableList.iterator();
            while (iterator.hasNext()) {
                int[] movableCell = iterator.next();
                boolean legalOrNot = parent.simulator.pieceMoveLegalCheck(this, movableCell[0],movableCell[1]);
                if (legalOrNot == false){
                    iterator.remove();
                }
            }
        }

        if (parent.checked == true && !(this instanceof WhiteKing || this instanceof BlackKing)){
            return;
        }
    }

    public void showSelected(PApplet p){
        Class<?> enemy = (this instanceof WhitePiece) ? BlackPiece.class : WhitePiece.class;
        for (int[] movableCell : movableList){
            if (parent.board[movableCell[1]][movableCell[0]] == null){
                ShowPieceMove.cellMovable(p, movableCell[0], movableCell[1]);
            }else{
                ShowPieceMove.cellEatable(p, movableCell[0], movableCell[1]);
            }
        }
    }

    public abstract void updateMovable(Piece[][] board);
}
