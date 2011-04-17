package edu.nku.cs.csc440.team2.message;

/**
 *
 * @author Shane Crandall
 * @version 0
 */
public class SmilDimension {
    
    private int width, height;

    public SmilDimension() {
        width = 0;
        height = 0;
    }

    public SmilDimension(SmilDimension smilDimension) {
        width = smilDimension.getWidth();
        height = smilDimension.getHeight();
    }

    public SmilDimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public boolean equals(Object obj) {
        SmilDimension smilDimension = (SmilDimension) obj;
        if (this.width == smilDimension.getWidth() && this.height == smilDimension.getHeight()) {
            return true;
        } else {
            return false;
        }
    }
    
}
