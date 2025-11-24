package logic;

import szte.mi.Move;
import szte.mi.Player;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class PlayerHuman implements Player {
    private Scanner consoleInput = new Scanner(System.in);
    private GameState gameState;

    @Override
    public void init(int order, long t, Random rnd) {
        this.gameState = new GameState(order);
    }

    // No longer needed, because we dont get the move from the console
    @Override
    public Move nextMove(Move prevMove, long tOpponent, long t) {
        return null;
    }
    //update move from opponent
    public void opponentMoved(Move opponentMove){
        //update gameState if not the first move
        if (opponentMove != null) {
            gameState.updateBoard(opponentMove, true);
        }
    }

    public boolean canStillMove(){
        return gameState.canStillMove();
    }

    public List<Move> getPossibleMoves(){
        return gameState.getPossibleMoves();
    }

    public int getScore(){
        return gameState.getPlayerCount();
    }

    public int getOpponentScore(){
        return gameState.getOpponentCount();
    }

    public GameState.SlotType[][] getBoard(){
        return gameState.getBoard();
    }

    public void move(Move currentMove) {
        gameState.updateBoard(currentMove, false);
    }
}
