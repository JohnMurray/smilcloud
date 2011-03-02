package edu.nku.cs.csc440.team2.message;

import java.awt.Dimension;

/**
 *
 * @author Shane Crandall
 * @version 0
 */
public class RootLayout {

    public final static String TRANSPARENT = "transparent";
    String id = null;
    private String backgroundColor = TRANSPARENT;
    private Dimension dimensions = new Dimension(480, 320);

    RootLayout() {
    }

    RootLayout(Dimension dimensions) {
        setDimensions(dimensions);
    }

    RootLayout(Dimension dimensions, String backgroundColor) {
        setDimensions(dimensions);
        if (new CdataValidator().validate(backgroundColor, "#") != null) {
            this.backgroundColor = backgroundColor;
        }
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public Dimension getDimensions() {
        return dimensions;
    }

    private void setDimensions(Dimension dimensions) {
        if (dimensions != null) {
            this.dimensions = dimensions;
        }
    }

    String format(String keyword, Object insideQuotes) {
        return " " + keyword + "=\"" + String.valueOf(insideQuotes) + "\"";
    }

    String toXml() {
        // <root-layout width="480" height="320" background-color="#98FB98"/>
        // <region id="r1" width="225" height="300" background-color="#ADFF2F" left="10" top="10" z-index="0" fit="hidden"/>
        StringBuilder xml = new StringBuilder();
        if (id == null) {
            xml.append("<root-layout");
        }
        xml.append(format("width", dimensions.width));
        xml.append(format("height", dimensions.height));
        xml.append(format("background-color", backgroundColor));
        if (id == null) {
            xml.append("/>\n");
        }
        return xml.toString();
    }
}
