package edu.nku.cs.csc440.team2.message;

/**
 *
 * @author Shane Crandall
 * @version 0
 */
public class Text extends TextImage {

    private static final char T = 't';
    private static int count = 0;

    public Text(double end, String src, Region region) {
        super(end, src, region);
        id = T + String.valueOf(++count);
    }

    public Text(double begin, double end, String src, Region region) {
        super(begin, end, src, region);
        id = T + String.valueOf(++count);
    }

    Text(double begin, double end, String src, Region region, String id) {
        super(begin, end, src, region);
        this.id = id;
    }
}
