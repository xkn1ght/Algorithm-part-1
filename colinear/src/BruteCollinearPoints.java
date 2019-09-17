import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;

public class BruteCollinearPoints {
    private LinkedList<LineSegment> segments;
    private int number;

    public BruteCollinearPoints(Point[] points){
        checkException(points);

        Point[] sortedPoint = points.clone();
        Arrays.sort(sortedPoint);
        checkDuplicate(sortedPoint);
        int lengths = sortedPoint.length;

        number = 0;
        segments = new LinkedList<>();

        for(int i = 0;i <lengths; i++){
            for(int j = i+1; j<lengths; j++){
                double slopeIJ = sortedPoint[i].slopeTo(sortedPoint[j]);
                for(int k = j+1; k<lengths; k++){
                    double slopeIK = sortedPoint[i].slopeTo(sortedPoint[k]);
                    if(slopeIJ!=slopeIK)
                        continue;
                    for(int h = k+1; h<lengths; h++){
                        double slopeIH = sortedPoint[i].slopeTo(sortedPoint[h]);
                        if(slopeIH==slopeIJ){
                            segments.add(new LineSegment(sortedPoint[i],sortedPoint[h]));
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments(){
        return number;
    }

    public LineSegment[] segments(){
        return segments.toArray(new LineSegment[0]);
    }

    private void checkDuplicate(Point[] points){
        int lengths = points.length;
        for(int i = 0; i<lengths - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void checkException(Point[] points) {
        if(points==null){
            throw new IllegalArgumentException();
        }
        for(Point p :points){
            if(p==null)
                throw new IllegalArgumentException();
        }
    }
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        long startTime = System.currentTimeMillis();
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
