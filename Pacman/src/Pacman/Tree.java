package Pacman;

import java.awt.*;

public class Tree extends BoardObject {

    public Tree(int x, int y)
    {
        super(x, y);
        texturePath = "resources/tree.png";
    }

    public Point GetPosition()
    {
        return new Point(positionX,positionY);
    }
}

