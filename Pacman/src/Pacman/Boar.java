package Pacman;

import java.awt.*;
import java.util.Random;

public class Boar extends Animal {
    enum Direction {
        Up, Down;

        public static Direction FromInt(int nr)
        {
            switch (nr){
                case 0: return Up;
                case 1: return Down;
            }
            return Up;
        }
    }

    Direction direction;
    int step = 21;

    public Boar(int x, int y, Board board)
    {
        super(x, y);
        texturePath = "resources/boar.png";
        step = board.GetStep();
        BadRequest();
        initialPosition = new Point(4 * 21, 13 * 21);
        penalty = 20;
    }

    @Override
    public Point RequestMove()
    {
        switch (direction) {
            case Up:
                return new Point(positionX,positionY - step);
            case Down:
                return new Point(positionX,positionY + step);
        }
        return new Point(positionX, positionY);
    }

    @Override
    public void BadRequest()
    {
        Random random = new Random();
        direction = Direction.FromInt(random.nextInt(2));
    }
}

