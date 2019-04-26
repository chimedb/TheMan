package Pacman;

import java.awt.*;
import java.util.PriorityQueue;
import java.util.function.Predicate;

public class MyPriorityQueue {
    class PriorityPoint implements Comparable<PriorityPoint>
    {
        Point point;
        int priority;

        public PriorityPoint(Point p, ShortestPathFinder.BoardArrayElement[][] array)
        {
            point = p;
            priority = array[p.y][p.x].GetDistance() + array[p.y][p.x].GetEstimate();
        }

        public int compareTo(PriorityPoint p2)
        {
            return  (priority - p2.priority);
        }

        @Override
        public boolean equals(Object obj) {
            PriorityPoint p = (PriorityPoint)obj;
            return p.point.x == point.x && p.point.y == point.y;
        }
    }

    PriorityQueue<PriorityPoint> _queue;

    ShortestPathFinder.BoardArrayElement[][] _array;

    public MyPriorityQueue(ShortestPathFinder.BoardArrayElement[][] array)
    {
        _array = array;
        _queue = new PriorityQueue<>();
    }

    public void add(Point p)
    {
        PriorityPoint point = new PriorityPoint(p,_array);
        _queue.add(point);
    }

    public boolean isEmpty()
    {
        return _queue.isEmpty();
    }

    public Point poll()
    {
        PriorityPoint p = _queue.poll();
        return p.point;
    }

    public boolean contains(Point p)
    {
        PriorityPoint point = new PriorityPoint(p,_array);
        return _queue.contains(point);
    }

    public void actualize(Point p)
    {
        PriorityPoint point = new PriorityPoint(p,_array);
        _queue.removeIf(new Predicate<PriorityPoint>() {
            @Override
            public boolean test(PriorityPoint priorityPoint) {
                Point q = priorityPoint.point;
                return q.x == p.x && q.y == p.y;
            }
        });
        _queue.add(point);
    }
}
