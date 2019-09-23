import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

/**
 * @author knight
 */

public class PointSET {

    private TreeSet<Point2D> treeSet;

    public PointSET() {
        treeSet = new TreeSet<>(Point2D::compareTo);
    }

    public boolean isEmpty() {
        return treeSet.isEmpty();
    }

    public int size() {
        return treeSet.size();
    }

    public void insert(Point2D p) {
        checkArgument(p);
        treeSet.add(p);
    }

    public boolean contains(Point2D p) {
        checkArgument(p);
        return treeSet.contains(p);
    }

    public void draw() {
        for (Point2D p : treeSet) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        checkArgument(rect);
        TreeSet<Point2D> iterable = new TreeSet<>();
        for (Point2D point : treeSet) {
            if (rect.contains(point)) {
                iterable.add(point);
            }
        }
        return iterable;
    }

    public Point2D nearest(Point2D p) {
        checkArgument(p);
        if (!isEmpty()) {
            double comparer = Double.POSITIVE_INFINITY;
            Point2D candidate = null;
            for (Point2D point : treeSet) {
                double temp = p.distanceSquaredTo(point);
                if (temp < comparer) {
                    candidate = point;
                    comparer = temp;
                }
            }
            return candidate;
        }
        return null;
    }

    private void checkArgument(Object o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
    }
}
