package edu.nku.cs.csc440.team2.message;

/**
 *
 * @author Shane Crandall
 * @version 0
 */
public class Video extends AudioVideo {

    private static final char V = 'v';
    private static int count = 0;

    public Video(double end, String src, Region region) {
        super(end, src);
        setRegion(region);
        id = V + String.valueOf(++count);
    }

    public Video(double begin, double end, String src, Region region, double clipBegin, double clipEnd) {
        super(begin, end, src, clipBegin, clipEnd);
        setRegion(region);
        id = V + String.valueOf(++count);
    }

    Video(double begin, double end, String src, Region region, double clipBegin, double clipEnd, String id) {
        super(begin, end, src, clipBegin, clipEnd);
        setRegion(region);
        this.id = id;
    }
}
