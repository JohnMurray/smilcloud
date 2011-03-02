package edu.nku.cs.csc440.team2.message;

/**
 *
 * @author Shane Crandall
 * @version 0
 */
public abstract class Media extends Body {

    public final static String REMOVE = "remove";
    public final static String FREEZE = "freeze";
    private String src = null;
    private Region region = null;
    private String fill = REMOVE;

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        boolean isAudio = this.getClass().getName().contains("Audio");
        if (fill == null) {
            if (isAudio) {
                this.fill = fill;
            }
        } else {
            if ((!isAudio && fill.equals(REMOVE)) || (!isAudio && fill.equals(FREEZE))) {
                this.fill = fill;
            }
        }
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        boolean isAudio = this.getClass().getName().contains("Audio");
        if (region == null) {
            if (isAudio) {
                this.region = region;
            } else {
                this.region = new Region();
            }
        } else {
            if (!isAudio) {
                this.region = region;
            }
        }
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = new CdataValidator().validate(src, "%");
    }

    @Override
    String toXml() {
        // <text id="t0" repeat="1" begin="1.5" end="8.5" src="http://www.textfiles.com/anarchy/206.txt" region="r1" fill="remove"/>
        // <img id="i0" repeat="1" begin="0.5" end="2.0" src="image.jpg" region="r3" fill="remove"/>
        // <audio id="a1" repeat="1" begin="0.0" end="12.0" src="audio.mp3" clip-begin="3.0" clip-end="15.0"/>
        // <video id="v1" repeat="1" begin="0.5" end="9.5" src="video.mp4" region="r1" fill="remove" clip-begin="12.0" clip-end="21.0"/>
        StringBuilder xml = new StringBuilder();
        if (id.startsWith("t")) {
            xml.append("<text");
        }
        if (id.startsWith("i")) {
            xml.append("<img");
        }
        if (id.startsWith("a")) {
            xml.append("<audio");
        }
        if (id.startsWith("v")) {
            xml.append("<video");
        }
        xml.append(super.toXml());
        xml.append(format("src", getSrc()));
        if (!id.startsWith("a")) {
            xml.append(format("region", getRegion().getId()));
            xml.append(format("fill", getFill()));
        }
        if (!id.startsWith("a") && !id.startsWith("v")) {
            xml.append("/>\n");
        }
        return xml.toString();
    }
}
