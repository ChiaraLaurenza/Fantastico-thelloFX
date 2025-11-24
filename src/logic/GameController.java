package logic;

import szte.mi.Move;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameController {

    PlayerHuman playerHuman;
    PlayerAI playerAI;
    boolean gameEnded;
    Random randomEngine; //only AI uses it
    int turn;
    List<Move> playerHumanPossibleMoves;


    public boolean hasGameEnded() {
        return gameEnded;
    }

    private void initializeGame() {
        // initialize members
        gameEnded = false;
        turn = 1;
        randomEngine = new Random();

        // initialize players
        playerHuman = new PlayerHuman();
        playerAI = new PlayerAI();
        playerHuman.init(0, 0, randomEngine);
        playerAI.init(1, 0, randomEngine);

        // populate humanPossibleMoves
        playerHumanPossibleMoves = playerHuman.getPossibleMoves();

        System.out.println("[INFO] Game initialized. Starting game...");
    }

    // Starts the game
    public void startGame() {
        // initialize game before starting
        initializeGame();

        System.out.println("[GAME] Player Human (BLACK) moves...");
    }

    // Call whenever human player moves
    public void onPlayerHumanMove(Move move) {
        // PlayerHuman moves...
        playerHuman.move(move);

    }

    public Move movePlayerAI(Move prevMove) {
        // PlayerAI moves...
        System.out.println("[GAME] Player AI (WHITE) moves...");
        Move aiMove = playerAI.nextMove(prevMove, 0, 0);

        // UpdatePlayerHuman state based on aiMove
        playerHuman.opponentMoved(aiMove);
        playerHumanPossibleMoves = playerHuman.getPossibleMoves();

        return aiMove;
    }

    public void nextTurn(Move aiMove) {
        // Check for end condition
        if (aiMove == null || playerHuman.canStillMove() == false) {
            gameEnded = true;
        }

        // Next turn
        turn++;

    }

    public String getScore() {
        return "Human: " + playerHuman.getScore() + " | AI: " + playerHuman.getOpponentScore();
    }

    public boolean isPlayerHumanLegalMove(Move move) {

        for (Move possibleMove : playerHumanPossibleMoves) {
            if (possibleMove.x == move.x && possibleMove.y == move.y) {

                return true;
            }
        }

        return false;
    }


    public List<Move> getPlayerHumanPossibleMoves() {
        return playerHuman.getPossibleMoves();
    }

    //from playerHuman get the board
    public GameState.SlotType[][] getPlayerHumanBoard() {
        return playerHuman.getBoard();
    }


    //choose if you want to play with a Player or with a Machine
    public void playerOrMachine(String userChoice) {

        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("choose if you want to play with a Player a Machine:");

        // TODO: Implement choice logic

        String choice = myObj.nextLine();  // Read user input
    }


    //TODO: choose the difficulty of the game [Easy, intermediate, Advance]

    //TODO:Visualize the legalMoves


}
