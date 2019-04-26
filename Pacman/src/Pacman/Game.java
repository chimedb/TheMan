package Pacman;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Game extends Application {

    Group group;
    Scene scene;

    Board _board;

    int x, y, width, height;
    Timer timerPlayer;
    Timer timer;

    Rectangle boardRect;
    ViewController controller;
    FXMLLoader loader;
    Stage stage;
    Group firstGroup;

    int duration = 100;

    private void LoadRoot() throws Exception {
        loader = new FXMLLoader(getClass().getResource("view.fxml"));
        Parent root = loader.load();
        firstGroup = new Group();
        firstGroup.getChildren().add(root);
    }

    private void SetScene(Stage primaryStage)
    {
        scene = new Scene(firstGroup, 850, 420);
        primaryStage.setTitle("Man In The Forest");
        primaryStage.setScene(scene);
        width = (int)scene.getWidth() - 220;
        height = (int)scene.getHeight();
        stage = primaryStage;
    }

    private void InitializeController(Stage primaryStage)
    {
        controller = (ViewController)loader.getController();
        controller.SetStage(primaryStage);
    }

    private void Sleep(int ms)
    {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LocalTime localtime = LocalTime.now();
        LoadRoot();
        SetScene(primaryStage);
        group = new Group();
        firstGroup.getChildren().addAll(group);
        InitializeController(primaryStage);

        primaryStage.setOnHidden(e -> Platform.exit());
        primaryStage.show();
        CreateBoard(LocalTime.now());
    }

    public void ChangeLevel()
    {
        Platform.runLater(()->{
            StopTimers();
            duration = (int)(duration*0.95);
            Integer level = controller.getLevel();
            if (level < 10) controller.setLevel(level+1);
            else EndWonGame();
            CleanBoard();
            CreateBoard(LocalTime.now());
        });
    }

    private void EndWonGame()
    {
        controller.AddPoints(100);
        Sleep(3);
        AlertBox alertBox = new AlertBox(_board);
        alertBox.display("You won", "Your score is " + controller.getPoints());
    }

    public void CleanBoard()
    {
        //group = new Group();
        //firstGroup.getChildren().remove(group);
        group.getChildren().clear();
        //group.getChildren().addAll(g);
    }

    public void StopTimers()
    {
        _board.timer.cancel();
        timerPlayer.cancel();
        bowAppearance.stop();
        animalsAliveness.stop();
        Sleep(duration);
    }

    public void InitializeController()
    {
        controller.Initialize();
    }

    public void CreateBoard(LocalTime startTime)
    {
        boardRect = new Rectangle(0, 0, 630, 420);
        group.getChildren().add(boardRect);
        _board = new Board(group, boardRect, loader, this);
        AddObjects();

        Hunting();
        _board.SetBoardActualisationTimer();
        SetGameStartTime(startTime, _board);
        controller.setArrow(30);
        HandleBowAppearance();
        HandleDeathOfAnimals();
    }

    public void SetGameStartTime(LocalTime startTime, Board board) {
        board.LastBowDisappearance = startTime;
    }

    private void AddObjects()
    {
        AddBow();
        Boards.GetBoard(_board,controller.getLevel());
        //AddRandBoard();
    }

    private void AddRandBoard()
    {
        AddTrees();
        AddPlayer();
        AddMushrooms();
        AddAnimals();
    }

    private void AddBow() {
        Point p = new Point();
        RandPosition(p);
        _board.AddBow(p.x, p.y);
    }

    private void AddPlayer()
    {
        x /= 21; x *= 21;
        y /= 21; y *= 21;
        _board.AddPlayer(x,y);
    }

    private void AddAnimals()
    {
        AddBoar();
        AddDeer();
        AddWolf();
        AddBear();
    }

    private void AddBoar() {
        Point p = new Point();
        RandPosition(p);
        _board.AddBoar(p.x,p.y);
    }

    private void AddDeer()
    {
        Point deerPos = new Point();
        RandPosition(deerPos);
        _board.AddDeer(deerPos.x,deerPos.y);
    }

    private void AddWolf()
    {
        Point p = new Point();
        RandPosition(p);
        _board.AddWolf(p.x,p.y);
    }

    private void AddBear()
    {
        Point p = new Point();
        RandPosition(p);
        _board.AddBear(p.x,p.y);
    }

    private void AddTrees()
    {
        for (int i=0;i<10;i++)
        {
            Point p = new Point();
            RandPosition(p);
            for (int j=0;j<10 && IsGoodPosition(p.x,p.y);j++)
            {
                CreateTree(p);
                p.x += Tree.GetWidth();
            }
        }
        for (int i=0;i<10;i++)
        {
            Point p = new Point();
            RandPosition(p);
            for (int j=0;j<10 && IsGoodPosition(p.x,p.y);j++)
            {
                CreateTree(p);
                p.y += Tree.GetHeight();
            }
        }
    }

    private void CreateTree(Point p)
    {
        _board.AddTree(p.x,p.y);
    }

    private void Hunting() {
        timerPlayer = new Timer();
        HandleKeyPressing();

        SetTimers();
    }

    Timeline animalsAliveness;
    private void HandleDeathOfAnimals() {
        animalsAliveness = new Timeline();
        KeyFrame keyframe = new KeyFrame(Duration.millis(duration), e -> {
            _board.checkAnimalsAliveness();
        });
        animalsAliveness.getKeyFrames().add(keyframe);
        animalsAliveness.setCycleCount(Timeline.INDEFINITE);
        animalsAliveness.play();
    }

    Timeline bowAppearance;
    private void HandleBowAppearance() {
        bowAppearance = new Timeline();
        KeyFrame keyframe = new KeyFrame(Duration.millis(duration), e -> {
            _board.checkTheBowTime();
        });
        bowAppearance.getKeyFrames().add(keyframe);
        bowAppearance.setCycleCount(Timeline.INDEFINITE);
        bowAppearance.play();
    }

    private void AddMushrooms() {
        for (int i=0;i<10;i++)
        {
            Point p = new Point();
            RandPosition(p);
            for (int j=0;j<10 && IsGoodPosition(p.x,p.y);j++)
            {
                CreateMushroom(p);
                p.x += Mushroom.GetWidth();
            }
        }
        for (int i=0;i<10;i++)
        {
            Point p = new Point();
            RandPosition(p);
            for (int j=0;j<10 && IsGoodPosition(p.x,p.y);j++)
            {
                CreateMushroom(p);
                p.y += Mushroom.GetHeight();
            }
        }
    }

    private void CreateMushroom(Point p) {
        _board.AddMushroom(p.x,p.y);
    }

    private void SetTimers()
    {
        SetPlayerTimer();
    }

    private void SetPlayerTimer()
    {
        TimerTask task = new TimerTask() {
            public void run() {
                Platform.runLater(()->_board.HandlePlayerMove());
            }
        };
        timerPlayer.schedule(task, 0, duration);
    }

    private void HandleKeyPressing()
    {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                Platform.runLater(()->{
                    _board.ChangePlayerDirection(event.getCode());
                    _board.TryDeleteTrees(event.getCode());
                });
            }
        });
    }

    private void GetPosition() {
        Random rand = new Random();
        x = rand.nextInt(630);
        x /= 21; x *= 21;
        y = rand.nextInt(420);
        y /= 21; y *= 21;
    }

    void RandPosition(Point p)
    {
        Random random = new Random();
        int px = random.nextInt(width);
        int py = random.nextInt(height);
        px /= 21; px *= 21;
        py /= 21; py *= 21;
        while (!IsGoodPosition(px,py))
        {
            px = random.nextInt(width);
            py = random.nextInt(height);
            px /= 21; px *= 21;
            py /= 21; py *= 21;
        }
        p.x = px;
        p.y = py;
    }

    boolean IsGoodPosition(int x, int y)
    {
        return _board.IsGoodPositionToCreate(x,y);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
