package edu.nku.cs.csc440.team2.message;

import java.util.LinkedList;

/**
 *
 * @author Shane Crandall
 * @version 0
 */
public class Sequence extends Timing {

    private static final char S = 's';
    private static int count = 0;

    public Sequence() {
        super();
        id = S + String.valueOf(++count);
    }

    public Sequence(double begin) {
        super(begin);
        id = S + String.valueOf(++count);
    }

    public Sequence(double begin, double end) {
        super(begin, end);
        id = S + String.valueOf(++count);
    }

    Sequence(LinkedList<Body> content, boolean isImplicit) {
        this.content = content;
        this.isImplicit = isImplicit;
    }

    Sequence(double begin, double end, String id) {
        super(begin);
        setEndTiming(end);
        this.id = id;
    }
}
