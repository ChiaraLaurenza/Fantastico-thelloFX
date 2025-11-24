package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application { //nur controller aufrufen

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../gui/OthelloGui.fxml"));

        // Stage properties
        primaryStage.setTitle("Othello Laurenza");
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
        primaryStage.setMaxWidth(1000);
        primaryStage.setMaxHeight(1000);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(1000);

        primaryStage.setScene(new Scene(root, 1000, 1000, Color.BLACK));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
