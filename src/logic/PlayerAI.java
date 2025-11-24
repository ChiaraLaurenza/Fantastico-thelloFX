package logic;

import szte.mi.Move;


import java.util.List;
import java.util.Random;

public class PlayerAI implements szte.mi.Player {

    private GameState gameState;
    private Random randomEngine;

    // Constant (capital letters)
    private final int MAX_DEPTH = 3; // The search depth of the Minimax algorithm (exponential growth)
    private final int VALUE_CORNER = 20;
    private final int VALUE_EDGE = 15;
    private final int VALUE_CENTER = 5;
    private final int VALUE_AROUND_CORNER = -20;

    @Override
    public void init(int order, long t, Random rnd) {
        this.gameState = new GameState(order);
        this.randomEngine = rnd;
    }

    @Override
    public Move nextMove(Move prevMove, long tOpponent, long t) {
        // Update gameStatus (if not the first move)
        if (prevMove != null) {
            gameState.updateBoard(prevMove, true);
            if (gameState.canStillMove() == false) {
                return null;
            }
        }

        // Get best move
        // gameState.printBoard();
        Move bestMove = getBestMove();

        // Update board (based on bestMove)
        if (bestMove != null) {
            System.out.println("AI Move " + "(x: " + bestMove.x + " y: " + bestMove.y + ")");
            gameState.updateBoard(bestMove, false);
        }

        return bestMove;
    }


    public Move getBestMove() {
        List<Move> possibleMoves = gameState.getPossibleMoves();

        // Check if no possible moves are left
        if (possibleMoves.size() == 0) {
            return null;
        }

//        int maxValue = Integer.MIN_VALUE;
//        Move bestMove = null;
//        int moveIndex = 1;

//        for (Move possibleMove : possibleMoves) {
//            GameState possibleState = new GameState(gameState, false); // copy current game state
//            possibleState.updateBoard(possibleMove, false); // apply possible move on the copied state
//            int value = expandStateUtility(possibleState, MAX_DEPTH);
//
//            // Add value points based on move location (corner/edge/center)
//            if (isCorner(possibleMove)) value += VALUE_CORNER;
//            else if (isEdge(possibleMove)) value += VALUE_EDGE;
//            else if (isCenter(possibleMove)) value += VALUE_CENTER;
//            else if (isAroundCorner(possibleMove)) value += VALUE_AROUND_CORNER;
//
//            if (value > maxValue) {
//                maxValue = value;
//                bestMove = possibleMove;
//            }
//
//            System.out.println("Considered move " + moveIndex++ + "/" + possibleMoves.size());
//            System.out.println(">>> VALUE: " + value);
//        }

        // EXPERIMENTAL ================================================================================================

        // Corner
        for (Move possibleMove : possibleMoves) {
            if (isCorner(possibleMove)) return possibleMove;
        }

        // Edge
        for (Move possibleMove : possibleMoves) {
            if (isEdge(possibleMove)) return possibleMove;
        }

        // Center
        for (Move possibleMove : possibleMoves) {
            if (isCenter(possibleMove)) return possibleMove;
        }

        // Avoid around corner
        for (Move possibleMove : possibleMoves) {
            if (!isAroundCorner(possibleMove)) return possibleMove;
        }

        // If nothing left, random
        int min = 0;
        int max = possibleMoves.size() - 1;
        int rndMove = min + randomEngine.nextInt(max - min + 1);
        return possibleMoves.get(rndMove);

        // =============================================================================================================

//        return bestMove;
    }

    private int gameStateUtility(GameState gameState) {
        return gameState.getPlayerCount() - gameState.getOpponentCount();
    }

    //in the recursive method we will return values
    private int expandStateUtility(GameState currentState, int searchDepth) {
        if (searchDepth == 0 || currentState.canStillMove() == false) {
            return gameStateUtility(currentState);
        }

        searchDepth--;
        List<Move> possibleMoves = currentState.getPossibleMoves();
        int maxValue = Integer.MIN_VALUE;

        for (Move possibleMove : possibleMoves) {
            GameState possibleState = new GameState(currentState, true); // copy current game state
            possibleState.updateBoard(possibleMove, false); // apply possible move on the copied state
            int value = expandStateUtility(possibleState, searchDepth);

            if (value > maxValue) {
                maxValue = value;
            }
        }

        return maxValue;
    }

    // Is corner move
    private boolean isCorner(Move move) {
        return ((move.x == 0 && move.y == 0) || (move.x == 7 && move.y == 7) || (move.x == 7 && move.y == 0) || (move.x == 0 && move.y == 7));
    }

    // Is edge move
    private boolean isEdge(Move move) {
        return ((move.x >= 2 && move.x <= 5) || (move.y >= 2 && move.y <= 5));
    }

    // Is center move
    private boolean isCenter(Move move) {
        return (move.x < 6 && move.y < 6 && move.x >= 2 && move.y >= 2);
    }

    // Is around corner move
    private boolean isAroundCorner(Move move) {
        return ((move.x == 0 && move.y == 1) || (move.x == 1 && move.y == 0) || (move.x == 1 && move.y == 1) ||
                (move.x == 0 && move.y == 6) || (move.x == 1 && move.y == 6) || (move.x == 1 && move.y == 7) ||
                (move.x == 6 && move.y == 0) || (move.x == 6 && move.y == 1) || (move.x == 7 && move.y == 1) ||
                (move.x == 6 && move.y == 6) || (move.x == 6 && move.y == 7) || (move.x == 7 && move.y == 6));
    }
}


