package Pacman;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ShortestPathFinder {
    Board board;
    int step;

    public ShortestPathFinder(Board board, int step)
    {
        this.board = board;
        this.step = step;
        int width = (int)(Math.ceil(board.GetWidth() / step));
        int height = (int)(Math.ceil(board.GetHeight() / step));
        array = InitializeArray(width, height);
    }

    final int GREAT_VAL = 1000000;
    class BoardArrayElement
    {
        int weightUp;
        int weightDown;
        int weightLeft;
        int weightRight;
        int distance = GREAT_VAL;
        int estimate = 0;
        boolean isInClose;
        Point last = null;

        public BoardArrayElement()
        {
            SetAllDistancesToValue(1);
            isInClose = false;
        }

        public void SetAllDistancesToValue(int value)
        {
            weightUp = weightDown = weightLeft = weightRight = value;
        }

        public void SetDistanceUp(int value)
        {
            weightUp = value;
        }

        public void SetDistanceDown(int value)
        {
            weightDown = value;
        }

        public void SetDistanceLeft(int value)
        {
            weightLeft = value;
        }

        public void SetDistanceRight(int value)
        {
            weightRight = value;
        }

        public int GetDistance()
        {
            return distance;
        }

        public void SetDistance(int value)
        {
            distance = value;
        }

        public void SetEstimate(int value)
        {
            estimate = value;
        }

        public int GetEstimate()
        {return estimate;}

        public void AddToClose() {isInClose = true;}
        public boolean IsInClose() {return isInClose;}
        public void SetLast(Point p) {last = p;}
        public Point GetLast() {return last;}
    }

    private BoardArrayElement[][] array;

    public List<Point> FindStartOfShortestPath(Point s, Point t, int len)
    {
        s = Convert(s, step);
        t = Convert(t, step);

        CalculateEstimates(array,  t);
        List<Point> list = FindShortestPath(s,t,array);
        if (list == null) return null;
        list = list.stream().limit(len).collect(Collectors.toList());
        ConvertBack(list);
        return list;
    }


    public List<Point> FindStartOfShortestPath(Point s, Point t)
    {
        long start = System.nanoTime();
        List<Point> list =  FindStartOfShortestPath(s,t,10);
        long end = System.nanoTime();
        long tm = end - start;
        //System.out.println(tm);
        return list;
    }

    BoardArrayElement[][] InitializeArray(int width, int height) {
        BoardArrayElement[][] array = AllocateArray(width, height);
        FindTrees(array);
        return array;
    }

    void CalculateEstimates(BoardArrayElement[][] array, Point t)
    {
        for (int i=0;i<array.length;i++)
            for (int j=0;j<array[i].length;j++)
            {
                int estimate = (int)Math.sqrt((i - t.y) * (i - t.y) + (j-t.x)*(j-t.x));
                array[i][j].SetEstimate(estimate);
            }
    }

    BoardArrayElement[][] AllocateArray(int width, int height)
    {
        BoardArrayElement[][] array = new BoardArrayElement[height][];
        for (int i=0;i<height;i++)
            array[i] = new BoardArrayElement[width];
        for (int i=0;i<height;i++)
            for (int j=0;j<width;j++)
                array[i][j] = new BoardArrayElement();

        return array;
    }

    List<Point> FindShortestPath(Point s, Point t, BoardArrayElement[][] array)
    {
        MyPriorityQueue open = new MyPriorityQueue(array);
        open.add(s);
        array[s.y][s.x].SetDistance(0);
        while (!open.isEmpty())
        {
            Point u = open.poll();
            array[u.y][u.x].AddToClose();
            if (u.x == t.x && u.y == t.y)
                break;
            CorrectForEdges(array, open, u);
        }
        if (array[t.y][t.x].last == null) return null;
        List<Point> points = ReconstructPath(array,s,t);
        return points;
    }

    void CorrectForEdges(BoardArrayElement[][] array, MyPriorityQueue open, Point u)
    {
        int hgt = array.length;
        int wdt = array[0].length;
        if (u.y - 1 >= 0)
            Correct(array,open,u,new Point(u.x,u.y-1), array[u.y][u.x].weightUp);
        if (u.y + 1 < hgt)
            Correct(array,open,u,new Point(u.x,u.y+1), array[u.y][u.x].weightDown);
        if (u.x - 1 >= 0)
            Correct(array,open,u,new Point(u.x-1,u.y), array[u.y][u.x].weightLeft);
        if (u.x + 1 < wdt)
            Correct(array,open,u, new Point(u.x+1,u.y), array[u.y][u.x].weightRight);
    }

    void Correct(BoardArrayElement[][] array, MyPriorityQueue open, Point s, Point e, int weight)
    {
        if (e.y < 0 || e.x < 0 || e.y >= array.length || e.x >= array[0].length)
            return;
        if (array[e.y][e.x].IsInClose()) return;
        if (!open.contains(e))
        {
            open.add(e);
        }
        int distance = array[s.y][s.x].GetDistance() + weight;
        if (array[e.y][e.x].GetDistance() > distance)
        {
            array[e.y][e.x].SetDistance(distance);
            open.actualize(e);
            array[e.y][e.x].SetLast(s);
        }

    }

    void ConvertBack(java.util.List<Point> list)
    {
        ListIterator<Point> iterator = list.listIterator();
        while (iterator.hasNext()){
            Point p = iterator.next();
            iterator.set(new Point(p.x*step, p.y*step));
        }
    }

    List<Point> ReconstructPath(BoardArrayElement[][] array, Point s, Point t)
    {
        List<Point> list = new ArrayList<>();
        Point a = t;
        list.add(t);
        while (!a.equals(s))
        {
            Point p = array[a.y][a.x].GetLast();
            list.add(p);
            a = p;
        }
        Collections.reverse(list);
        return list;
    }

    void FindTrees(BoardArrayElement[][] array)
    {
        int wdt = BoardObject.GetWidth();
        int hgt = BoardObject.GetHeight();
        for (Tree t: board.GetTrees())
        {
            Point p = t.GetPosition();
            p = Convert(p,step);
            for (int x= p.x - wdt/step + 1;x<p.x+wdt/step;x++)
                for (int y = p.y - hgt/step + 1;y<p.y + hgt/step;y++)
                    SetInfiniteDistance(array,x,y);
        }
    }

    void SetInfiniteDistance(BoardArrayElement[][] array, int x, int y)
    {
        boolean isSuccesful = SetInfiniteDistanceFromPoint(array,x,y);
        if (!isSuccesful) return;
    }

    boolean SetInfiniteDistanceFromPoint(BoardArrayElement[][] array, int x, int y)
    {
        if (x < 0 || x >= board.GetWidth()/step)
            return false;
        if (y < 0 || y >= board.GetHeight()/step)
            return false;
        array[y][x].SetAllDistancesToValue(GREAT_VAL);
        return true;
    }

    Point Convert(Point p, int step)
    {
        p.x = (p.x) / step;
        p.y = (p.y) / step;
        return new Point(p);
    }
}
