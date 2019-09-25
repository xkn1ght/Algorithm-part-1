import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordNet;

    public Outcast(WordNet wordNet){
        this.wordNet = wordNet;
    }

    public String outcast(String[] nouns) {
        final int length = nouns.length;
        int[] distances = new int[length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (i == j) {
                    continue;
                }
                String word = nouns[i];
                distances[i] += wordNet.distance(word, nouns[j]);
            }
        }

        int MAX = -1;
        int max = -1;
        for (int i = 0; i < length; i++) {
            if (distances[i] > MAX) {
                max = i;
                MAX = distances[i];
            }
        }

        return nouns[max];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
