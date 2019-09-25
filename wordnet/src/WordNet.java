import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;

import java.util.LinkedList;
import java.util.List;


/**
 * @author knight
 */
public class WordNet {

    private Digraph digraph;
    private synset[] synsets;
    private final RedBlackBST<String, List<synset>> redBlackBST;

    private class synset{
        private int id;
        private String[] words;

        public synset(int id, String[] synsets) {
            this.id = id;
            this.words = synsets;
        }
    }

    public WordNet(String synsets, String hypernyms){
        checkNull(synsets==null||hypernyms==null);

        redBlackBST = new RedBlackBST<>();

        makeSets(synsets);
        makeGraph(hypernyms);

    }

    private void makeSets(String synStr) {
        checkNull(synStr==null);

        In in = new In(synStr);
        String[] input = in.readAllLines();
        this.synsets = new synset[input.length];
        String[] line;
        for(int i = 0; i < input.length; i++){
            line = input[i].split(",");
            int id = Integer.parseInt(line[0]);

            String[] words = line[1].split(" ");
            synset syn = new synset(id,words);
            synsets[id] = syn;

            for(String s:words){
                List<synset> list = redBlackBST.get(s);
                if(list==null){
                    list = new LinkedList<>();
                }
                list.add(syn);
                //如果已经在里面，则替代
                redBlackBST.put(s,list);
            }
        }
    }

    private void makeGraph(String hypernyms) {
        checkNull(hypernyms == null);
        In in = new In(hypernyms);
        String[] input = in.readAllLines();
        int length = input.length;
        digraph = new Digraph(length);
        for (int i = 0; i < length; i++) {
            String[] line = input[i].split(",");
            int v = Integer.parseInt(line[0]);

            int lineLength = line.length;
            for (int j = 1; j < lineLength; j++) {
                digraph.addEdge(v, Integer.parseInt(line[j]));
            }
        }
    }

    public Iterable<String> nouns(){
        return redBlackBST.keys();
    }

    public boolean isNoun(String word){
        checkNull(word == null);
        return redBlackBST.contains(word);
    }

    private List<Integer> vertices(String noun) {
        List<Integer> list = new LinkedList<>();
        for (synset synset : redBlackBST.get(noun)) {
            list.add(synset.id);
        }
        return list;
    }

    public int distance(String nounA, String nounB) {
        checkNull(nounA == null || nounB == null);

        if (!(isNoun(nounA) && isNoun(nounB))) {
            throw new IllegalArgumentException("wordNet should include the words!");
        }

        List v = vertices(nounA);
        List w = vertices(nounB);

        SAP sap = new SAP(digraph);
        return sap.length(v, w);
    }

    public String sap(String nounA, String nounB){
        checkNull(nounA==null||nounB==null);

        if(!(isNoun(nounA)&&isNoun(nounB))){
            throw new IllegalArgumentException("wordNet should include the words!");
        }

        List v = vertices(nounA);
        List w = vertices(nounB);

        SAP sap = new SAP(digraph);
        int key = sap.ancestor(v, w);

        return String.join(" ",synsets[key].words);
    }


    private void checkNull(boolean b) {
        if (b) {
            throw new IllegalArgumentException("variable can't be null");
        }
    }
}
