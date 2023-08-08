package XXLChess;

import processing.core.*;

/**
 * This class especially deals with issues regarding time management.
 */
public class Counting{
    public App parent;
    int startTime;
    int PMS;
    int CMS;
    int lastUpdateTime;
    int BW;
    int CS;

    /**
     * Constructor: initially set up
     * @param parent The main class App's instance
     */
    public Counting(App parent){
        this.parent = parent;
        PMS = parent.playerMiliSeconds;
        CMS = parent.cpuMiliSeconds;
        BW = parent.BOARD_WIDTH;
        CS = parent.CELLSIZE;
    }

    /**
     * record the startTime when the game starts.
     */
    public void start(){
        startTime = parent.millis();
    }

    /**
     * When game starts, count down from 3.
     */
    public void startingCountDown(){
        int currentTime = (parent.millis() - startTime) / 1000;
        int remainingTime = 3 - currentTime;
        parent.textSize(36);
        parent.fill(230,230,230);
        parent.textAlign(parent.LEFT,parent.TOP);
        parent.text(PMS/60000+":"+String.format("%02d",(PMS/1000)%60), BW*CS, (BW-1)*CS);
        parent.text(CMS/60000+":"+String.format("%02d",(CMS/1000)%60), BW*CS, 0);

        if (remainingTime >= 0) {
            parent.textSize(96);
            parent.textAlign(parent.CENTER);
            parent.fill(255, 0, 0);
            parent.text(remainingTime, parent.width/2, parent.height/2);
        }else{
            lastUpdateTime = parent.millis();
            parent.startingFlag = true;
            parent.movableFlag = true;
        }
    }

    /**
     * Reduce the time for both side when it is their turn.
     */
    public void turnCountDown(){
        if (parent.endingFlag == false && parent.movableFlag == true){
            if (parent.turn == 0){
                PMS = PMS - (parent.millis() - lastUpdateTime);
            }else{
                CMS = CMS - (parent.millis() - lastUpdateTime);
            }
            if (PMS <= 0){
                parent.playerLoseOnTimeFlag = true;
            }else if (CMS <= 0){
                parent.playerWinOnTimeFlag = true;
            }
        }
        parent.textSize(36);
        parent.fill(230,230,230);
        parent.textAlign(parent.LEFT,parent.TOP);
        parent.text(PMS/60000+":"+String.format("%02d",(PMS/1000)%60), BW*CS, (BW-1)*CS);
        parent.text(CMS/60000+":"+String.format("%02d",(CMS/1000)%60), BW*CS, 0);
        lastUpdateTime = parent.millis();
    }

    /**
     *  Increase the time for both sides when they finish their move.
     */
    public void timeIncrement(){
        if (parent.movedPiece instanceof WhitePiece){
            PMS += parent.playerIncreSeconds*1000;
        }else{
            CMS += parent.cpuIncreSeconds*1000;
        }
    }
}