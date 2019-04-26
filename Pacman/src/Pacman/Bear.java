package Pacman;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Bear extends Animal {
    Board board;
    int step;
    HashMap<Integer,Boolean> treeMap;

    public Bear(int x, int y, Board board)
    {
        super(x,y);
        this.board = board;
        texturePath = "resources/bear.png";
        step = board.GetStep();
        InitializeTreeMap();
        FindNextPointsToMove();
        initialPosition = new Point(15 * 21, 13 * 21);
        penalty = 1;
    }

    private void InitializeTreeMap()
    {
        treeMap = new HashMap<>();
        List<Tree> trees = board.GetTrees();
        int width = board.GetWidth();
        for (Tree t: trees)
            treeMap.put(t.GetPosition().y*width + t.GetPosition().x,true);
    }

    List<Point> nextPoints;
    Point chosenPoint;


    @Override
    public Point RequestMove() {
        if (nextPoints == null || nextPoints.isEmpty())
            FindNextPointsToMove();
        if (nextPoints == null) return null;
        Point next = nextPoints.remove(0);
        return next;
    }

    @Override
    public void BadRequest() {
        FindNextPointsToMove();
    }

    private void FindNextPointsToMove()
    {
        Point s = new Point(positionX,positionY);
        Point t = PredictPosition(board.player);
        ShortestPathFinder finder = new ShortestPathFinder(board,step);
        nextPoints = finder.FindStartOfShortestPath(s,t);
    }

    enum Direction {left,right,up,down, none};

    Point PredictPosition(Player player)
    {
        chosenPoint = player.GetPosition();
        Direction direction = GetDirection(player);
        for (int i=0;i<10;i++)
            direction = Predict(direction);
        return chosenPoint;
    }

    Direction GetDirection(Player player)
    {
        if (player.IsUp()) return Direction.up;
        if (player.IsDown()) return Direction.down;
        if (player.IsLeft()) return Direction.left;
        if (player.IsRight()) return Direction.right;
        return Direction.none;
    }

    Direction Predict(Direction direction)
    {
        Point p =chosenPoint;
        Point next;
        switch (direction) {
            case down: next = new Point(p.x,p.y+step);
                break;
            case up: next = new Point(p.x,p.y-step);
                break;
            case left: next = new Point(p.x - step, p.y );
                break;
            case right: next = new Point(p.x+step,p.y);
                break;
            default: next = new Point(p);
        }
        if (IsConflict(next))
            return ChooseNewDirection(direction);
        else
        {
            chosenPoint = next;
            return direction;
        }
    }

    boolean IsConflict(Point p)
    {
        if (p.x < 0 || p.x >= board.GetWidth() || p.y < 0 || p.y>= board.GetHeight())
            return true;
        for (int x = p.x; x<p.x+BoardObject.GetWidth() - 1;x++)
            for (int y=p.y; y<p.y + BoardObject.GetHeight() - 1;y++)
                if (treeMap.containsKey(p.y*board.GetWidth() + p.x))
                    return true;
        return false;
    }

    Direction ChooseNewDirection(Direction badDirection)
    {
        if (badDirection == Direction.up || badDirection == Direction.down)
            return ChooseLeftRight();
        if (badDirection == Direction.left || badDirection == Direction.right)
            return ChooseUpDown();
        return Direction.none;
    }

    Direction ChooseLeftRight()
    {
        Random random = new Random();
        int correction = (random.nextInt(2) * 2 - 1)*step;
        Point w = new Point(chosenPoint.x + correction, chosenPoint.y);
        if (IsConflict(w))
        {
            correction *=-1;
            w = new Point(chosenPoint.x + correction, chosenPoint.y);
            if (IsConflict(w)) return Direction.none;
        }
        chosenPoint = w;
        return correction > 0? Direction.right : Direction.left;
    }

    Direction ChooseUpDown()
    {
        Random random = new Random();
        int correction = (random.nextInt(2) * 2 - 1)*step;
        Point w = new Point(chosenPoint.x, chosenPoint.y + correction);
        if (IsConflict(w))
        {
            correction *=-1;
            w = new Point(chosenPoint.x, chosenPoint.y + correction);
            if (IsConflict(w)) return Direction.none;
        }
        chosenPoint = w;
        return correction > 0? Direction.down : Direction.up;
    }

    int hash(Point p)
    {
        return p.y*board.GetWidth() + p.x;
    }
}
