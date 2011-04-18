/*
 * Region.java
 */
package edu.nku.cs.csc440.team2.message;

/**
 * Java binding for region elements defined and referenced within a SMIL
 * message; each region object has an automatically generated unique id;
 * "Specifies an abstract rendering surface [and] controls the position, size
 * and scaling of media object elements." - w3.org
 * 
 * @author Shane Crandall
 * @version 1.0, 04/18/11
 */
public class Region extends RootLayout implements Comparable<Region> {

	/**
	 * "If the intrinsic height (width) of the media object element is smaller
	 * than the height (width) defined in the "region" element, render the
	 * object starting from the top (left) edge and fill up the remaining height
	 * (width) with the background color." - w3.org
	 * 
	 * "If the intrinsic height (width) of the media object element is greater
	 * than the height (width) defined in the "region" element, render the
	 * object starting from the top (left) edge until the height (width) defined
	 * in the "region" element is reached, and clip the parts of the object
	 * below (right of) the height (width)." - w3.org
	 */
	public final static String HIDDEN = "hidden";

	/**
	 * "Scale the object's height and width independently so that the content
	 * just touches all edges of the box." - w3.org
	 */
	public final static String FILL = "fill";

	/**
	 * "Scale the visual media object while preserving its aspect ratio until
	 * its height or width is equal to the value specified by the height or
	 * width attributes, while none of the content is clipped. The object's left
	 * top corner is positioned at the top-left coordinates of the box, and
	 * empty space at the left or bottom is filled up with the background
	 * color." - w3.org
	 */
	public final static String MEET = "meet";

	/**
	 * "Scale the visual media object while preserving its aspect ratio so that
	 * its height or width are equal to the value specified by the height and
	 * width attributes while some of the content may get clipped. Depending on
	 * the exact situation, either a horizontal or a vertical slice of the
	 * visual media object is displayed. Overflow width is clipped from the
	 * right of the media object. Overflow height is clipped from the bottom of
	 * the media object." - w3.org
	 */
	public final static String SLICE = "slice";

	/**
	 * region id prefix
	 */
	private final static char R = 'r';

	/**
	 * region id counter
	 */
	private static int count = 0;

	/**
	 * region origin with both default coordinates of zero
	 */
	private SmilDimension origin = new SmilDimension(0, 0);

	/**
	 * "Specifies the stack level of the box in the current stacking context." -
	 * w3.org; default value is zero
	 */
	private int zIndex = 0;

	/**
	 * "Specifies the behavior if the intrinsic height and width of a visual
	 * media object differ from the values specified by the height and width
	 * attributes in the "region" element." - w3.org; default value is "hidden"
	 */
	private String fit = HIDDEN;

	/**
	 * default constructor to construct Region with defaults
	 */
	public Region() {
		super();
		id = R + String.valueOf(++count);
	}

	/**
	 * constructs new Region from a dimension and all remaining default values
	 * 
	 * @param dimensions
	 *            dimensions to use
	 */
	public Region(SmilDimension dimensions) {
		super(dimensions);
		id = R + String.valueOf(++count);
	}

	/**
	 * constructs new Region with four arguments and the default z-index value
	 * 
	 * @param dimensions
	 *            dimensions to use
	 * @param backgroundColor
	 *            background color
	 * @param origin
	 *            dimensions of origin
	 * @param fit
	 *            fit to use
	 */
	public Region(SmilDimension dimensions, String backgroundColor,
			SmilDimension origin, String fit) {
		super(dimensions, backgroundColor);
		setOrigin(origin);
		setFit(fit);
		id = R + String.valueOf(++count);
	}

	/**
	 * constructs new Region with no default or generated values; only used from
	 * within package to generate anonymous convenience objects
	 * 
	 * @param dimensions
	 *            dimensions to use
	 * @param backgroundColor
	 *            background color
	 * @param origin
	 *            dimensions of origin
	 * @param fit
	 *            fit to use
	 * @param id
	 *            id to use
	 * @param zIndex
	 *            z-index to use
	 */
	Region(SmilDimension dimensions, String backgroundColor,
			SmilDimension origin, String fit, String id, int zIndex) {
		super(dimensions, backgroundColor);
		this.origin = origin;
		this.fit = fit;
		this.id = id;
		this.zIndex = zIndex;
	}

	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return fit
	 */
	public String getFit() {
		return fit;
	}

	/**
	 * sets fit if specified correctly
	 * 
	 * @param fit
	 *            fit requested
	 */
	public void setFit(String fit) {
		if (fit != null
				&& (fit.equals(HIDDEN) || fit.equals(FILL) || fit.equals(MEET) || fit
						.equals(SLICE))) {
			this.fit = fit;
		}
	}

	/**
	 * @return dimension of origin
	 */
	public SmilDimension getOrigin() {
		return origin;
	}

	/**
	 * sets origin if argument is not null
	 * 
	 * @param origin
	 *            origin to use
	 */
	public void setOrigin(SmilDimension origin) {
		if (origin != null) {
			this.origin = origin;
		}
	}

	/**
	 * @return z-index
	 */
	public int getZindex() {
		return zIndex;
	}

	/**
	 * sets z-index
	 * 
	 * @param zIndex
	 *            z-index to use
	 */
	public void setZindex(int zIndex) {
		this.zIndex = zIndex;
	}

	@Override
	String toXml() {
		// <region id="r1" width="225" height="300" background-color="#ADFF2F"
		// left="10" top="10" z-index="0" fit="hidden"/>
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

	/*
	 * (non-Javadoc) orders region elements by the integer value of their unique
	 * id's
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Region region) {
		if (Integer.valueOf(id.substring(1)) < Integer.valueOf(region.getId()
				.substring(1))) {
			return -1;
		} else if (Integer.valueOf(id.substring(1)) > Integer.valueOf(region
				.getId().substring(1))) {
			return 1;
		} else {
			return 0;
		}
	}
}
