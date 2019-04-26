package Pacman;

import java.awt.*;

abstract public class Animal extends BoardObject{
    int speed;
    boolean alive;
    Point initialPosition;
    int penalty;
    int award = 50;

    public int GetPenalty()
    {
        return penalty;
    }

    public int GetAward()
    {
        return award;
    }

    public Animal(int x, int y)
    {
        super(x,y);
        alive = true;
    }

    abstract public Point RequestMove();

    public void SetPosition(Point p)
    {
        drawnRectangle.relocate(p.x, p.y);
        positionX = p.x;
        positionY = p.y;
    }

    abstract public void BadRequest();
}
