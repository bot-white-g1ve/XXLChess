package XXLChess;

import processing.core.*;
import java.util.List;
import java.util.ArrayList;

/**
 * This class is used to deal with all the pieces' movement.
 */
public class ShowPieceMove{

    static int BW = App.BOARD_WIDTH;
    static int CS = App.CELLSIZE;

    /**
     * Indicates a cell to be movable.
     */
    public static void cellMovable(PApplet app,int CX, int CY){
        app.fill(135, 206, 235,150);
        app.rect(CX*CS, CY*CS, CS, CS);
    }

    /**
     * Indicates a cell to be eatable.
     */
    public static void cellEatable(PApplet app,int CX, int CY){
        app.fill(255, 165, 0, 150);
        app.rect(CX*CS, CY*CS, CS, CS);
    }

    /**
     * Indicates a cell that a piece moved from.
     */
    public static void cellMoved(Piece piece, PApplet app,int orgX, int orgY){
        app.stroke(0,0,0,0);
        app.fill(170,162,58,200);
        app.rect(piece.x, piece.y, CS, CS);
        app.rect(orgX, orgY, CS, CS);
    }

    /**
     * Update the movable list for rook.
     */
    public static List<int[]> updateRookMovable(Piece piece,Piece[][] board){
        List<int[]> movableList = new ArrayList<>();
        try {
            Class<?> opposite = (piece instanceof WhitePiece) ? Class.forName("XXLChess.BlackPiece") : Class.forName("XXLChess.WhitePiece");
            for (int i = 1; i < 15; i++) {
                if (piece.x/CS + i <= BW - 1) {
                    if (board[piece.y/CS][piece.x/CS + i] == null) {
                        movableList.add(new int[]{piece.x/CS + i, piece.y/CS});
                    } else if (opposite.isInstance(board[piece.y/CS][piece.x/CS + i])) {
                        movableList.add(new int[]{piece.x/CS + i, piece.y/CS});
                        break;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            for (int i = 1; i < 15; i++) {
                if (piece.x/CS - i >= 0) {
                    if (board[piece.y/CS][piece.x/CS - i] == null) {
                        movableList.add(new int[]{piece.x/CS - i, piece.y/CS});
                    } else if (opposite.isInstance(board[piece.y/CS][piece.x/CS - i])) {
                        movableList.add(new int[]{piece.x/CS - i, piece.y/CS});
                        break;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            for (int i = 1; i < 15; i++) {
                if (piece.y/CS + i <= BW - 1) {
                    if (board[piece.y/CS + i][piece.x/CS] == null) {
                        movableList.add(new int[]{piece.x/CS, piece.y/CS + i});
                    } else if (opposite.isInstance(board[piece.y/CS+i][piece.x/CS])) {
                        movableList.add(new int[]{piece.x/CS, piece.y/CS + i});
                        break;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            for (int i = 1; i < 15; i++) {
                if (piece.y/CS - i >= 0) {
                    if (board[piece.y/CS - i][piece.x/CS] == null) {
                        movableList.add(new int[]{piece.x/CS, piece.y/CS - i});
                    } else if (opposite.isInstance(board[piece.y/CS-i][piece.x/CS])) {
                        movableList.add(new int[]{piece.x/CS, piece.y/CS - i});
                        break;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return movableList;
    }

    /**
     * Update the movable list for bishop.
     */
    public static List<int[]> updateBishopMovable(Piece piece,Piece[][] board){
        List<int[]> movableList = new ArrayList<>();
        try {
            Class<?> opposite = (piece instanceof WhitePiece) ? Class.forName("XXLChess.BlackPiece") : Class.forName("XXLChess.WhitePiece");
            for (int i = 1; i < 15; i++) {
                if (piece.x/CS + i <= BW - 1 && piece.y/CS + i <= BW - 1) {
                    if (board[piece.y/CS + i][piece.x/CS + i] == null) {
                        movableList.add(new int[]{piece.x/CS + i, piece.y/CS + i});
                    } else if (opposite.isInstance(board[piece.y/CS + i][piece.x/CS + i])) {
                        movableList.add(new int[]{piece.x/CS + i, piece.y/CS + i});
                        break;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            for (int i = 1; i < 15; i++) {
                if (piece.x/CS - i >= 0 && piece.y/CS - i >= 0) {
                    if (board[piece.y/CS - i][piece.x/CS - i] == null) {
                        movableList.add(new int[]{piece.x/CS - i, piece.y/CS - i});
                    } else if (opposite.isInstance(board[piece.y/CS - i][piece.x/CS - i])) {
                        movableList.add(new int[]{piece.x/CS - i, piece.y/CS - i});
                        break;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            for (int i = 1; i < 15; i++) {
                if (piece.y/CS + i <= BW - 1 && piece.x/CS - i >= 0) {
                    if (board[piece.y/CS + i][piece.x/CS - i] == null) {
                        movableList.add(new int[]{piece.x/CS - i, piece.y/CS + i});
                    } else if (opposite.isInstance(board[piece.y/CS + i][piece.x/CS - i])) {
                        movableList.add(new int[]{piece.x/CS - i, piece.y/CS + i});
                        break;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            for (int i = 1; i < 15; i++) {
                if (piece.y/CS - i >= 0 && piece.x/CS + i <= BW - 1) {
                    if (board[piece.y/CS - i][piece.x/CS + i] == null) {
                        movableList.add(new int[]{piece.x/CS + i, piece.y/CS - i});
                    } else if (opposite.isInstance(board[piece.y/CS - i][piece.x/CS + i])) {
                        movableList.add(new int[]{piece.x/CS + i, piece.y/CS - i});
                        break;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return movableList;
    }

    /**
     * Update the movable list for knight.
     */
    public static List<int[]> updateKnightMovable(Piece piece,Piece[][] board){
        List<int[]> movableList = new ArrayList<>();
        try {
            Class<?> opposite = (piece instanceof WhitePiece) ? Class.forName("XXLChess.BlackPiece") : Class.forName("XXLChess.WhitePiece");
            int[][] positions = {{2,1},{-2,1},{1,2},{-1,2},{2,-1},{-2,-1},{1,-2},{-1, -2}};
            for (int[] pos : positions) {
                try{
                    if (board[piece.y/CS + pos[1]][piece.x/CS + pos[0]] == null) {
                        movableList.add(new int[]{piece.x/CS + pos[0], piece.y/CS + pos[1]});
                    } else if (opposite.isInstance(board[piece.y/CS + pos[1]][piece.x/CS + pos[0]])) {
                        movableList.add(new int[]{piece.x/CS + pos[0], piece.y/CS + pos[1]});
                    }
                }catch (ArrayIndexOutOfBoundsException e){
                    continue;
                }
            }
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return movableList;
    }

    /**
     * Update the movable list for camel.
     */
    public static List<int[]> updateCamelMovable(Piece piece,Piece[][] board){
        List<int[]> movableList = new ArrayList<>();
        try {
            Class<?> opposite = (piece instanceof WhitePiece) ? Class.forName("XXLChess.BlackPiece") : Class.forName("XXLChess.WhitePiece");
            int[][] positions = {{3,1},{-3,1},{1,3},{-1,3},{3,-1},{-3,-1},{1,-3},{-1, -3}};
            for (int[] pos : positions) {
                try{
                    if (board[piece.y/CS + pos[1]][piece.x/CS + pos[0]] == null) {
                        movableList.add(new int[]{piece.x/CS + pos[0], piece.y/CS + pos[1]});
                    } else if (opposite.isInstance(board[piece.y/CS + pos[1]][piece.x/CS + pos[0]])) {
                        movableList.add(new int[]{piece.x/CS + pos[0], piece.y/CS + pos[1]});
                    }
                }catch (ArrayIndexOutOfBoundsException e){
                    continue;
                }
            }
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return movableList;
    }

    /**
     * Update the movable list for king.
     */
    public static List<int[]> updateKingMovable(Piece piece,Piece[][] board){
        List<int[]> movableList = new ArrayList<>();
        try {
            Class<?> opposite = (piece instanceof WhitePiece) ? Class.forName("XXLChess.BlackPiece") : Class.forName("XXLChess.WhitePiece");
            int[][] positions = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{-1,1},{-1,-1},{1, -1}};
            for (int[] pos : positions) {
                try{
                    if (board[piece.y/CS + pos[1]][piece.x/CS + pos[0]] == null) {
                        movableList.add(new int[]{piece.x/CS + pos[0], piece.y/CS + pos[1]});
                    } else if (opposite.isInstance(board[piece.y/CS + pos[1]][piece.x/CS + pos[0]])) {
                        movableList.add(new int[]{piece.x/CS + pos[0], piece.y/CS + pos[1]});
                    }
                }catch (ArrayIndexOutOfBoundsException e){
                    continue;
                }
            }
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        /*
        Class<?> myRook = (piece instanceof WhitePiece)?WhiteRook.class:BlackRook.class;
        int pieceYDivCS = piece.y / CS;
        if (!piece.moved) {
            for (int i = 0; i < BW; i++) {
                if (myRook.isInstance(board[pieceYDivCS][i]) && !board[pieceYDivCS][i].moved) {
                    if ((i > piece.x / CS)&&piece.x/CS < BW-3&&(board[pieceYDivCS][piece.x/CS+1] == null && board[pieceYDivCS][piece.x/CS+2] == null)){
                        movableList.add(new int[]{piece.x/CS+2, pieceYDivCS});
                    }else if ((i < piece.x / CS)&&piece.x/CS>2&&(board[pieceYDivCS][piece.x/CS-1] == null && board[pieceYDivCS][piece.x/CS+2] == null)) {
                        movableList.add(new int[]{piece.x/CS-2, pieceYDivCS});
                    }
                }
            }
        }

         */
        return movableList;
    }

    /**
     * Update the movable list for pawn.
     */
    public static List<int[]> updatePawnMovable(Piece piece, Piece[][] board){
        List<int[]> movableList = new ArrayList<>();
        try{
            Class<?> opposite = (piece instanceof WhitePiece)?Class.forName("XXLChess.BlackPiece"):Class.forName("XXLChess.WhitePiece");
            int direction = (piece instanceof WhitePiece)?-1:1;
            int startLine = (piece instanceof WhitePiece)?576:48;
            if (board[piece.y/CS + direction][piece.x/CS] == null) {
                movableList.add(new int[]{piece.x/CS, piece.y/CS+direction});
                if (piece.moved == false && piece.y == startLine && board[piece.y/CS+2*direction][piece.x/CS] == null) {
                    movableList.add(new int[]{piece.x/CS, piece.y/CS +2*direction});
                }
            }
            if (opposite.isInstance(board[piece.y/CS + direction][piece.x/CS + 1])) {
                movableList.add(new int[]{piece.x/CS + 1, piece.y/CS+direction});
            }
            if (opposite.isInstance(board[piece.y/CS + direction][piece.x/CS - 1])) {
                movableList.add(new int[]{piece.x/CS - 1, piece.y/CS+direction});
            }
        }catch (ArrayIndexOutOfBoundsException e){
        }catch (ClassNotFoundException e){
            e.printStackTrace();
            System.exit(1);
        }
        return movableList;
    }
}
