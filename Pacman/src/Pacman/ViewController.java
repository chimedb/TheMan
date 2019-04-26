package Pacman;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable {
    @FXML
    private Group group;
    private IntegerProperty level;
    private IntegerProperty lifeCount;
    private IntegerProperty points;
    private IntegerProperty arrow;
    private Highscores highscores;
    private Stage _stage;

    public void SetStage(Stage stage)
    {
        _stage = stage;
    }

    public int getLevel()
    {
        return level.get();
    }

    public void setLevel(int value)
    {
        level.set(value);
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public int getLifeCount()
    {
        return lifeCount.get();
    }

    public void setLifeCount(int value)
    {
        lifeCount.set(value);
    }

    public IntegerProperty lifeCountProperty() {
        return lifeCount;
    }

    public int getPoints()
    {
        return points.get();
    }

    public void setPoints(int value)
    {
        points.set(value);
    }

    public IntegerProperty pointsProperty() {
        return points;
}

    public int getArrow()
    {
        return arrow.get();
    }

    public void setArrow(int value)
    {
        arrow.set(value);
    }

    public IntegerProperty arrowProperty() {
        return arrow;
    }

    public ViewController()
    {
        level = new SimpleIntegerProperty(1);
        points = new SimpleIntegerProperty(0);
        arrow = new SimpleIntegerProperty(30);
        lifeCount = new SimpleIntegerProperty(3);
        highscores = new Highscores();
    }

    public void Initialize()
    {
        setLevel(1);
        setPoints(0);
        setArrow(30);
        setLifeCount(3);
    }

    public void AddPoints(int count)
    {
        setPoints(getPoints() + count);
    }

    public void ReducePoints(int count) {
        int cnt = getPoints() - count;
        if (cnt < 0) cnt = 0;
        setPoints(cnt);
    }

    public void ReduceLife(int count) {
        setLifeCount(Math.max(0, getLifeCount() - count));
    }

    public void ReduceBowTime(int count) { setArrow(Math.max(0, getArrow() - count)); }

    @Override
    public void initialize(URL url, ResourceBundle resources)
    {
    }

    @FXML
    public void showHighScores()
    {
        highscores.Display(_stage);
    }

    public void AddHighScores(String name, int result)
    {
        highscores.AddScore(name,result);
    }
}