import edu.princeton.cs.algs4.*;

import java.util.LinkedList;

/**
 * @author knight
 */

public class KdTree {

    private kdNode root;
    private int size;
    private RectHV board;

    public KdTree() {
        size = 0;
        board = new RectHV(0.0, 0.0, 1.0, 1.0);
    }

    private class kdNode {
        private double x;
        private double y;
        private kdNode lNode;
        private kdNode rNode;
        private boolean isHorizontal;

        public kdNode(Point2D point, boolean flag) {
            this(point.x(), point.y(), null, null, flag);
        }

        public kdNode(double x, double y, kdNode lNode, kdNode rNode, boolean FLAG) {
            this.isHorizontal = FLAG;
            this.x = x;
            this.y = y;
            this.lNode = lNode;
            this.rNode = rNode;
        }

        private int compareTo(Point2D point) {
            if (isHorizontal) {
                return Double.compare(point.y(), this.y);
            } else {
                return Double.compare(point.x(), this.x);
            }
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        root = put(root, p, false);
    }

    private kdNode put(kdNode node, Point2D point, boolean flag) {
        if (node == null) {
            size++;
            return new kdNode(point, flag);
        }

        int cmp = node.compareTo(point);

        if (cmp < 0) {
            node.lNode = put(node.lNode, point, !flag);
        } else if (cmp > 0) {
            node.rNode = put(node.rNode, point, !flag);
        }

        return node;
    }

    public boolean contains(Point2D p) {
        checkArgument(p);
        kdNode iter = root;
        while (iter != null) {
            int cmp = iter.compareTo(p);
            if (cmp < 0) {
                iter = iter.lNode;
            } else if (cmp > 0) {
                iter = iter.rNode;
            } else {
                return true;
            }
        }
        return false;
    }

    public void draw() {
        StdDraw.setPenRadius(0.005);
        draw(root, board);
    }

    private void draw(kdNode node, RectHV rect) {
        if (node != null) {
            if (node.isHorizontal) {
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.line(rect.xmin(), node.y, rect.xmax(), node.y);
            } else {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.x, rect.ymin(), node.x, rect.ymax());
            }
            draw(node.lNode, leftRect(node, rect));
            draw(node.rNode, rightRect(node, rect));
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        checkArgument(rect);
        LinkedList<Point2D> list = new LinkedList<>();
        get(root, board, rect, list);
        return list;
    }

    private void get(kdNode node, RectHV nodeRect, RectHV rect, LinkedList<Point2D> list) {
        if (node != null) {
            if (nodeRect.intersects(rect)) {
                Point2D temp = new Point2D(node.x, node.y);
                if (rect.contains(temp)) {
                    list.addFirst(temp);
                }
                get(node.lNode, leftRect(node, nodeRect), rect, list);
                get(node.rNode, rightRect(node, nodeRect), rect, list);
            }
        }
    }

    private RectHV leftRect(kdNode node, RectHV rect) {
        if (node.isHorizontal) {
            return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.y);
        } else {
            return new RectHV(rect.xmin(), rect.ymin(), node.x, rect.ymax());
        }
    }

    private RectHV rightRect(kdNode node, RectHV rect) {
        if (node.isHorizontal) {
            return new RectHV(rect.xmin(), node.y, rect.xmax(), rect.ymax());
        } else {
            return new RectHV(node.x, rect.ymin(), rect.xmax(), rect.ymax());
        }
    }

    public Point2D nearest(Point2D p) {
        checkArgument(p);
        return findNearest(p, root, board, Double.POSITIVE_INFINITY, null);
    }

    private Point2D findNearest(Point2D point, kdNode node, RectHV rect, double closest, Point2D closestPoint) {
        if (node == null) {
            return closestPoint;
        } else {
            Point2D cloPoint = new Point2D(node.x, node.y);
            double cloDistance = point.distanceTo(cloPoint);
            if (cloDistance < closest) {
                closest = cloDistance;
                closestPoint = cloPoint;
            }

            if (leftRect(node, rect).distanceTo(point) < closest) {
                closestPoint = findNearest(point, node.lNode, leftRect(node, rect), closest, closestPoint);
            }

            //修改全局变量
            closest = closestPoint.distanceTo(point);

            if (rightRect(node, rect).distanceTo(point) < closest) {
                closestPoint = findNearest(point, node.rNode, rightRect(node, rect), closest, closestPoint);
            }
        }
        return closestPoint;
    }


    private void checkArgument(Object o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        Point2D point2D = new Point2D(0.2, 0.2);

        long start = System.currentTimeMillis();
        kdtree.nearest(point2D).draw();
        long end = System.currentTimeMillis();
        System.out.println("kdtree time: "+(end-start));

        start = System.currentTimeMillis();
        brute.nearest(point2D).draw();
        end = System.currentTimeMillis();
        System.out.println("brute time: "+(end-start));

        kdtree.draw();
    }
}
