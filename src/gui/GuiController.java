package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogEvent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import logic.GameController;
import logic.GameState;
import szte.mi.Move;

import java.net.URL;
import java.util.*;


public class GuiController implements Initializable {

    // GUI attributes, elements to link to the GUI
    public BorderPane borderPane;
    public GridPane gridPane;
    private Map<GameState.SlotType, Image> slotImages;
    private Image possibleSlotImage;
    private ImageView[][] slots;
    private boolean isGameInteractable;

    // Logic attributes
    private GameController gameController;

    public GuiController() {
        this.slotImages = new HashMap<>();
        this.slotImages.put(GameState.SlotType.Empty, new Image("/assets/slot-empty.jpg"));
        this.slotImages.put(GameState.SlotType.Black, new Image("/assets/slot-player1.jpg"));
        this.slotImages.put(GameState.SlotType.White, new Image("/assets/slot-player2.jpg"));
        this.possibleSlotImage = new Image("/assets/slot-possible.jpg");

        slots = new ImageView[8][8];
        isGameInteractable = false;

        gameController = new GameController();
    }

    public void startGame() {
        gameController.startGame();
        initializeBoardGUI();
        highlightPossibleMoves();
        isGameInteractable = true;

    }

    private void initializeMainMenuGUI() {
        var vbox = new VBox();
        var title = new Text();
        var startButton = new Button();
        var logo = new ImageView();

        logo.setImage(new Image("/assets/title.png"));

        title.setText("Othello - Chiara");
        title.setTextAlignment(TextAlignment.CENTER);
        title.setStyle("-fx-text-fill:white; -fx-font-size: 24px; -fx-font-weight: bold");

        startButton.setText("Start Game");
        startButton.setPrefSize(200, 50);
        startButton.setStyle("-fx-font-size: 24px;");
        startButton.setAlignment(Pos.CENTER);
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                startGame();
            }
        });

        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.getChildren().add(logo);
        vbox.getChildren().add(title);
        vbox.getChildren().add(startButton);
        borderPane.setCenter(vbox);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMainMenuGUI();

        // Initialize slots
        String slotBase = "#slot_";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String slotName = slotBase + i + j;
                ImageView slot = (ImageView) gridPane.lookup(slotName);
                slot.addEventHandler(MouseEvent.MOUSE_CLICKED, new CustomOnClickEventHandler(this));
                slots[i][j] = slot;
            }
        }
    }


    public void onStartGame() {
        startGame();

    }

    public void onExitGame() {
        System.exit(0);

    }

    public void onAbout(){
        // About Alert Box
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setContentText("Chiara Laurenza, Othello, ProPra, December 2022");
        alert.show();
    }


    // Invoced every time slot/immageview is clicked
    public void onSlotClicked(ImageView imageView) {
        System.out.println(imageView.getId());
        if (isGameInteractable == false) return;

        // Convert ImageView to Move
        Move playerHumanMove = getMoveFromId(imageView.getId());

        // Checks for illegal move
        if (!gameController.isPlayerHumanLegalMove(playerHumanMove)) {
            System.out.println("The move is not legal");
            return;
        }

        // Move HumanPlayer (logic + GUI)
        gameController.onPlayerHumanMove(playerHumanMove);
        syncBoardGUI();

        // Move AiPlayer (logic + GUI)
        Move aiMove = gameController.movePlayerAI(playerHumanMove);
        syncBoardGUI();

        // handle next turn logic
        gameController.nextTurn(aiMove);
        highlightPossibleMoves();

        if (gameController.hasGameEnded()) {
            isGameInteractable = false;

            // End Game Alert Box
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Ended");
            alert.setContentText(gameController.getScore());
            alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                @Override
                public void handle(DialogEvent dialogEvent) {
                    initializeMainMenuGUI();
                }
            });
            alert.show();
        }

    }

    private void initializeBoardGUI() {
        borderPane.setCenter(gridPane);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                slots[i][j].setImage(slotImages.get(GameState.SlotType.Empty));
            }
        }

        slots[3][3].setImage(slotImages.get(GameState.SlotType.White));
        slots[4][4].setImage(slotImages.get(GameState.SlotType.White));
        slots[3][4].setImage(slotImages.get(GameState.SlotType.Black));
        slots[4][3].setImage(slotImages.get(GameState.SlotType.Black));
    }


    // Pass the id of the image view and get the MOVE. Transition from gui(ImageView) to logic(Move)
    private Move getMoveFromId(String id) {
        int underscoreIndex = id.indexOf('_');
        int x = id.charAt(underscoreIndex + 1) - '0';
        int y = id.charAt(underscoreIndex + 2) - '0';
        return new Move(x, y);
    }


    private void syncBoardGUI() {
        var board = gameController.getPlayerHumanBoard();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                slots[i][j].setImage(slotImages.get(board[i][j]));
            }
        }

    }

    private void highlightPossibleMoves() {
        List<Move> possibleMoves = gameController.getPlayerHumanPossibleMoves();

        for (Move move : possibleMoves) {
            slots[move.x][move.y].setImage(possibleSlotImage);
        }
    }

}
