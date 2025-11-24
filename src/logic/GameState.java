package logic;

import szte.mi.Move;

import java.util.ArrayList;
import java.util.List;


public class GameState {


    //properties always have a semicolon at the end
    private static final Move[] DIRECTIONS = new Move[]{
            new Move(1, 0),
            new Move(1, 1),
            new Move(0, 1),
            new Move(-1, 1),
            new Move(-1, 0),
            new Move(-1, -1),
            new Move(0, -1),
            new Move(1, -1),
    };

    public enum SlotType {
        Empty,
        White,
        Black,
    }

    //it stores slot types
    private SlotType board[][] = new SlotType[8][8];
    private SlotType playerColor;
    private SlotType opponentColor;
    private int playerCount;
    private int opponentCount;


    public GameState(int order) {
        if (order == 1) {
            this.playerColor = SlotType.White;
            this.opponentColor = SlotType.Black;
        } else if (order == 0) {
            this.playerColor = SlotType.Black;
            this.opponentColor = SlotType.White;
        } else {
            System.out.println("Error: wrong order received");
        }

        this.initializeBoard();
    }

    public GameState(GameState gameState, boolean switchPerspective) {
        // COPY BOARD (VALUES ONLY) ( efficient manner = idk :( )
        for (int i = 0; i < this.board.length; i++) { //this equals to the row in our matrix. //
            for (int j = 0; j < this.board[0].length; j++) { //this equals to the column in each row.
                this.board[i][j] = gameState.board[i][j];
            }
        }

        this.playerColor = gameState.playerColor;
        this.opponentColor = gameState.opponentColor;
        this.playerCount = gameState.playerCount;
        this.opponentCount = gameState.opponentCount;

        if (switchPerspective) {
            SlotType auxColor = playerColor;
            playerColor = opponentColor;
            opponentColor = auxColor;


            int auxCount = playerCount;
            playerCount = opponentCount;
            opponentCount = auxCount;
        }
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getOpponentCount() {
        return opponentCount;
    }

    private void initializeBoard() {
        for (int i = 0; i < board.length; i++) { //this equals to the row in our matrix. //
            for (int j = 0; j < board[0].length; j++) { //this equals to the column in each row.
                board[i][j] = SlotType.Empty;
            }
        }

        board[3][3] = SlotType.White;
        board[3][4] = SlotType.Black;
        board[4][3] = SlotType.Black;
        board[4][4] = SlotType.White;

        playerCount = 2;
        opponentCount = 2;
    }

    public void printBoard() {

        System.out.println(playerColor + ": " + playerCount + " ; " + opponentColor + ": " + opponentCount);
        for (int i = 0; i < board.length; i++) { //this equals to the row in our matrix. //
            for (int j = 0; j < board[0].length; j++) { //this equals to the column in each row.
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    //checking if at the end of the corner there is an empty or the opponent player.
    // Returns true or false if the given move is a possible move or not.
    public boolean checkLegalMove(Move move) {
        for (Move direction : DIRECTIONS) {
            if (findInDirection(move, direction, playerColor)) {
                return true;
            }
        }

        return false;
    }

    // Returns true if it finds opponentColor in the given direction
    private boolean findInDirection(Move start, Move direction, SlotType playerColor) {
        Move current = new Move(start.x + direction.x, start.y + direction.y);
        boolean lookingForReverted = true;

        //boundaries
        while (current.x < 8 && current.y < 8 && current.x >= 0 && current.y >= 0) {
            SlotType currentSlot = board[current.x][current.y];

            if (lookingForReverted) {
                if (currentSlot == playerColor || currentSlot == SlotType.Empty) return false;
                lookingForReverted = false;
            } else {
                if (currentSlot == SlotType.Empty) return false; //only if empty
                if (currentSlot == playerColor) return true;
            }

            current = new Move(current.x + direction.x, current.y + direction.y);
        }

        return false;
    }

    public SlotType[][] getBoard(){
        return board;
    }

    // Updates board
    public void updateBoard(Move move, boolean isOpponent) {
        board[move.x][move.y] = isOpponent ? opponentColor : playerColor;
        int flippedPieces = 0;

        for (Move direction : DIRECTIONS) {
            if (isOpponent) {
                // opponent's perspective
                if (findInDirection(move, direction, opponentColor)) {
                    flippedPieces += flipInDirection(move, direction, opponentColor);
                }

            } else {
                // player's perspective
                if (findInDirection(move, direction, playerColor)) {
                    flippedPieces += flipInDirection(move, direction, playerColor);
                }
            }
        }

        if (isOpponent) {
            opponentCount += flippedPieces + 1; // because you placed already 1 piece, so +1
            playerCount -= flippedPieces;
        } else {
            playerCount += flippedPieces + 1; // because you placed already 1 piece, so +1
            opponentCount -= flippedPieces;
        }
    }

    public boolean canStillMove() {
        List<Move> possibleMoves = getPossibleMoves();
        return possibleMoves.size() > 0;
    }

    // Flip pieces in given direction, return the number of flipped pieces
    private int flipInDirection(Move start, Move direction, SlotType playerColor) {
        int count = 0;
        Move current = new Move(start.x + direction.x, start.y + direction.y);

        while (current.x < 8 && current.y < 8 && current.x >= 0 && current.y >= 0) {
            SlotType currentSlot = board[current.x][current.y];

            if (currentSlot == playerColor) break; //stop if is the same color
            board[current.x][current.y] = playerColor; //it flips the slot
            count++;
            current = new Move(current.x + direction.x, current.y + direction.y);
        }

        return count;
    }

    // Get list of all possible moves
    public List<Move> getPossibleMoves() {

        // List = dynamic array (it changes its size while the program is running)
        List<Move> possibleMoves = new ArrayList<Move>();

        for (int i = 0; i < board.length; i++) { // this equals to the row in our matrix
            for (int j = 0; j < board[0].length; j++) { // this equals to the column in each row
                Move move = new Move(i, j);
                if (board[i][j] == SlotType.Empty && checkLegalMove(move)) {
                    possibleMoves.add(move);
                }
            }
        }

        return possibleMoves;
    }

}

