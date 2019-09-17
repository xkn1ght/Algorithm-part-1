import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;

public class FastCollinearPoints {

    private int number;
    private LinkedList<LineSegment> segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points){
        checkException(points);

        Point[] sortByPoint = points.clone();
        Arrays.sort(sortByPoint);
        checkDuplicate(points);

        final int lengths = points.length;

        segments = new LinkedList<>();
        LinkedList<Double> segmentsSlope = new LinkedList<>();
        number = 0;

        for(int i = 0; i<lengths; i++){
            Point[] pointsBySlope = sortByPoint.clone();
            Point comparer = sortByPoint[i];

            Arrays.sort(pointsBySlope,comparer.slopeOrder());

            int from = 1;
            int max = 1;
            Point tempBack = null;

            while(from<lengths){

                LinkedList<Point> candidates = new LinkedList<>();
                candidates.add(comparer);
                final double SLOPE_REF = comparer.slopeTo(pointsBySlope[from]);
                do{
                    max++;
                    candidates.add(pointsBySlope[from++]);
                }while(from < lengths && SLOPE_REF==comparer.slopeTo(pointsBySlope[from]));

                tempBack = pointsBySlope[from-1];//保存之前一个顶点

                if(max>=4) {//当候选点大于等于4
                    Point[] newLine = candidates.toArray(new Point[0]);
                    boolean isDuplicated = false;
                    Arrays.sort(newLine);
                    LineSegment temp = new LineSegment(newLine[0], newLine[newLine.length - 1]);
                    for (LineSegment line : segments) {
                        if (temp.toString().equals(line.toString())) {
                            isDuplicated = true;
                        }
                    }
                    if (!isDuplicated) {
                        segments.add(temp);
                    }
                }
                max = 1;

//                    candidatePoint = pointsBySlope[from];??
            }
        }
        System.out.println(segments);


    }

    // the number of line segments
    public int numberOfSegments(){
        return number;
    }
    // the line segments
    public LineSegment[] segments(){
        return segments.toArray(new LineSegment[0]);
    }

    private void checkDuplicate(Point[] points){
        int lengths = points.length;
        for(int i = 0; i<lengths - 1; i++){
            if(points[i].compareTo(points[i+1])==0){
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}