import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String args[]){
        int times = Integer.valueOf(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        for(int j = 1; j<args.length; j++){
            queue.enqueue(args[j]);
        }
        for(int i = 0; i<times; i++){
            StdOut.println(queue.dequeue());
        }

    }
}
