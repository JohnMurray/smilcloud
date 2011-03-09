package edu.nku.cs.csc440.team2.message;

/**
 *
 * @author Shane Crandall
 * @version 0
 */
public abstract class Body implements Comparable<Body> {

    String id;
    private int repeat = 1;
    private double begin = 0.0;
    private double end = -1.0;

    String toXml() { // update conditional "end"
        StringBuilder xml = new StringBuilder();
        xml.append(format("id", id));
        xml.append(format("repeat", repeat));
        xml.append(format("begin", begin));
        if (end != -1.0) {
            xml.append(format("end", end));
        }
        return xml.toString();
    }

    String format(String keyword, Object insideQuotes) {
        return " " + keyword + "=\"" + String.valueOf(insideQuotes) + "\"";
    }

    public String getId() {
        return id;
    }

    public double getBegin() {
        return begin;
    }

    public void setBegin(double begin) {
        if (begin >= 0.0) {
            this.begin = begin;
        }
    }

    public double getEnd() {
        return end;
    }

    void setEndTiming(double end) {
        this.end = end;
    }

    public void setEnd(double end) {
        if (end >= 0.0) {
            this.end = end;
        } else if (this.end < 0.0) {
            this.end = 0.0;
        } else;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        if (repeat >= 1) {
            this.repeat = repeat;
        }
    }

    public int compareTo(Body b) {
        if (getId().charAt(0) < b.getId().charAt(0)) {
            return -1;
        } else if (getId().charAt(0) > b.getId().charAt(0)) {
            return 1;
        } else {
            if (Integer.valueOf(getId().substring(1)) < Integer.valueOf(b.getId().substring(1))) {
                return -1;
            } else if (Integer.valueOf(getId().substring(1)) > Integer.valueOf(b.getId().substring(1))) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
