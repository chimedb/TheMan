package Pacman;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalTime;


public class AlertBox {

    private static boolean returned = true;
    private static Stage window;

    Board _board;
    int result;
    TextField field;

    public AlertBox(Board board)
    {
        _board = board;
    }

    public void display(String title, String message) {
        CleanBoard();
        window = new Stage();

        window.initModality(Modality.WINDOW_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label enterYourName = new Label("Enter your name: ");
        field = new TextField("Anonim");


        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Quit The Game");
        Button playAgain = new Button("Play Again");
        closeButton.setOnAction(e->EndGame(window));
        playAgain.setOnAction(e->PlayAgain(window));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(enterYourName, field);
        layout.getChildren().addAll(label, closeButton, playAgain);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }

    private void CleanBoard()
    {
        result = _board.game.controller.getPoints();
        _board.game.StopTimers();
        _board.game.CleanBoard();
    }

    private void StartNewGame()
    {
        Game game = _board.game;
        game.CreateBoard(LocalTime.now());
        game.InitializeController();
        _board = game._board;
    }

    private void PlayAgain(Stage window)
    {
        StartNewGame();
        AfterEnding(window);
    }

    private void EndGame(Stage window)
    {
        _board.game.StopTimers();
        AfterEnding(window);
    }

    private void AfterEnding(Stage window)
    {
        String name = field.getText();
        _board.game.controller.AddHighScores(name,result);
        window.close();
    }
}
