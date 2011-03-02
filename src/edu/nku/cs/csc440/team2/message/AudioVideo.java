package edu.nku.cs.csc440.team2.message;

/**
 *
 * @author Shane Crandall
 * @version 0
 */
public class AudioVideo extends Media {

    private double clipBegin = 0.0;
    private double clipEnd = 0.0;

    AudioVideo(double end, String src) {
        setEnd(end);
        setSrc(src);
        setClipEnd(end);
    }

    AudioVideo(double begin, double end, String src, double clipBegin, double clipEnd) {
        this(end, src);
        setBegin(begin);
        setClipBegin(clipBegin);
        setClipEnd(clipEnd);
    }

    public double getClipBegin() {
        return clipBegin;
    }

    public void setClipBegin(double clipBegin) {
        if (clipBegin >= 0.0) {
            this.clipBegin = clipBegin;
        }
    }

    public double getClipEnd() {
        return clipEnd;
    }

    public void setClipEnd(double clipEnd) {
        if (clipEnd >= 0.0) {
            this.clipEnd = clipEnd;
        }
    }

    @Override
    String toXml() {
        // <audio id="a1" repeat="1" begin="0.0" end="12.0" src="audio.mp3" clip-begin="3.0" clip-end="15.0"/>
        // <video id="v1" repeat="1" begin="0.5" end="9.5" src="video.mp4" region="r1" fill="remove" clip-begin="12.0" clip-end="21.0"/>
        StringBuilder xml = new StringBuilder(super.toXml());
        xml.append(format("clip-begin", getClipBegin()));
        xml.append(format("clip-end", getClipEnd()));
        xml.append("/>\n");
        return xml.toString();
    }
}
