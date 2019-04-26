package Pacman;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.ImagePattern;
import javafx.scene.image.Image;

import java.awt.*;

public class Player extends BoardObject {
    public int point;
    public int life;
    public boolean hasBow;
    public boolean touched;
    public String bowPath;
    private boolean up, down, right, left;

    public enum Direction {up, down, right, left, none}

    private Direction direction;

    public Player(int x, int y)
    {
        super(x, y);
        point = 0;
        life = 3;
        hasBow = false;
        touched = false;
        direction = Direction.none;
        bowPath = "resources/manbow.png";
        texturePath = "resources/man.png";
    }

    public void ResetMovement() {
        up = down = right = left = false;
    }

    public boolean IsUp()
    {
        return up;
    }

    public boolean IsDown()
    {
        return down;
    }

    public boolean IsLeft()
    {
        return left;
    }

    public boolean IsRight()
    {
        return right;
    }

    public void ChangeDirection(KeyCode keyCode)
    {
        switch (keyCode) {
            case UP :
                ResetMovement();
                up = true;
                break;
            case DOWN :
                ResetMovement();
                down = true;
                break;
            case LEFT :
                ResetMovement();
                left = true;
                break;
            case RIGHT :
                ResetMovement();
                right = true;
                break;
        }
    }

    public Point GetPosition()
    {
        return new Point(positionX,positionY);
    }

    public void setBowImage() {
        Image img = LoadImage(bowPath);
        ImagePattern imagePattern = new ImagePattern(img);
        this.GetActualRectangle().setFill(imagePattern);
    }

    public void setDefaultImage() {
        Image img = LoadImage(texturePath);
        ImagePattern imagePattern = new ImagePattern(img);
        this.GetActualRectangle().setFill(imagePattern);
    }
}

