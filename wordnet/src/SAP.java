import edu.princeton.cs.algs4.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author knight
 */

public class SAP {

    private final Digraph digraph;
    private final Iterable<Integer> topSort;

    private class Ancestor{
        private int ancestor;
        private int distance;

        public Ancestor(int ancestor, int distance) {
            this.ancestor = ancestor;
            this.distance = distance;
        }
    }

    public SAP(Digraph G) {
        digraph = G;

        DepthFirstOrder depthFirstOrder = new DepthFirstOrder(digraph);
        topSort =  depthFirstOrder.reversePost();
    }



    private List<Integer> makeList(int num){
        checkInteger(num);
        List<Integer> list = new LinkedList<>();
        list.add(num);
        return list;
    }

    public int length(int v, int w) {
        return length(makeList(v),makeList(w));

    }

    public int ancestor(int v, int w) {
        return ancestor(makeList(v), makeList(w));
    }

    private Ancestor findCommonAncestor(Iterable<Integer> vList, Iterable<Integer> wList){
        checkLists(vList,wList);
        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(digraph, vList);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(digraph, wList);

        int minDistance = Integer.MAX_VALUE;
        int ancestor = -1;

        for(int root : topSort){
            if(vPath.hasPathTo(root)&&wPath.hasPathTo(root)){
                int dist = vPath.distTo(root)+wPath.distTo(root);
                if(dist<minDistance){
                    minDistance = dist;
                    ancestor = root;
                }
            }
        }

        return new Ancestor(ancestor, ancestor==-1?-1:minDistance);

    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        Ancestor ancestor = findCommonAncestor(v,w);

        return ancestor.distance;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        Ancestor ancestor = findCommonAncestor(v,w);

        return ancestor.ancestor;
    }

    private void checkInteger(Integer v){
        if(v==null){
            throw new IllegalArgumentException("Integer or Integer in the list can not be null!");
        }
    }

    private void checkLists(Iterable<Integer> list1, Iterable<Integer> list2){
        if(list1==null||list2==null){
            throw new IllegalArgumentException("list's reference can not be null");
        }

        for(Integer a : list1){
            checkInteger(a);
        }

        for(Integer b:list2){
            checkInteger(b);
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

