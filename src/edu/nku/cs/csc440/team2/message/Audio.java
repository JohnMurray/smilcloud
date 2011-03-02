package edu.nku.cs.csc440.team2.message;

/**
 *
 * @author Shane Crandall
 * @version 0
 */
public class Audio extends AudioVideo {

    private final static char A = 'a';
    private static int count = 0;

    public Audio(double end, String src) {
        super(end, src);
        id = A + String.valueOf(++count);
        this.setFill(null);
        this.setRegion(null);
    }

    public Audio(double begin, double end, String src, double clipBegin, double clipEnd) {
        super(begin, end, src, clipBegin, clipEnd);
        id = A + String.valueOf(++count);
        this.setFill(null);
        this.setRegion(null);
    }

    Audio(double begin, double end, String src, double clipBegin, double clipEnd, String id) {
        super(begin, end, src, clipBegin, clipEnd);
        this.id = id;
    }
}
