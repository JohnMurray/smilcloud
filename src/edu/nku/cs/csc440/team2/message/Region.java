package edu.nku.cs.csc440.team2.message;

/**
 *
 * @author Shane Crandall
 * @version 0
 */
public class Region extends RootLayout implements Comparable<Region> {

    public final static String HIDDEN = "hidden";
    public final static String FILL = "fill";
    public final static String MEET = "meet";
    public final static String SLICE = "slice";
    private final static char R = 'r';
    private static int count = 0;
    private SmilDimension origin = new SmilDimension(0, 0);
    private int zIndex = 0;
    private String fit = HIDDEN;

    public Region() {
        super();
        id = R + String.valueOf(++count);
    }

    public Region(SmilDimension dimensions) {
        super(dimensions);
        id = R + String.valueOf(++count);
    }

    public Region(SmilDimension dimensions, String backgroundColor, SmilDimension origin, String fit) {
        super(dimensions, backgroundColor);
        setOrigin(origin);
        setFit(fit);
        id = R + String.valueOf(++count);
    }

    Region(SmilDimension dimensions, String backgroundColor, SmilDimension origin, String fit, String id, int zIndex) {
        super(dimensions, backgroundColor);
        this.origin = origin;
        this.fit = fit;
        this.id = id;
        this.zIndex = zIndex;
    }

    public String getId() {
        return id;
    }

    public String getFit() {
        return fit;
    }

    public void setFit(String fit) {
        if (fit != null && (fit.equals(HIDDEN) || fit.equals(FILL)
                || fit.equals(MEET) || fit.equals(SLICE))) {
            this.fit = fit;
        }
    }

    public SmilDimension getOrigin() {
        return origin;
    }

    public void setOrigin(SmilDimension origin) {
        if (origin != null) {
            this.origin = origin;
        }
    }

    public int getZindex() {
        return zIndex;
    }

    public void setZindex(int zIndex) {
        this.zIndex = zIndex;
    }

    @Override
    String toXml() {
        // <region id="r1" width="225" height="300" background-color="#ADFF2F" left="10" top="10" z-index="0" fit="hidden"/>
        StringBuilder xml = new StringBuilder("<region");
        xml.append(format("id", id));
        xml.append(super.toXml());
        xml.append(format("left", origin.getWidth()));
        xml.append(format("top", origin.getHeight()));
        xml.append(format("z-index", zIndex));
        xml.append(format("fit", fit));
        xml.append("/>\n");
        return xml.toString();
    }

    public int compareTo(Region region) {
        if (Integer.valueOf(id.substring(1)) < Integer.valueOf(region.getId().substring(1))) {
            return -1;
        } else if (Integer.valueOf(id.substring(1)) > Integer.valueOf(region.getId().substring(1))) {
            return 1;
        } else {
            return 0;
        }
    }
}
