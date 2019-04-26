package Pacman;

import java.awt.*;
import java.util.Random;

public class Deer extends Animal {
    enum Direction {
        Left, Right, Up, Down;

        public static Direction FromInt(int nr)
        {
            switch(nr){
                case 0: return Left;
                case 1: return Right;
                case 2: return Up;
                case 3: return Down;
            }
            return Left;
        }
    }

    Direction direction;
    int step = 21;

    public Deer(int x, int y, Board board)
    {
        super(x,y);
        texturePath = "resources/deer.png";
        BadRequest();
        step = board.GetStep();
        initialPosition = new Point(6 * 21, 7 * 21);
        penalty = 15;
    }

    @Override
    public Point RequestMove()
    {
        switch(direction) {
            case Left:
                return new Point(positionX - step, positionY);
            case Right:
                return new Point(positionX + step, positionY);
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
        direction = Direction.FromInt(random.nextInt(4));
    }
}