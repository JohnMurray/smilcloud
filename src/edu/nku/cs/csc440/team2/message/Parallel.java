package edu.nku.cs.csc440.team2.message;

/**
 *
 * @author Shane Crandall
 * @version 0
 */
public class Parallel extends Timing {

    private final static char P = 'p';
    private static int count = 0;

    public Parallel() {
        super();
        id = P + String.valueOf(++count);
    }

    public Parallel(double begin) {
        super(begin);
        id = P + String.valueOf(++count);
    }

    public Parallel(double begin, double end) {
        super(begin, end);
        id = P + String.valueOf(++count);
    }

    Parallel(double begin, double end, String id) {
        super(begin);
        setEndTiming(end);
        this.id = id;
    }
}
