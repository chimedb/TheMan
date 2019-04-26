package Pacman;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Board {
    Group displayGroup;
    Rectangle area;
    FXMLLoader loader;
    Game game;
    Bow bow;

    Wolf wolf;
    Bear bear;
    Boar boar;
    Deer deer;

    Player player;
    List<Tree> _trees = FXCollections.observableArrayList();//new ArrayList<>(); //CopyOnWriteArrayList<>();
    List<Mushroom> _mushrooms = FXCollections.observableArrayList();//new ArrayList<>();
    List<Animal> _animals = FXCollections.observableArrayList();//new ArrayList<>();
    int step = 7;


    public Board(Group group, Rectangle boardArea, FXMLLoader loader, Game game)
    {
        displayGroup = group;
        area = boardArea;
        this.loader = loader;
        this.game = game;
    }

    private void AddToDisplay(BoardObject object)
    {
        Rectangle rect = object.GetDrawnRectangle();
        displayGroup.getChildren().add(rect);
    }

    public void AddPlayer(int x, int y)
    {
        player = new Player(x,y);
        AddToDisplay(player);
    }

    public void AddTree(int x, int y)
    {
        Tree tree = new Tree(x,y);
        _trees.add(tree);
        Rectangle rect = tree.GetDrawnRectangle();
        displayGroup.getChildren().add(rect);
    }

    public void AddBoar(int x, int y)
    {
        boar = new Boar(x, y, this);
        Platform.runLater(() -> _animals.add(boar));
        AddToDisplay(boar);
    }

    public void AddDeer(int x, int y)
    {
        deer = new Deer(x, y, this);
        Platform.runLater(() -> _animals.add(deer));
        AddToDisplay(deer);
    }

    public void AddWolf(int x, int y)
    {
        wolf = new Wolf(x,y,this);
        Platform.runLater(() -> _animals.add(wolf));
        AddToDisplay(wolf);
    }

    public void AddBear(int x, int y)
    {
        bear = new Bear(x, y, this);
        Platform.runLater(() -> _animals.add(bear));
        AddToDisplay(bear);
    }

    public void AddMushroom(int x, int y)
    {
        Mushroom mushroom = new Mushroom(x, y);
        _mushrooms.add(mushroom);
        AddToDisplay(mushroom);
        //Rectangle rect = mushroom.GetDrawnRectangle();
        //displayGroup.getChildren().add(rect);
        //mushroomRect.add(rect);
    }

    public void AddMushromsWithProbability(double probability)
    {
        //int p = (int)(probability * 100);
        int cnt = 100;
        int wdt = GetWidth()/step;
        int hgt = GetHeight()/step;
        Random rand = new Random();
        while (cnt > 0)
        {
            int x = rand.nextInt(wdt);
            int y = rand.nextInt(hgt);
            if (IsGoodPositionToCreate(x*21,y*21))
            {
                AddMushroom(x*21,y*21);
                cnt--;
            }
        }
    }

    public void AddBow(int x, int y) {
        bow = new Bow(x, y);
    }

    LocalTime LastBowDisappearance;
    long additionalTime = 0;
    LocalTime LastBowAppearance;
    int counter = 1;
    public void checkTheBowTime() {
        try {
            LocalTime localtime = LocalTime.now();
            long elapsed = ChronoUnit.SECONDS.between(LastBowDisappearance, localtime);
            elapsed += additionalTime;
            if (elapsed == counter) {
                counter++;
                ReduceBowTime(1);
            }
            if (!bow.visible && elapsed == 30) {
                bow.visible = true;
                AddBowToDisplay(bow);
                LastBowAppearance = localtime;
            }
            if (bow.visible) {
                KeepDisplayBow(LastBowAppearance);
            }
            if (player.hasBow) {
                PlayerHoldsTheBow(LastBowTakenBy);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void AddBowToDisplay(Bow bow)
    {
        Rectangle rect = bow.GetDrawnRectangle();
        Point p = new Point();
        RandPosition(p);
        bow.positionX = p.x;
        bow.positionY = p.y;
        rect.relocate(p.x, p.y);
        Platform.runLater(()->displayGroup.getChildren().add(rect));
        //displayGroup.getChildren().add(rect);
    }

    LocalTime LastBowTakenBy;
    private void KeepDisplayBow(LocalTime time) {
        LocalTime localtime = LocalTime.now();
        long elapsed = ChronoUnit.SECONDS.between(time, localtime);
        if (!player.hasBow && elapsed <= 10) {
            if (player.CutsWithObjStartingAt(bow.positionX, bow.positionY)) {
                player.hasBow = true;
                player.setBowImage();
                bow.visible = false;
                LastBowTakenBy = localtime;
                RemoveBow();
            }
        }
        if (!player.hasBow && (displayGroup.getChildren().contains(bow.GetActualRectangle()) && elapsed == 10)) {
            RemoveBow();
            counter = 1;
            RestartBowTime(30);
            LastBowDisappearance = localtime;
            bow.visible = false;
        }
    }

    public void PlayerHoldsTheBow(LocalTime time) {
        LocalTime localtime = LocalTime.now();
        long elapsed = ChronoUnit.SECONDS.between(time, localtime);
        long elapsedMillis = ChronoUnit.MILLIS.between(time, localtime);
        if (9200 <= elapsedMillis && elapsedMillis < 9400)
            player.setDefaultImage();
        if (9400 <= elapsedMillis && elapsedMillis < 9600)
            player.setBowImage();
        if (9600 <= elapsedMillis && elapsedMillis < 9800)
            player.setDefaultImage();
        if (9800 <= elapsedMillis && elapsedMillis < 10000)
            player.setBowImage();
        if (elapsed == 10) {
            player.setDefaultImage();
            player.hasBow = false;
            bow.time += elapsed;
            counter = 1;
            RestartBowTime(30);
            LastBowDisappearance = localtime;
        }
    }

    void RandPosition(Point p)
    {
        int width = (int)area.getWidth();
        int height = (int)area.getHeight();
        Random random = new Random();
        int px = random.nextInt(width);
        int py = random.nextInt(height);
        px /= 21; px *= 21;
        py /= 21; py *= 21;
        while (!IsGoodPosition(px, py))
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
        return IsGoodPositionToCreate(x,y);
    }

    private void RemoveBow() {
        Platform.runLater(() -> displayGroup.getChildren().remove(bow.GetActualRectangle()));
    }

    public void ChangePlayerDirection(KeyCode keyCode)
    {
        player.ChangeDirection(keyCode);
    }

    Timer timer;
    public void SetBoardActualisationTimer()
    {
        timer = new Timer();
        timer.schedule(new Mover(), game.duration);
    }

    public void TryDeleteTrees(KeyCode key)
    {
        Point pos = player.GetPosition();
        switch (key)
        {
            case A: pos = SetDeleteTreeLeft(pos);
                break;
            case S: pos = SetDeleteTreeDown(pos);
                break;
            case D: pos = SetDeleteTreeRight(pos);
                break;
            case W: pos = SetDeleteTreeUp(pos);
                break;
            default: return;
        }
        TryDeleteTree(pos);
    }

    private Point ConvertActualPositionForDeletingTrees(Point p)
    {
        if (p.x == 0)
            return p;
        int tx = p.x / BoardObject.GetWidth();
        int dx = p.x % BoardObject.GetHeight();
        if (dx > BoardObject.GetWidth()/2) tx += 1;
        int ty = p.y / BoardObject.GetHeight();
        int dy = p.y % BoardObject.GetHeight();
        if (dy > BoardObject.GetHeight() / 2) ty += 1;
        return new Point(tx*BoardObject.GetWidth(),ty * BoardObject.GetHeight());
    }

    private Point SetDeleteTreeLeft(Point p)
    {
        int tx = p.x / BoardObject.GetWidth();
        int ty = p.y / BoardObject.GetHeight();
        int dy = p.y % BoardObject.GetHeight();
        if (dy > BoardObject.GetHeight() / 2) ty += 1;
        return new Point((tx-1)*BoardObject.GetWidth(),ty*BoardObject.GetHeight());
    }

    private Point SetDeleteTreeRight(Point p)
    {
        int tx = p.x / BoardObject.GetWidth();
        int dx = p.x % BoardObject.GetHeight();
        if (dx > 0) tx += 1;
        int ty = p.y / BoardObject.GetHeight();
        int dy = p.y % BoardObject.GetHeight();
        if (dy > BoardObject.GetHeight() / 2) ty += 1;
        return new Point((tx+1)*BoardObject.GetWidth(),ty*BoardObject.GetHeight());
    }
    private Point SetDeleteTreeUp(Point p)
    {
        int tx = p.x / BoardObject.GetWidth();
        int dx = p.x % BoardObject.GetHeight();
        if (dx > BoardObject.GetWidth()/2) tx += 1;
        int ty = p.y / BoardObject.GetHeight();
        return new Point(tx*BoardObject.GetWidth(),(ty-1)*BoardObject.GetHeight());
    }

    private Point SetDeleteTreeDown(Point p)
    {
        int tx = p.x / BoardObject.GetWidth();
        int dx = p.x % BoardObject.GetHeight();
        if (dx > BoardObject.GetWidth()/2) tx += 1;
        int ty = p.y / BoardObject.GetHeight();
        int dy = p.y % BoardObject.GetHeight();
        if (dy > 0) ty += 1;
        return new Point(tx*BoardObject.GetWidth(),(ty+1)*BoardObject.GetHeight());
    }

    private void TryDeleteTree(Point pos)
    {
        //_trees.removeIf((t)->t.Contains(pos.x,pos.y));
        for (int i=0;i<_trees.size();i++)
            if (_trees.get(i).Contains(pos.x,pos.y)) {
                displayGroup.getChildren().remove(_trees.get(i).GetActualRectangle());
                _trees.remove(i);
            }
    }


    class Mover extends TimerTask
    {
        @Override
        public void run()
        {
            Platform.runLater(()->{
                for (Animal a : _animals)
                    Move(a);
                timer.schedule(new Mover(), game.duration);
            });
        }
    }

    private void Move(Animal a)
    {
        if (a.alive) {
            Point p = a.RequestMove();
            if (p == null) return;
            if (IsGoodPositionToMove(p.x, p.y, a))
                a.SetPosition(p);
            else
                a.BadRequest();
        }
    }

    public void HandlePlayerMove()
    {
        try {
            if (_mushrooms.size() == 0)
                ChangeLevel();
            HandleMoveUp();
            HandleMoveDown();
            HandleMoveLeft();
            HandleMoveRight();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void ChangeLevel()
    {
        game.ChangeLevel();
    }

    private void HandleMoveUp()
    {
        DeathOnTop();
        if (player.IsUp()) {
            if (player.positionY < step || !CheckCollision(player.positionX, player.positionY - step)) {
                player.ResetMovement();
            } else {
                player.positionY -= step;
                EatMushrooms(player.positionX, player.positionY);
                player.GetActualRectangle().relocate(player.positionX, player.positionY);
                //System.out.println(player.positionX + " " + player.positionY);
            }
        }
    }

    private void HandleMoveDown()
    {
        DeathOnBottom();
        if (player.IsDown()) {
            if (player.positionY >= area.getHeight() - step || !CheckCollision(player.positionX, player.positionY + step)) {
                player.ResetMovement();
            } else {
                player.positionY += step;
                EatMushrooms(player.positionX, player.positionY);
                player.GetActualRectangle().relocate(player.positionX, player.positionY);
                //System.out.println(player.positionX + " " + player.positionY);
            }
        }
    }

    private void HandleMoveLeft()
    {
        DeathOnLeft();
        if (player.IsLeft()) {
            if (player.positionX < step|| !CheckCollision(player.positionX - step, player.positionY)) {
                player.ResetMovement();
            } else {
                player.positionX -= step;
                EatMushrooms(player.positionX, player.positionY);
                player.GetActualRectangle().relocate(player.positionX, player.positionY);
                //System.out.println(player.positionX + " " + player.positionY);
            }
        }
    }

    private void HandleMoveRight()
    {
        DeathOnRight();
        if (player.IsRight()) {
            if (player.positionX >= area.getWidth() - step || !CheckCollision(player.positionX + step, player.positionY)) {
                player.ResetMovement();
            } else {
                player.positionX += step;
                EatMushrooms(player.positionX, player.positionY);
                player.GetActualRectangle().relocate(player.positionX, player.positionY);
                //System.out.println(player.positionX + " " + player.positionY);
            }
        }
    }

    LocalTime DeathTimeOfWolf;
    LocalTime DeathTimeOfBear;
    LocalTime DeathTimeOfBoar;
    LocalTime DeathTimeOfDeer;
    private void KillAnimal(Animal animal) {
        Rectangle rect = animal.GetActualRectangle();
        rect.setVisible(false);
        animal.alive = false;
        LocalTime current = LocalTime.now();
        if (animal.getClass() == Wolf.class)
            DeathTimeOfWolf = current;
        if (animal.getClass() == Bear.class)
            DeathTimeOfBear = current;
        if (animal.getClass() == Boar.class)
            DeathTimeOfBoar = current;
        if (animal.getClass() == Deer.class)
            DeathTimeOfDeer = current;
    }

    public void checkAnimalsAliveness()
    {
        if (wolf != null) RespawnAnimal(wolf);
        if (bear != null) RespawnAnimal(bear);
        if (boar != null) RespawnAnimal(boar);
        if (deer != null) RespawnAnimal(deer);
    }

    private void RespawnAnimal(Animal animal) {
        if (!animal.alive) {
            LocalTime deathTime = LocalTime.now();
            if (animal.getClass() == Wolf.class)
                deathTime = DeathTimeOfWolf;
            if (animal.getClass() == Bear.class)
                deathTime = DeathTimeOfBear;
            if (animal.getClass() == Boar.class)
                deathTime = DeathTimeOfBoar;
            if (animal.getClass() == Deer.class)
                deathTime = DeathTimeOfDeer;
            LocalTime localTime = LocalTime.now();
            long elapsed = ChronoUnit.SECONDS.between(deathTime, localTime);
            if (elapsed >= 5 && !animal.alive) {
                if (IsGoodPositionToMove(animal.positionX, animal.positionY, animal)) {
                    animal.alive = true;
                    animal.GetActualRectangle().setVisible(true);
                }
            }
        }
    }

    private void ServeDeath(Animal m)
    {
        if (!player.hasBow) {
            player.touched = true;
            ReducePoints(m.GetPenalty());
        } else {
            if (m.alive) {
                KillAnimal(m);
                AddPoints(m.GetAward());
            }
        }
    }

    private void DeathOnRight() {
        if (player.IsRight()) {
            for (Animal m : _animals) {
                if (player.positionX + BoardObject.GetWidth() == m.positionX && player.positionY == m.positionY) {
                    ServeDeath(m);
                }
            }
        }
        RespawnPlayer();
    }

    private void DeathOnLeft() {
        if (player.IsLeft()) {
            for (Animal m : _animals) {
                if (player.positionX - BoardObject.GetWidth() == m.positionX && player.positionY == m.positionY) {
                    ServeDeath(m);
                }
            }
        }
        RespawnPlayer();
    }

    private void DeathOnTop() {
        if (player.IsUp()) {
            for (Animal m : _animals) {
                if (player.positionX == m.positionX && player.positionY - BoardObject.GetHeight() == m.positionY) {
                    ServeDeath(m);
                }
            }
        }
        RespawnPlayer();
    }

    private void DeathOnBottom() {
        if (player.IsDown()) {
            for (Animal m : _animals) {
                if (player.positionX == m.positionX && player.positionY + player.GetObjectHeight() == m.positionY) {
                    ServeDeath(m);
                }
            }
        }
        RespawnPlayer();
    }

    private boolean isFinishing;

    private void RespawnPlayer() {
        if (player.touched) {
            Platform.runLater(() -> ReduceLife(1));
            if (((ViewController)loader.getController()).getLifeCount() == 1) {
                FinishTheGame();
            } else {
                player.GetActualRectangle().relocate(0, 0);
                player.positionX = 0;
                player.positionY = 0;
                player.touched = false;
            }
        }
    }

    private void FinishTheGame() {
        if (isFinishing) return;
        isFinishing = true;
        try {
            TimeUnit.MILLISECONDS.sleep(3);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        int point = ((ViewController)loader.getController()).getPoints();
        String str = Integer.toString(point);
        Platform.runLater(() -> {
                AlertBox alertBox = new AlertBox(this);
                alertBox.display("You're dead", "Your point is " + str);
                isFinishing = false;
        });
    }

    private void ReduceBowTime(int count) {
        ((ViewController)loader.getController()).ReduceBowTime(count);
    }

    private void RestartBowTime(int count) {
        ((ViewController)loader.getController()).setArrow(count);
    }

    private void ReduceLife(int count) { ((ViewController)loader.getController()).ReduceLife(count); }

    private void EatMushrooms(int x, int y) {
        for (Mushroom m: _mushrooms) {
            if (m.CutsWithObjStartingAt(x,y)) {
                Platform.runLater(
                        () ->  {
                            displayGroup.getChildren().remove(m.GetActualRectangle());
                            _mushrooms.remove(m);
                            AddPoints(1);
                        }
                );
            }
        }

//        for (Rectangle rect: mushroomRect) {
//            if (rect.getX() == x && rect.getY() == y) {
//                Platform.runLater(
//                    () ->  {
//                        displayGroup.getChildren().remove(rect);
//                        mushroomRect.remove(rect);
//                        AddPoints(1);
//                    }
//                );
//                return;
//            }
//        }
    }

    private void AddPoints(int count)
    {
        Platform.runLater(() -> ((ViewController)loader.getController()).AddPoints(count));
    }

    private void ReducePoints(int count)
    {
        Platform.runLater(() -> ((ViewController)loader.getController()).ReducePoints(count));
    }

    private Boolean CheckCollision(int x, int y) {
        if (CutLines(x,y,player)) return false;
        for (Tree t: _trees) {
            if (t.CutsWithObjStartingAt(x, y)) return false;
        }
        for (Animal animal : _animals) {
            if (animal.alive && animal.CutsWithObjStartingAt(x, y) && !player.hasBow) return false;
        }
        return true;
    }

    private boolean CutLines(int x, int y, BoardObject obj)
    {
        int h = obj.GetObjectHeight();
        int w = obj.GetObjectWidth();
        if (x < 0) return true;
        if (y < 0) return true;
        if (x + w > area.getWidth()) return true;
        if (y + h > area.getHeight())
            return true;
        return false;
    }

    public boolean IsGoodPositionToCreate(int x, int y)
    {
        if (!Tree.IsInBoard(x,y,(int)area.getWidth(),(int)area.getHeight())) return false;
        for (Tree t: _trees) {
            if (t.CutsWithObjStartingAt(x,y)) return false;
        }
        for (Mushroom m: _mushrooms) {
            if (m.CutsWithObjStartingAt(x,y)) return false;
        }
//        for (Rectangle m: mushroomRect) {
//            if (m.getX() == x && m.getY() == y) return false;
//        }
        for (Animal animal : _animals) {
            if (animal.CutsWithObjStartingAt(x, y)) return false;
        }
        return true;
    }

    public boolean IsGoodPositionToMove(int x, int y, BoardObject obj)
    {
        if (CutLines(x,y,obj)) return false;
        //to service going through the line
        for (Tree t: _trees) {
            if (t.CutsWithObjStartingAt(x,y)) return false;
        }
        for (Animal a: _animals)
        {
            if (a!=obj && a.alive) {
                if (a.CutsWithObjStartingAt(x,y)) return false;
            }
        }
        if (player != obj) {
            if (player.CutsWithObjStartingAt(x,y)) {
                ServeDeath((Animal)obj);
            }
        }
        return true;
    }

    public Point GetPlayerPosition()
    {
        return player.GetPosition();
    }

    public int GetWidth()
    {
        return (int)area.getWidth();
    }

    public int GetHeight()
    {
        return (int)area.getHeight();
    }

    public List<Tree> GetTrees()
    {
        return _trees;
    }

    public int GetStep()
    {
        return step;
    }
}
