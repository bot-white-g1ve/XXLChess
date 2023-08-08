package XXLChess;

//import org.reflections.Reflections;
//import org.reflections.scanners.Scanners;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;
import processing.core.PFont;
import processing.event.MouseEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.awt.Font;
import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.lang.NoSuchMethodException;
import java.lang.InstantiationException;
import java.lang.IllegalAccessException;

/**
 * This is the main class. The application users plays with
 */
public class App extends PApplet {
    public static final int SPRITESIZE = 480;
    public static int CELLSIZE;
    public static int SIDEBAR;
    public static int BOARD_WIDTH;
    public static int WIDTH;
    public static int HEIGHT;
    public static int FPS;
    public static double piece_movement_speed;
    public static int max_movement_time;
    public String enemy;
    private String layout;

    public Piece[][] board;
    private static HashMap<String,String> references = new HashMap<String,String>();
    public int selectedX;
    public int selectedY;
    public int selectedCellX;
    public int selectedCellY;
    public int originalX;
    public int originalY;
    private boolean selected = false;
    public int turn = 0;
    public Piece selectedPiece;
    public Piece movedPiece;
    public boolean checked = false;
    public int illegalMoveCountDown = 0;

    public boolean chooseEnemyFlag = true;
    public boolean chooseLevelFlag = false;
    public boolean startingFlag = false;
    public boolean endingFlag = false;
    public boolean movableFlag = false;
    public int playerMiliSeconds;
    public int cpuMiliSeconds;
    public int playerIncreSeconds;
    public int cpuIncreSeconds;
    public int AIcountdown = 0;

    public boolean playerWinByCheckmateFlag = false;
    public boolean playerWinOnTimeFlag = false;
    public boolean playerLoseByCheckmateFlag = false;
    public boolean playerLoseOnTimeFlag = false;
    public boolean resignFlag = false;

    public AI ai;
    public Counting counter;
    public Core core;
    public Simulator simulator;


    public App() {}

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
     */
    public void setup() {
        settings();
        frameRate(FPS);
        ai = new AI(this);
        core = new Core(this);
        counter = new Counting(this);
        simulator = new Simulator(this);
    }

    /**
     * Initialise the setting of the window size and read the json file to initialize the setting.
    */
    public void settings() {
        try(FileReader reader = new FileReader("config.json")){
            JSONObject config = new JSONObject(reader);
            CELLSIZE = config.getInt("CELLSIZE");
            SIDEBAR = config.getInt("SIDEBAR");
            BOARD_WIDTH = config.getInt("BOARD_WIDTH");
            layout = config.getString("layout");
            FPS = config.getInt("FPS");
            piece_movement_speed = config.getDouble("piece_movement_speed");
            WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
            HEIGHT = BOARD_WIDTH*CELLSIZE;
            board = new Piece[BOARD_WIDTH][BOARD_WIDTH]; //board[y][x]
            playerMiliSeconds = config.getJSONObject("time_controls").getJSONObject("player").getInt("seconds")*1000;
            playerIncreSeconds = config.getJSONObject("time_controls").getJSONObject("player").getInt("increment");
            cpuMiliSeconds = config.getJSONObject("time_controls").getJSONObject("cpu").getInt("seconds")*1000;
            cpuIncreSeconds = config.getJSONObject("time_controls").getJSONObject("cpu").getInt("increment");
            max_movement_time = config.getInt("max_movement_time")*FPS;
            enemy = config.getString("enemy");
        }catch (IOException e){
            System.out.println("Error reading config.json");
            System.exit(1);
        }

        size(WIDTH, HEIGHT);
    }

    /**
     * Read the layout file and corresponding initialize the board.
     */
    private void readLayOut(){
        references.put("p","XXLChess.WhitePawn");
        references.put("r","XXLChess.WhiteRook");
        references.put("n","XXLChess.WhiteKnight");
        references.put("b","XXLChess.WhiteBishop");
        references.put("q","XXLChess.WhiteQueen");
        references.put("k","XXLChess.WhiteKing");
        references.put("h","XXLChess.WhiteArchBishop");
        references.put("c","XXLChess.WhiteCamel");
        references.put("g","XXLChess.WhiteKnightKing");
        references.put("a","XXLChess.WhiteAmazon");
        references.put("e","XXLChess.WhiteChancellor");
        references.put("P","XXLChess.BlackPawn");
        references.put("R","XXLChess.BlackRook");
        references.put("N","XXLChess.BlackKnight");
        references.put("B","XXLChess.BlackBishop");
        references.put("Q","XXLChess.BlackQueen");
        references.put("K","XXLChess.BlackKing");
        references.put("H","XXLChess.BlackArchBishop");
        references.put("C","XXLChess.BlackCamel");
        references.put("G","XXLChess.BlackKnightKing");
        references.put("A","XXLChess.BlackAmazon");
        references.put("E","XXLChess.BlackChancellor");

        try (BufferedReader reader = new BufferedReader(new FileReader(layout))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                int columnNumber = 0;
                for (char ch : line.toCharArray()) {
                    if (ch != ' '){
                        Class<?> clazz = Class.forName(references.get(String.valueOf(ch)));
                        board[lineNumber][columnNumber] = (Piece)clazz.getConstructor(int.class,int.class,App.class).newInstance(columnNumber*CELLSIZE,lineNumber*CELLSIZE,this);
                        if (clazz.equals(WhiteKing.class)){
                            simulator.whiteKingCellX = columnNumber;
                            simulator.whiteKingCellY = lineNumber;
                        }else if (clazz.equals(BlackKing.class)){
                            simulator.blackKingCellX = columnNumber;
                            simulator.blackKingCellY = lineNumber;
                        }
                    }
                    columnNumber++;
                }
                lineNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Update the movalable list for all pieces on the board.
     */
    private void allUpdateMovale(){
        for (int i = 0; i < App.BOARD_WIDTH; i++) {
            for (int j = 0; j < App.BOARD_WIDTH; j++) {
                if (board[i][j] instanceof Piece) {
                    board[i][j].updateMovable(board);
                }
            }
        }
    }

    /**
     * Receive key pressed signal from the keyboard.
    */
    public void keyPressed(){
        if (chooseEnemyFlag == true){
            if (key == 'p'){
                enemy = "player";
                chooseLevelFlag = true;
                chooseEnemyFlag = false;
            }else if (key == 'c'){
                enemy = "cpu";
                chooseLevelFlag = true;
                chooseEnemyFlag = false;
            }
        }

        if (chooseLevelFlag == true){
            if (key == 'n'){
                layout = "level1.txt";
                chooseLevelFlag = false;
                readLayOut();
                allUpdateMovale();
                counter.start();
            }else if(key == 'h'){
                layout  = "level2.txt";
                chooseLevelFlag = false;
                readLayOut();
                allUpdateMovale();
                counter.start();
            }else if (key == 'e'){
                layout  = "level3.txt";
                chooseLevelFlag = false;
                readLayOut();
                allUpdateMovale();
                counter.start();
            }else if (key == 'l'){
                layout  = "level4.txt";
                chooseLevelFlag = false;
                readLayOut();
                allUpdateMovale();
                counter.start();
            }else if (key == 'q'){
                layout = "test1.txt";
                chooseLevelFlag = false;
                readLayOut();
                allUpdateMovale();
                counter.start();
            }else if (key == 'w'){
                layout = "test2.txt";
                chooseLevelFlag = false;
                readLayOut();
                allUpdateMovale();
                counter.start();
            }else if (key == 't'){
                layout = "test3.txt";
                chooseLevelFlag = false;
                readLayOut();
                allUpdateMovale();
                counter.start();
            }else if (key == 'r'){
                layout = "test4.txt";
                chooseLevelFlag = false;
                readLayOut();
                allUpdateMovale();
                counter.start();
            }else if (key == 'y'){
                layout = "test5.txt";
                chooseLevelFlag = false;
                readLayOut();
                allUpdateMovale();
                counter.start();
            }else if (key == 'u'){
                layout = "test6.txt";
                chooseLevelFlag = false;
                readLayOut();
                allUpdateMovale();
                counter.start();
            }
        }

        if (startingFlag == true && endingFlag == false){
            if (key == ESC){
                key = 0;
                resignFlag = true;
            }
        }

        if (endingFlag == true){
            if (key == 'r'){
                endingFlag = false;
                playerWinByCheckmateFlag = false;
                playerWinOnTimeFlag = false;
                playerLoseByCheckmateFlag = false;
                playerLoseOnTimeFlag = false;
                resignFlag = false;
                checked = false;
                movedPiece = null;
                turn = 0;
                for (int i = 0; i < BOARD_WIDTH; i++){
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[i][j] = null;
                    }
                }
                chooseEnemyFlag = true;
            }
        }
    }
    
    /**
     * Receive key released signal from the keyboard.
    */
    public void keyReleased(){

    }

    /**
     * Receive mouse pressed signal from the mouse.
     * @param e The mouse signal received.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(movableFlag == true){
            if (e.getX() < CELLSIZE * BOARD_WIDTH) {
                selectedX = e.getX() - e.getX() % CELLSIZE;
                selectedY = e.getY() - e.getY() % CELLSIZE;
                selectedCellX = selectedX / CELLSIZE;
                selectedCellY = selectedY / CELLSIZE;
            }
            dealWithSelectedPiece();
        }
    }

    /**
     * Once a piece is selected. This method will deal with this piece, like do preMove or reselection.
     */
    private void dealWithSelectedPiece(){
        if (enemy.equals("player")){
            if (selected == false && ((turn == 0 && board[selectedCellY][selectedCellX] instanceof WhitePiece) || (turn == 1 && board[selectedCellY][selectedCellX] instanceof BlackPiece))) {
                selectedPiece = board[selectedCellY][selectedCellX];
                selectedPiece.updateMovable(board);
                selectedPiece.illegalMoveCheck();
                selected = true;
            }else if (selected == true){
                boolean success = selectedPiece.preMove(selectedCellX, selectedCellY);
                selected = false;
                if(!success){
                    selectedPiece = null;
                    if ((turn == 0 && board[selectedCellY][selectedCellX] instanceof WhitePiece) || (turn == 1 && board[selectedCellY][selectedCellX] instanceof BlackPiece)) {
                        selectedPiece = board[selectedCellY][selectedCellX];
                        selectedPiece.updateMovable(board);
                        selectedPiece.illegalMoveCheck();
                        selected = true;
                    }
                }else{
                    originalX = selectedPiece.x;
                    originalY = selectedPiece.y;
                    movedPiece = selectedPiece;
                    selectedPiece = null;
                }
            }
        } else if (enemy.equals("cpu")){
            if (turn == 0){
                if (selected == false && board[selectedCellY][selectedCellX] instanceof WhitePiece) {
                    selectedPiece = board[selectedCellY][selectedCellX];
                    selectedPiece.updateMovable(board);
                    selectedPiece.illegalMoveCheck();
                    selected = true;
                }else if (selected == true){
                    boolean success = selectedPiece.preMove(selectedCellX, selectedCellY);
                    selected = false;
                    if(!success){
                        selectedPiece = null;
                        if (board[selectedCellY][selectedCellX] instanceof WhitePiece) {
                            selectedPiece = board[selectedCellY][selectedCellX];
                            selectedPiece.updateMovable(board);
                            selectedPiece.illegalMoveCheck();
                            selected = true;
                        }
                    }else{
                        originalX = selectedPiece.x;
                        originalY = selectedPiece.y;
                        movedPiece = selectedPiece;
                        selectedPiece = null;
                    }
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * Draw all elements in the game by current frame.
    */
    public void draw() {
        background(255);

        core.showBoard();
        core.showBar();

        if (chooseEnemyFlag){
            chooseEnemy();
        }else if (chooseLevelFlag){
            chooseLevel();
        }else if (!chooseEnemyFlag && !chooseLevelFlag){
            core.trackMove();

            if(endingFlag == false && startingFlag == true) {
                core.showMousePosition();
                if (selected == true && movableFlag == true) {
                    core.showMouseSelected();
                }
            }

            if (checked == true) {
                core.checked(turn);
            }

            if (illegalMoveCountDown != 0){
                core.illegalMoveWhenChecked();
                illegalMoveCountDown --;
            }

            core.showPieces();

            if (startingFlag == false) {
                counter.startingCountDown();
            }else if (startingFlag == true){
                counter.turnCountDown();
            }

            if (endingFlag == false && AIcountdown > 0){
                AIcountdown--;
                if (AIcountdown == 0){
                    ai.select();
                    ai.preMove();
                }
            }

            if (!endingFlag) {
                core.pieceMove();
            }

            if (playerWinByCheckmateFlag == true) {
                playerWinByCheckmate();
                endingFlag = true;
            }else if (playerWinOnTimeFlag == true){
                playerWinOnTime();
                endingFlag = true;
            }else if (playerLoseByCheckmateFlag == true){
                playerLoseByCheckmate();
                endingFlag = true;
            }else if (playerLoseOnTimeFlag == true){
                playerLoseOnTime();
                endingFlag = true;
            }else if (resignFlag == true){
                resign();
                endingFlag = true;
            }
        }
    }

    /**
     * The menu for choosing enemy (player or cpu).
     */
    private void chooseEnemy(){
        textSize(18);
        fill(230,230,230);
        textAlign(LEFT,TOP);
        text("Please select \nyour enemy:",(float)BOARD_WIDTH*CELLSIZE, CELLSIZE);
        text("p: player",(float)BOARD_WIDTH*CELLSIZE, (float)2.1*CELLSIZE);
        text("c: cpu",(float)BOARD_WIDTH*CELLSIZE, (float)2.6*CELLSIZE);
    }

    /**
     * The menu for choosing level (easy, normal or hard).
     */
    private void chooseLevel(){
        textSize(18);
        fill(230,230,230);
        textAlign(LEFT,TOP);
        text("Please select \nyour level:",(float)BOARD_WIDTH*CELLSIZE, CELLSIZE);
        text("n: normal",(float)BOARD_WIDTH*CELLSIZE, (float)2.1*CELLSIZE);
        text("e: esay",(float)BOARD_WIDTH*CELLSIZE, (float)2.6*CELLSIZE);
        text("h: hard",(float)BOARD_WIDTH*CELLSIZE, (float)3.1*CELLSIZE);
        text("l: HELL!",(float)BOARD_WIDTH*CELLSIZE, (float)3.6*CELLSIZE);
    }

    /**
     * Called when player wins by checkmate.
     */
    private void playerWinByCheckmate(){
        textSize(20);
        fill(230,230,230);
        textAlign(LEFT,TOP);
        text("You won by \ncheckmate",(float)BOARD_WIDTH*CELLSIZE, (float)((BOARD_WIDTH - 1) * CELLSIZE / 2 - 1.5*CELLSIZE));
        text("Press 'r' \nto restart",(float)BOARD_WIDTH*CELLSIZE, (float)((BOARD_WIDTH - 1) * CELLSIZE / 2 + 1.5*CELLSIZE));
    }

    /**
     * Called when player wins on time.
     */
    private void playerWinOnTime(){
        textSize(20);
        fill(230,230,230);
        textAlign(LEFT,TOP);
        text("You won on \ntime",(float)BOARD_WIDTH*CELLSIZE, (float)((BOARD_WIDTH - 1) * CELLSIZE / 2 - 1.5*CELLSIZE));
        text("Press 'r' \nto restart",(float)BOARD_WIDTH*CELLSIZE, (float)((BOARD_WIDTH - 1) * CELLSIZE / 2 + 1.5*CELLSIZE));
    }

    /**
     * Called when player loses by checkmate.
     */
    private void playerLoseByCheckmate(){
        textSize(20);
        fill(230,230,230);
        textAlign(LEFT,TOP);
        text("You lost by \ncheckmate",(float)BOARD_WIDTH*CELLSIZE, (float)((BOARD_WIDTH - 1) * CELLSIZE / 2 - 1.5*CELLSIZE));
        text("Press 'r' \nto restart",(float)BOARD_WIDTH*CELLSIZE, (float)((BOARD_WIDTH - 1) * CELLSIZE / 2 + 1.5*CELLSIZE));
    }

    /**
     * Called when player loses on time.
     */
    public void playerLoseOnTime(){
        textSize(20);
        fill(230,230,230);
        textAlign(LEFT,TOP);
        text("You lost on \ntime",(float)BOARD_WIDTH*CELLSIZE, (float)((BOARD_WIDTH - 1) * CELLSIZE / 2 - 1.5*CELLSIZE));
        text("Press 'r' \nto restart",(float)BOARD_WIDTH*CELLSIZE, (float)((BOARD_WIDTH - 1) * CELLSIZE / 2 + 1.5*CELLSIZE));
    }

    /**
     * Called when player press "ESC" to resign the game.
     */
    private void resign(){
        textSize(20);
        fill(230,230,230);
        textAlign(LEFT,TOP);
        text("You resigned",(float)BOARD_WIDTH*CELLSIZE, (float)((BOARD_WIDTH - 1) * CELLSIZE / 2 - 1.5*CELLSIZE));
        text("Press 'r' \nto restart",(float)BOARD_WIDTH*CELLSIZE, (float)((BOARD_WIDTH - 1) * CELLSIZE / 2 + 1.5*CELLSIZE));
    }

    public static void main(String[] args) {
        PApplet.main("XXLChess.App");
    }
}
