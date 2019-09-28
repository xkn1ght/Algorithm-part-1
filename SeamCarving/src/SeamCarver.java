import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import javax.swing.border.EmptyBorder;
import javax.swing.text.StyledEditorKit;

/**
 * @author knight
 * @date 2019/9/27 21:27
 * created
 */

//todo 调试
public class SeamCarver {

    private Picture picture;
    //column[]  row[]
    private final double energy[][];
    private int width;
    private int height;

    private enum direction{
        vertical,
        horizontal,
    }

    private enum channel{
        R,
        G,
        B,
    }

    public SeamCarver(Picture picture) {
        checkNull(picture);

        updatePicture(picture);

        energy = new double[width][];

        int NUM = 0;

        for(int i = 0; i<width; i++) {
            energy[i] = new double[height];
            for(int j = 0; j<height; j++){
                energy[i][j] = energy(i,j);
            }
        }
    }

    public Picture picture() {
        return picture;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double energy(int x, int y) {
        checkIndex(x, y);
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
            return 1000;
        } else {
            double R_yeild_x = Math.abs(getRGBValue(x + 1, y, channel.R) - getRGBValue(x - 1, y, channel.R));
            double G_yeild_x = Math.abs(getRGBValue(x + 1, y, channel.G) - getRGBValue(x - 1, y, channel.G));
            double B_yeild_x = Math.abs(getRGBValue(x + 1, y, channel.B) - getRGBValue(x, y, channel.B));
            double yielding_x = Math.pow(R_yeild_x, 2) + Math.pow(G_yeild_x, 2) + Math.pow(B_yeild_x, 2);

            double R_yeild_y = Math.abs(getRGBValue(x, y + 1, channel.R) - getRGBValue(x, y - 1, channel.R));
            double G_yeild_y = Math.abs(getRGBValue(x, y + 1, channel.G) - getRGBValue(x, y - 1, channel.G));
            double B_yeild_y = Math.abs(getRGBValue(x, y + 1, channel.B) - getRGBValue(x, y - 1, channel.B));
            double yielding_y = Math.pow(R_yeild_y, 2) + Math.pow(G_yeild_y, 2) + Math.pow(B_yeild_y, 2);

            return Math.sqrt(yielding_x + yielding_y);

        }
    }

    public int[] findHorizontalSeam() {
        transportPicture();
        int[] temp = findSeamVertical();
        transportPicture();
        return temp;
    }

    private int[] findSeamVertical() {
        int xIndex = 1;
//实际上是一个简化的dij算法
        double destEnergy = Double.MAX_VALUE;

        int[] indexes = new int[height];
        int[] destIndexes = null;
        double energy;
        for (int i = 1; i < width-1; i += 2) {
            energy = 1000.0;
            xIndex = i;
            for (int j = 1; j < height; j++) {
                double left = energy(xIndex - 1, j);
                double right = energy(xIndex + 1, j);
                double center = energy(xIndex, j);
                if (left < right) {
                    if (left < center) {
                        energy += left;
                        xIndex--;
                    } else {
                        energy += center;
                    }
                } else {
                    if (right < center) {
                        energy += right;
                        xIndex++;
                    } else {
                        energy += center;
                    }
                }
                indexes[j] = xIndex;
            }
            if(energy<destEnergy){
                destEnergy = energy;
                destIndexes = indexes;
                indexes = new int[height];
            }
        }
        return destIndexes;
    }

    public int[] findVerticalSeam() {
        return findSeamVertical();
    }

    public void removeHorizontalSeam(int[] seam) {
        transportPicture();
        checkValidSeam(seam, direction.vertical);
        removeSeamVertical(seam);
        transportPicture();
    }

    private void removeSeamVertical(int[] seam) {
        checkNull(seam);
        checkPictureSize(direction.vertical);

        Picture temp = new Picture(width - 1, height);

        for (int i = 0; i < height; i++) {
            int index = 0;

            for (int j = 0; j < width - 1; j++) {
                if (j == seam[i]) {
                    index++;
                }
                temp.setRGB(j, i, picture.getRGB(index++, i));

            }
        }
        updatePicture(temp);
    }

    public void removeVerticalSeam(int[] seam) {
        checkValidSeam(seam, direction.vertical);
        removeSeamVertical(seam);
    }

    private int getRGBValue(int x,int y,channel c){

        int RGB = picture.getRGB(x,y);
        switch (c){
            case R:
                return (RGB>>16)&0xFF;
            case G:
                return (RGB>>8)&0xFF;
            case B:
                return (RGB>>0)&0xFF;
                default:
                    return -1;
        }
    }

    public void transportPicture() {

        Picture temp = new Picture(height, width);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                temp.setRGB(j, i, picture.getRGB(i, j));
            }
        }
        updatePicture(temp);
    }

    private void checkPictureSize(direction d) {
        if (d == direction.horizontal && height <= 1) {
            throw new IllegalArgumentException("the height of the picture is no more than one! ");
        } else if (d == direction.vertical && width <= 1) {
            throw new IllegalArgumentException("the width of the picture is no more than one! ");
        }
    }

    private void checkIndex(int x, int y) {
        if (x >= width || x < 0 || y >= height || y < 0) {
            throw new IllegalArgumentException("the input index is out of bound! "+x+"  "+y);
        }
    }

    private void checkValidSeam(int[] seam, direction d){
        if (d == direction.vertical){
            if(seam.length!=height){
                throw new IllegalArgumentException("the length of array not equal to height  "+seam.length+" height: "+height);
            }else{
                for(int i = 0; i <seam.length;i++){
                    checkIndex(seam[i],0);
                }
            }
        }else if (d==direction.horizontal){
            if(seam.length!=width){
                throw new IllegalArgumentException("the length of arrat not eaual to width");
            }else{
                for(int i = 0; i <seam.length;i++){
                    checkIndex(0,seam[i]);
                }
            }
        }
    }

    private void checkNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("para should not be null!");
        }
    }

    private void updatePicture(Picture picture){
        this.picture = picture;
        this.height = picture.height();
        this.width = picture.width();
    }

    public static void main(String args[]) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("image is %d columns by %d rows\n", picture.width(), picture.height());

        SeamCarver sc = new SeamCarver(picture);
        sc.picture.show();
        sc.removeHorizontalSeam(sc.findHorizontalSeam());
        StdOut.printf("image is %d columns by %d rows\n", picture.width(), picture.height());

        sc.picture.show();

    }
}
