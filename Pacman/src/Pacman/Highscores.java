package Pacman;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Highscores {
    final int N = 5;


    String filename = "resources/highscores.txt";

    class Score
    {
        int score;
        String name;
        public Score(String name, int score)
        {
            this.name = name;
            this.score = score;
        }

        public Integer GetScore()
        {
            return score;
        }

        public String GetName()
        {
            return name;
        }

        @Override
        public String toString() {
            return name + ":" + score;
        }
    }

    class ScoreComparator implements Comparator<Score> {

        @Override
        public int compare(Score o1, Score o2) {
            return o2.score - o1.score;
        }
    }

    List<Score> scores = new ArrayList<>(5);

    public Highscores()
    {
        File file = new File(filename);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            int i=0;
            try {
                while ((line = br.readLine()) != null && i < 5)
                {
                    ParseHighscoresLine(line);
                    i++;
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        catch (FileNotFoundException e) { e.printStackTrace();}
        SortAndCut();
    }

    private void ParseHighscoresLine(String line) {
        int last = line.lastIndexOf(':');
        String start = line.substring(0,last);
        String end = line.substring(last+1);
        Integer score = Integer.parseInt(end);
        scores.add(new Score(start,score));
    }

    private void SortAndCut()
    {
        scores.sort(new ScoreComparator());
        scores = scores.stream().limit(N).collect(Collectors.toList());
    }

    public void AddScore(String name, int result)
    {
        Score score = new Score(name, result);
        ScoreComparator comparator = new ScoreComparator();
        if (scores.size() == 0 || comparator.compare(score,scores.get(scores.size()-1)) < 0)
        {
            scores.add(score);
            SortAndCut();
        }
        Save();
    }

    public void Save()
    {
        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            for (Score score: scores)
                writer.println(score.toString());
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void Display(Stage parentStage)
    {
        Scene scene = new Scene(new Group());
        Stage dialog = new Stage();
        dialog.initOwner(parentStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setWidth(250);
        dialog.setHeight(200);

        AddHighscoresTable(((Group) scene.getRoot()));
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void AddHighscoresTable(Group g)
    {
        GridPane gridpane = new GridPane();
        AddConstraints(gridpane);
        AddLabel("Nr",0,0,gridpane);
        AddLabel("Name",1,0,gridpane);
        AddLabel("Score",2,0,gridpane);
        AddData(gridpane);

        g.getChildren().addAll(gridpane);
    }

    private void AddConstraints(GridPane gridpane)
    {
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(25);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);
        ColumnConstraints c3 = new ColumnConstraints();
        c3.setPercentWidth(25);
        gridpane.getColumnConstraints().addAll(c1,c2,c3);
    }

    private void AddLabel(String name, int x, int y, GridPane gridpane)
    {
        Label label = new Label(name);
        Font columnNameFont = Font.font("Verdana", FontWeight.BOLD,16);
        label.setFont(columnNameFont);
        label.setTextAlignment(TextAlignment.CENTER);
        gridpane.add(label,x,y);
    }

    private void AddData(GridPane gridpane)
    {
        for (int i=0;i<scores.size();i++)
        {
            Integer nr = i+1;
            AddLabel(nr.toString(),0,i+1,gridpane);
            AddLabel(scores.get(i).GetName(),1,i+1,gridpane);
            AddLabel(scores.get(i).GetScore().toString(),2,i+1,gridpane);
        }
    }

}
