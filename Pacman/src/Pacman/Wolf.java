package Pacman;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Wolf extends Animal {
    Board board;
    int step;

    public Wolf(int x, int y, Board board)
    {
        super(x,y);
        this.board = board;
        texturePath = "resources/wolf.png";
        step = board.GetStep();
        FindNextPointsToMove();
        initialPosition = new Point(13 * 21, 0);
        penalty = 5;
    }

    List<Point> nextPoints = new ArrayList<Point>();

    @Override
    public Point RequestMove() {
        if (alive) {
            if (nextPoints == null || nextPoints.isEmpty()) {
                //System.out.println("Next is null");
                FindNextPointsToMove();
            }
            if (nextPoints == null) return new Point(positionX,positionY);
            Point next = nextPoints.remove(0);
//        System.out.println("Next: " + next.x + " , " + next.y);
            return next;
        }
        return new Point(positionX,positionY);
    }

    @Override
    public void BadRequest(){
        FindNextPointsToMove();
    }

    private void FindNextPointsToMove()
    {
        if (alive) {
            Point s = new Point(positionX, positionY);
            Point t = new Point(board.player.GetPosition());
            ShortestPathFinder finder = new ShortestPathFinder(board, step);
            nextPoints = finder.FindStartOfShortestPath(s, t);
        }
    }
}