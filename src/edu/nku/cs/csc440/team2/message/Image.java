package edu.nku.cs.csc440.team2.message;

/**
 *
 * @author Shane Crandall
 * @version 0
 */
public class Image extends TextImage {

    private final static char I = 'i';
    private static int count = 0;

    public Image(double end, String src, Region region) {
        super(end, src, region);
        id = I + String.valueOf(++count);
    }

    public Image(double begin, double end, String src, Region region) {
        super(begin, end, src, region);
        id = I + String.valueOf(++count);
    }

    Image(double begin, double end, String src, Region region, String id) {
        super(begin, end, src, region);
        this.id = id;
    }
}
