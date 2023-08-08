package XXLChess;

import processing.core.*;
import java.util.Arrays;

/**
 * This class is used to deal with issues regarding the drawing issue.
 */
public class Core{
    public App parent;
    int BW;
    int CS;
    public int requiredFrames;
    public  int speedX;
    public  int speedY;

    /**
     * Constructor
     * @param parent The main class App's instance
     */
    public Core(App parent){
        this.parent = parent;
        BW = parent.BOARD_WIDTH;
        CS = parent.CELLSIZE;
    }

    /**
     * Draws the board.
     */
    public  void showBoard(){
        parent.stroke(0,0,0,0);
        for (int i = 0; i< BW; i++) {
            if (i%2 == 0) {
                for (int j = 0; j < BW; j++) {
                    if (j%2 == 0){
                        parent.fill(240,217,181);
                    }else{
                        parent.fill(181,136,99);
                    }
                    parent.rect(j*CS, i*CS, CS, CS);
                }
            }else{
                for (int j = 0; j < BW; j++) {
                    if (j%2 == 0){
                        parent.fill(181,136,99);
                    }else{
                        parent.fill(240,217,181);
                    }
                    parent.rect(j*CS, i*CS, CS, CS);
                }
            }
        }
    }

    /**
     * Draws the bar.
     */
    public  void showBar(){
        parent.stroke(0,0,0,0);
        parent.fill(180,180,180);
        parent.rect(BW*CS, 0, parent.SIDEBAR,BW*CS);
    }

    /**
     * Indicates the grid player points to.
     */
    public  void showMousePosition(){
        if (parent.mouseX <= CS*BW) {
            parent.fill(0, 0, 0, 0);
            parent.stroke(255, 255, 0);
            parent.strokeWeight(3);
            parent.rect(parent.mouseX-parent.mouseX%CS, parent.mouseY-parent.mouseY%CS, CS, CS);
        }
    }

    /**
     * Highlights the piece the player selects currently.
     */
    public  void showMouseSelected(){
        parent.fill(105,138,76);
        parent.stroke(0,0,0,0);
        parent.rect(parent.selectedX, parent.selectedY, CS, CS);
        parent.selectedPiece.showSelected(parent);
    }

    /**
     * Highlights the piece being moved.
     */
    public  void trackMove(){
        if (parent.movedPiece != null)
            ShowPieceMove.cellMoved(parent.movedPiece, parent, parent.originalX, parent.originalY);
    }

    /**
     * Draws the pieces.
     */
    public void showPieces(){
        for (Piece[] line : parent.board) {
            for (Piece piece : line) {
                if (piece!= null)
                    parent.image(piece.sprite, piece.x, piece.y);
            }
        }
    }

    /**
     * Text in the bar "Check!" and highlight the piece being checked.
     * @param turn 0 indicates it's player's turn, 1 indicates it's cpu's turn.
     */
    public void checked(int turn){
        parent.textSize(36);
        parent.fill(230, 230, 230);
        parent.textAlign(parent.LEFT, parent.TOP);
        parent.text("Check!", BW * CS, (BW - 1) * CS / 2);

        int enemyKingCellX = (turn == 1)?parent.simulator.blackKingCellX:parent.simulator.whiteKingCellX;
        int enemyKingCellY = (turn == 1)?parent.simulator.blackKingCellY:parent.simulator.whiteKingCellY;
        parent.fill(215,0,0,150);
        parent.stroke(0,0,0,0);
        parent.rect(enemyKingCellX*CS,enemyKingCellY*CS,CS,CS);
    }

    /**
     * Called when u try to perform an illegal move when you are in check. Let the king's cell flash.
     */
    public void illegalMoveWhenChecked(){
        parent.textSize(24);
        parent.fill(230, 0,0);
        parent.textAlign(parent.LEFT, parent.TOP);
        parent.text("You must \ndefend \nyour king!", BW * CS, (BW - 1) * CS / 2 + CS);

        if (parent.illegalMoveCountDown > (int)parent.FPS*2.5 || (parent.illegalMoveCountDown < (int)parent.FPS*2 && parent.illegalMoveCountDown > (int)parent.FPS*1.5) || (parent.illegalMoveCountDown < (int)parent.FPS*1 && parent.illegalMoveCountDown > (int)parent.FPS*0.5)){
            int kingCellX = (parent.turn == 1)?parent.simulator.blackKingCellX:parent.simulator.whiteKingCellX;
            int kingCellY = (parent.turn == 1)?parent.simulator.blackKingCellY:parent.simulator.whiteKingCellY;
            if ((kingCellX + kingCellY)%2 == 0){
                parent.fill(240,217,181);
            }else{
                parent.fill(181,136,99);
            }
            parent.stroke(0,0,0,0);
            parent.rect(kingCellX*CS,kingCellY*CS,CS,CS);
        }
    }

    /**
     * Animations when piece is moving.
     */
    public void pieceMove(){
        if(requiredFrames > 0){
            parent.movableFlag = false;  //let counting and moving stop when a piece is moving
            parent.movedPiece.x = parent.movedPiece.x + speedX;
            parent.movedPiece.y = parent.movedPiece.y + speedY;
            requiredFrames --;
            if (requiredFrames == 0){
                parent.board[parent.selectedY/CS][parent.selectedX/CS] = parent.movedPiece;
                parent.movedPiece.x = parent.selectedX;
                parent.movedPiece.y = parent.selectedY;
                parent.board[parent.originalY/CS][parent.originalX/CS] = null;

                parent.movedPiece.moved = true;  //for prawn's special move1 and king's special move2

                pawnPromotion();
                updateKingPosition();
                checkTest();

                parent.counter.timeIncrement();

                if (parent.turn == 0){
                    if (parent.enemy.equals("cpu")) {
                            parent.AIcountdown = parent.max_movement_time + parent.ai.AIThinkingTime * parent.FPS;
                    }else if (parent.enemy.equals("player")){
                        boolean noMovingOrNot = noMovingAvaliabelTest(0);
                        if (noMovingOrNot){
                            parent.playerWinByCheckmateFlag = true;
                        }
                    }
                    parent.turn = 1;
                }else if (parent.turn == 1){
                    boolean noMovingOrNot = noMovingAvaliabelTest(1);
                    if (noMovingOrNot){
                        parent.playerLoseByCheckmateFlag = true;
                    }
                        parent.turn = 0;
                    }
                }

                parent.movableFlag = true;   //correspond to parent.startingFlag = false;
            }
        }

    /**
     * Check if pawn will be promoted after moving.
     */
    private void pawnPromotion(){
        if (parent.movedPiece instanceof WhitePawn && parent.selectedY/CS == 6){
            parent.board[parent.selectedY/CS][parent.selectedX/CS] = new WhiteQueen(parent.selectedX, parent.selectedY, parent);
        }else if (parent.movedPiece instanceof BlackPawn && parent.selectedY/CS == 7){
            parent.board[parent.selectedY/CS][parent.selectedX/CS] = new BlackQueen(parent.selectedX, parent.selectedY, parent);
        }
    }

    /**
     * Update the king's position after moving.
     */
    private  void updateKingPosition(){
        if (parent.movedPiece instanceof WhiteKing){
            parent.simulator.whiteKingCellX = parent.selectedX/CS;
            parent.simulator.whiteKingCellY = parent.selectedY/CS;
        }else if (parent.movedPiece instanceof BlackKing){
            parent.simulator.blackKingCellX = parent.selectedX/CS;
            parent.simulator.blackKingCellY = parent.selectedY/CS;
        }
    }

    /**
     * Test if the opponent's king is in check after moving.
     */
    private void checkTest(){
        parent.movedPiece.updateMovable(parent.board);
        int enemyKingCellX = (parent.movedPiece instanceof WhitePiece)?parent.simulator.blackKingCellX:parent.simulator.whiteKingCellX;
        int enemyKingCellY = (parent.movedPiece instanceof WhitePiece)?parent.simulator.blackKingCellY:parent.simulator.whiteKingCellY;
        boolean found = false;
        for (int[] movableCell : parent.movedPiece.movableList) {
            if (movableCell[0] == enemyKingCellX && movableCell[1] == enemyKingCellY) {
                parent.checked = true;
                found = true;
                break;
            }
        }
        if (found == false){
            parent.checked = false;
        }
    }

    /**
     * Test if the enemy is checkmated.
     * @param turn 0 indicates it's player's turn, 1 indicates it's cpu's turn.
     * @return Return the result if the enemy is checkmated.
     */
    private  boolean noMovingAvaliabelTest(int turn){
        for (int i = 0; i < parent.BOARD_WIDTH; i++) {
            for (int j = 0; j < parent.BOARD_WIDTH; j++) {
                Piece tempPiece = parent.board[i][j];
                if ((turn == 1 && tempPiece instanceof WhitePiece) || (turn == 0 && tempPiece instanceof BlackPiece)) {
                    tempPiece.updateMovable(parent.board);
                    tempPiece.illegalMoveCheck();
                    if (tempPiece.movableList.size() != 0){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}