import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double mean;
    private double dev;
    private double[] experiments;
    private Percolation model;

    public PercolationStats(int n, int trials){
        if(n<=0||trials<=0){
            throw new IllegalArgumentException("percolationstats");
        }
        experiments = new double[trials];
        for(int i = 0; i<trials; ++i){
            model = new Percolation(n);
            while(!model.percolates()){
                model.open(StdRandom.uniform(1,n+1),StdRandom.uniform(1,n+1));
            }
            experiments[i] = model.numberOfOpenSites() / (n*n*1.0);
        }
        mean = StdStats.mean(experiments);
        dev = StdStats.stddev(experiments);
    }

    public double mean(){
        return mean;
    }

    public double stddev(){
        return dev;
    }

    public double confidenceLo(){
        return mean-1.96*Math.sqrt(dev/experiments.length);
    }

    public double confidenceHi(){
        return mean+1.96*Math.sqrt(dev/experiments.length);
    }

    public static void main(String[] args){
        if(args.length!=2){
            throw new IllegalArgumentException("incorrect arguments");
        }

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

//        int n = 200;
//        int trials = 40;

        int confidenceSum = 0;

        PercolationStats percolationStats = new PercolationStats(n,trials);
        double low = percolationStats.confidenceLo();
        double high = percolationStats.confidenceHi();

        for(int i = 0; i<trials; i++){
            if(percolationStats.experiments[i]>=low&&percolationStats.experiments[i]<=high){
                ++confidenceSum;
            }
        }
        System.out.printf("%-30s%-20s","mean","= "+percolationStats.mean()+"\n");
        System.out.printf("%-30s%-20s","stddev","= "+percolationStats.stddev()+"\n");
        System.out.printf("%-30s%-20s","95% confidence interval","= ["+percolationStats.confidenceLo()+", "+percolationStats.confidenceHi()+"]");
    }
}
