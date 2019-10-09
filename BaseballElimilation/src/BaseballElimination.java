import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author knight
 */
public class BaseballElimination {
    private final int TEAM_NUMBER;
    private RedBlackBST<String,TeamNode> redBlackBST;
    private RedBlackBST<String, LinkedList<String>> subsetElimination;
    private String[] names;

    public BaseballElimination(String fileName){
        checkNull(fileName);
        redBlackBST = new RedBlackBST<>();
        subsetElimination = new RedBlackBST<>();
        final int TeamNameIndex = 0;
        In inputFile = new In(fileName);

        TEAM_NUMBER = Integer.parseInt(inputFile.readLine());
        names = new String[TEAM_NUMBER];

        String[] lines = inputFile.readAllLines();

        for(int i = 0; i< TEAM_NUMBER; i++){
            String teamInformation = lines[i];
            String[] iter = teamInformation.split(" +");
            String teamName = iter[TeamNameIndex];
            //iter 包括队伍名
            int[] infos = new int[iter.length-2];
            int infosIndex = 0;
            int againstIndex = 0;
            for(String s:iter){
                //start after team name;
                if(infosIndex==0||infosIndex==4+i){
                    infosIndex++;
                    continue;
                }
                infos[againstIndex++] = Integer.parseInt(s);
                infosIndex++;
            }
            redBlackBST.put(teamName,new TeamNode(infos));
            names[i] = teamName;
        }

        elimination();
    }

    private class TeamNode{
        private int won;
        private int losed;
        private int left;
        private int[] againstTeams;

        private TeamNode(int...teamInfos) {
            final int INTERVAL = 3;
            final int LENGTH = teamInfos.length;
            this.won = teamInfos[0];
            this.losed = teamInfos[1];
            this.left = teamInfos[2];
            againstTeams = new int[LENGTH-INTERVAL];
            System.arraycopy(teamInfos, INTERVAL, againstTeams, 0, LENGTH-INTERVAL);
        }
    }

    public Iterable<String> teams(){
        return Arrays.asList(names);
    }

    public int numberOfTeams(){
        return TEAM_NUMBER;
    }

    public int wins(String teams){
        checkNull(teams);
        return redBlackBST.get(teams).won;
    }

    public int losses(String team){
        checkNull(team);
        return redBlackBST.get(team).losed;
    }

    public int remaining(String team)
    {
        checkNull(team);
        return redBlackBST.get(team).left;
    }

    public int against(String team1, String team2){
        checkNull(team1,team2);
        return redBlackBST.get(team1).againstTeams[teamNumber(team2)];
    }

    public boolean isEliminated(String team) {
        checkNull(team);
        LinkedList<String> linkedList = subsetElimination.get(team);
        return linkedList != null;
    }

    public Iterable<String> certificateOfElimination(String team) {
        checkNull(team);
        return subsetElimination.get(team);
    }

    //todo 需要判断是计算哪一只队伍的淘汰吗 还是做一个循环？  如何做一个循环呢
    private void elimination(){
        final int INTERVAL = (TEAM_NUMBER-1)*(TEAM_NUMBER-2)/2;

        //有多少用简单的方法已经计算出来
        LinkedList<String> marks = new LinkedList<>();
        int[] wins = new int[TEAM_NUMBER];
        int[] toBeWin = new int[TEAM_NUMBER];
        //标记node
        int index = 0;

        //todo congratulation you encounter the problem of stability of sorting.
        for (String team:teams()){
            TeamNode node = redBlackBST.get(team);
            wins[index] = node.won;

            toBeWin[index++] = node.left+node.won;
        }

        for(int i = 0; i< TEAM_NUMBER; i++){
            for(int j = 0; j<TEAM_NUMBER;j++){
                if(i==j){
                    continue;
                }
                if(toBeWin[i]<wins[j]){
                    generateSubset(i, wins, toBeWin[i], j);
                    marks.add(names[i]);
                    break;
                }
            }
        }

        //generate graph and ff-method;
        int s = 0,t = culcNetNodes()-1;

        //判断是否已经淘汰
        boolean isMark = false;
        //foreach team-->i
        for(int i = 0;i<TEAM_NUMBER;i++){
            for(String mark :marks){
                if(names[i].equals(mark)){
                    isMark = true;
                    break;
                }
            }
            if(!isMark){
                //init graph
                FlowNetwork flowNetwork = new FlowNetwork(t+1);
                index = 1;
                //录入第j支球队的信息
                int jIndex = 1;
                int source = 0;
                for(int j = 0; j<TEAM_NUMBER;j++) {
                    //计算i球队无需计算j球队.
                    if(j==i){
                        continue;
                    }
                    TeamNode iter = redBlackBST.get(names[j]);
                    //在j球队中录入k球队的对战信息
                    int kIndex = 1;
                    for (int k = 0; k < j; k++) {
                        //todo 新建一个节点
                        if(k==i){
                            continue;
                        }
                        source+=iter.againstTeams[k];
                        flowNetwork.addEdge(new FlowEdge(0, index, iter.againstTeams[k]));
                        flowNetwork.addEdge(new FlowEdge(index, kIndex + INTERVAL, Double.MAX_VALUE));
                        flowNetwork.addEdge(new FlowEdge(index, jIndex + INTERVAL, Double.MAX_VALUE));

                        index++;
                        kIndex++;
                    }
                    //添加第三层
                    flowNetwork.addEdge(new FlowEdge(jIndex+INTERVAL,t,toBeWin[i]-wins[j]));
                    jIndex++;
                }
                FordFulkerson ff = new FordFulkerson(flowNetwork,s,t);
                int maxFlow = (int)ff.value();

                if(maxFlow!=source){
                    LinkedList<String> linkedList = new LinkedList<>();
                    int lIndex = 1;
                    for(int l = 0; l<TEAM_NUMBER; l++){
                        if(l==i){
                            continue;
                        }
                        if(ff.inCut(lIndex+INTERVAL)){
                            linkedList.add(names[l]);
                        }
                        lIndex++;
                    }
                    subsetElimination.put(names[i],linkedList);
                }
            }
        }

    }

    private int culcNetNodes() {
        return 1 + (TEAM_NUMBER-1)*(TEAM_NUMBER-2)/2 + TEAM_NUMBER - 1 + 1;
    }

    private void generateSubset(int team,int[] wins, int candidateWins, int from) {
        LinkedList<String> subsetTeams = new LinkedList<>();
        for(int k = from; k<TEAM_NUMBER;k++){
            if(candidateWins<wins[k]){
                subsetTeams.add(names[k]);
            }
        }
        subsetElimination.put(names[team],subsetTeams);
    }

    private int teamNumber(String team){
        checkNull(team);
        for(int i = 0; i<names.length; i++){
            if(names[i].equals(team)){
                return i;
            }
        }
        return -1;
    }

    private void checkNull(String s) {
        if (s == null || s.equals("")) {
            throw new IllegalArgumentException("string can't be null or empty");
        }
    }

    private void checkNull(String s1, String s2) {
        if (s1 == null || s2 == null || s1.equals("") || s2.equals("")) {
            throw new IllegalArgumentException("string can't be null or empty");
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}
