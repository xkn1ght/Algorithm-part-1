import edu.princeton.cs.algs4.*;

public class Percolation {
    private int length;
    private boolean[][] grid;
    private WeightedQuickUnionUF UF;
    private int openSite;

    public Percolation(int n){
        if(n<0)
            throw new IllegalArgumentException("n<0");
        grid = new boolean[n][n];
        UF = new WeightedQuickUnionUF(n*n+2);
        length = n;
    }

    public void open(int row, int col){
        validateRange(row,col);

        if(isOpen(row,col))
            return;
        int site = (row-1)*length+col;
        if(row==1){ UF.union(site,0); }
        if(row==length) { UF.union(site,(length*length+1)); }
        try{
            if(isOpen(row-1,col)) { UF.union(site,site-length); }
            if(isOpen(row+1,col)) { UF.union(site,site+length); }
            if(isOpen(row,col+1)) { UF.union(site,site+1);}
            if(isOpen(row,col-1)) { UF.union(site,site-1); }
        }catch (IllegalArgumentException iae){

        }
        grid[row-1][col-1] = true;

        openSite++;
    }

    public boolean isOpen(int row, int col){
        validateRange(row,col);
        return grid[row-1][col-1];
    }

    public boolean isFull(int row, int col){
        validateRange(row,col);
        return UF.connected(0,(row-1)*length+col);
    }

    private void validateRange(int row, int col){
        if(row<1||row>length||col<1||col>length)
            throw new IllegalArgumentException("outsize the range");
    }

    public int numberOfOpenSites(){
        return openSite;
    }

    public boolean percolates(){
        return UF.connected(0,length*length+1);
    }
}
