package XXLChess;

import processing.core.PApplet;
import java.util.Random;
import java.util.Arrays;

/**
 * This class represents the AI.
 */
public class AI{
    private App parent;
    private Piece selectedPiece;
    private Piece tempPiece;
    private Piece target;
    private Piece tempTarget;
    private  int targetCellX;
    private  int targetCellY;
    private  double largestValue;
    private  double tempValue;

    public  int AIThinkingTime = 0;

    int CS;

     /**
      * Constructor.
      * @param parent The main class app's instance.
      */
    public AI(App parent){
        this.parent = parent;
        CS = parent.CELLSIZE;
    }

    /**
     * AI evaluates the value of a moving and select a proper piece.
     */
    public  void select(){
        //System.out.println("Starting selection");
        if (parent.checked == false){
            largestValue = -64;
            selectedPiece = null;
            for (int i = 0; i < parent.BOARD_WIDTH; i++){
                for (int j = 0; j < parent.BOARD_WIDTH; j++){
                    tempPiece = parent.board[i][j];
                    if (tempPiece instanceof BlackPiece){
                        tempPiece.updateMovable(parent.board);
                        tempPiece.illegalMoveCheck();
                        //System.out.println("the temp piece is "+ tempPiece.getClass().getName());
                        for (int[] movableCell : tempPiece.movableList){
                            tempTarget = parent.board[movableCell[1]][movableCell[0]];
                            tempValue = evaluate(tempPiece, tempTarget, movableCell[0], movableCell[1]);
                            if (tempValue > largestValue){
                                largestValue = tempValue;
                                selectedPiece = tempPiece;
                                target = tempTarget;
                                targetCellX = movableCell[0];
                                targetCellY = movableCell[1];
                                //System.out.println("We change the selected piece to "+ selectedPiece.getClass().getName());
                                //System.out.println("Now the largest value is "+ largestValue);
                            }else if (tempValue == largestValue){
                                if (new Random().nextDouble() < 0.05){
                                    largestValue = tempValue;
                                    selectedPiece = tempPiece;
                                    target = tempTarget;
                                    targetCellX = movableCell[0];
                                    targetCellY = movableCell[1];
                                    //System.out.println("We change the selected piece to "+ selectedPiece.getClass().getName());
                                    //System.out.println("Now the largest value is "+ largestValue);
                                }
                            }
                        }
                    }
                }
            }
            //System.out.println(" ");
        }else if (parent.checked == true){
            largestValue = -64;
            selectedPiece = null;
            for (int i = 0; i < parent.BOARD_WIDTH; i++){
                for (int j = 0; j < parent.BOARD_WIDTH; j++){
                    tempPiece = parent.board[i][j];
                    if (tempPiece instanceof BlackPiece){
                        tempPiece.updateMovable(parent.board);
                        tempPiece.illegalMoveCheck();
                        //System.out.println("the temp piece is "+ tempPiece.getClass().getName());
                        for (int[] movableCell : tempPiece.movableList){
                            tempTarget = parent.board[movableCell[1]][movableCell[0]];
                            tempValue = evaluate(tempPiece, tempTarget, movableCell[0], movableCell[1]);
                            boolean legal = parent.simulator.pieceMoveLegalCheck(tempPiece, movableCell[0], movableCell[1]);
                            if (legal == true){
                                if (tempValue > largestValue){
                                    largestValue = tempValue;
                                    selectedPiece = tempPiece;
                                    target = tempTarget;
                                    targetCellX = movableCell[0];
                                    targetCellY = movableCell[1];
                                    //System.out.println("We change the selected piece to "+ selectedPiece.getClass().getName());
                                    //System.out.println("Now the largest value is "+ largestValue);
                                }else if (tempValue == largestValue){
                                    if (new Random().nextDouble() < 0.05){
                                        largestValue = tempValue;
                                        selectedPiece = tempPiece;
                                        target = tempTarget;
                                        targetCellX = movableCell[0];
                                        targetCellY = movableCell[1];
                                        //System.out.println("We change the selected piece to "+ selectedPiece.getClass().getName());
                                        //System.out.println("Now the largest value is "+ largestValue);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //System.out.println(" ");
        }
    }

    /**
     * AI prepares for doing his move.
     */
    public  void preMove(){
        if (selectedPiece != null){
            parent.movedPiece = selectedPiece;
            parent.selectedX = targetCellX*CS;
            parent.selectedY = targetCellY*CS;
            parent.originalX = selectedPiece.x;
            parent.originalY = selectedPiece.y;
            selectedPiece.preMove(targetCellX, targetCellY);
        }else{
            parent.playerWinByCheckmateFlag = true;
        }
    }

    /**
     * AI evaluates the value for a specific move.
     * @param piece The piece being moved.
     * @param target The piece being captured(null indicates no capture).
     * @param cellX The target's cell's X coordinate.
     * @param cellY The target's cell's Y coordinate.
     * @return The value for this move.
     */
    private  double evaluate(Piece piece, Piece target, int cellX, int cellY){
        boolean danger = false;
        Class<?> enemy = (piece instanceof WhitePiece) ? BlackPiece.class : WhitePiece.class;
        for (int i = 0; i < parent.board.length; i++) {
            parent.simulator.fakeBoard[i] = Arrays.copyOf(parent.board[i], parent.board[i].length);
        }
        parent.simulator.fakeBoard[cellY][cellX] = piece;
        parent.simulator.fakeBoard[piece.y / CS][piece.x / CS] = null;

        for (int i = 0; i < parent.BOARD_WIDTH; i++) {
            for (int j = 0; j < parent.BOARD_WIDTH; j++) {
                if (enemy.isInstance(parent.simulator.fakeBoard[i][j])) {
                    parent.simulator.fakeBoard[i][j].updateMovable(parent.simulator.fakeBoard);
                    for (int[] enemyMovableCell : parent.simulator.fakeBoard[i][j].movableList) {
                        if (enemyMovableCell[0] == cellX && enemyMovableCell[1] == cellY) {
                            danger = true;
                            break;
                        }
                    }
                }
            }
        }

        if (danger == false){
            if (target == null){
                return 0.0;
            }else{
                return target.value;
            }
        }else{
            if (target == null){
                return -piece.value;
            }else{
                return target.value-piece.value;
            }
        }
    }
}