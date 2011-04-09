package edu.nku.cs.csc440.team2.message;

import com.example.helloandroid.HelloAndroid;
import android.test.ActivityInstrumentationTestCase2;

public class BodyMediaTest extends
		ActivityInstrumentationTestCase2<HelloAndroid> {

	static Sequence s2, s3, s4;
	static Parallel p2, p3, p4;
	static Text t2, t3;
	static Image i2, i3;
	static Audio a2, a3;
	static Video v2, v3;
	Body[] b;
	Media[] m;

	public BodyMediaTest() {
		super("com.example.helloandroid", HelloAndroid.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		System.out.println("* SequenceTest: test setUpClass - constructors");
		new Sequence(); // "end" = -1.0
		s2 = new Sequence(); // "end" = -1.0
		s3 = new Sequence(0.0); // "end" = -1.0
		s4 = new Sequence(Double.MIN_VALUE, Double.MAX_VALUE);

		System.out.println("* ParallelTest: test setUpClass - constructors");
		new Parallel(); // "end" = -1.0
		p2 = new Parallel(); // "end" = -1.0
		p3 = new Parallel(0.0); // "end" = -1.0
		p4 = new Parallel(Double.MAX_VALUE, Double.MIN_VALUE);

		System.out.println("* TextTest: test setUpClass - constructors");
		new Text(1.0, null, null); // anonymous
		t2 = new Text(1.0, null, new Region()); // short
		t3 = new Text(-1, -1, " text", null); // long

		System.out.println("* ImageTest: test setUpClass - constructors");
		new Image(1.0, null, null);
		i2 = new Image(1.0, null, new Region());
		i3 = new Image(-1, -1, " image", null);

		System.out.println("* AudioTest: test setUpClass - constructors");
		new Audio(1.0, null);
		a2 = new Audio(1.0, null);
		a3 = new Audio(-1, -1, " audio", -1, -1);

		System.out.println("* VideoTest: test setUpClass - constructors");
		new Video(1.0, null, null);
		v2 = new Video(1.0, null, new Region());
		v3 = new Video(-1, -1, " video", null, -1, -1);

		b = new Body[] { s2, p2, t2, i2, a2, v2 };
		m = new Media[] { t2, i2, a2, v2 };
	}

	public void testAll() {

		// repeatCheck
		for (Body body : b) {
			System.out.println("* " + body.getClass().getSimpleName()
					+ "Test: test method 1 - repeatCheck()");
			body.setRepeat(5);
			body.setRepeat(0);
			assertEquals(5, body.getRepeat());
		}
		v3.setRepeat(Integer.MAX_VALUE);

		// beginCheck
		for (Body body : b) {
			System.out.println("* " + body.getClass().getSimpleName()
					+ "Test: test method 2 - beginCheck()");
			body.setBegin(2.0);
			body.setBegin(-1.0);
			assertEquals(2.0, body.getBegin(), 0.0);
		}
		v3.setBegin(Double.MAX_VALUE);

		// endCheck
		assertEquals(-1.0, s2.getEnd(), 0.0);
		assertEquals(-1.0, s3.getEnd(), 0.0);
		assertEquals(-1.0, p2.getEnd(), 0.0);
		assertEquals(-1.0, p3.getEnd(), 0.0);

		for (Body body : b) {
			System.out.println("* " + body.getClass().getSimpleName()
					+ "Test: test method 3 - endCheck()");
			body.setEnd(2.0);
			body.setEnd(-1.0);
			assertEquals(2.0, body.getEnd(), 0.0);
		}
		v3.setEnd(Double.MAX_VALUE);

		// idCheck
		System.out.println("* MediaTest: test method 4 - idCheck()");

		assertEquals("s2", s2.getId());
		assertEquals("s3", s3.getId());
		assertEquals("s4", s4.getId());

		assertEquals("p2", p2.getId());
		assertEquals("p3", p3.getId());
		assertEquals("p4", p4.getId());

		assertEquals("t2", t2.getId());
		assertEquals("t3", t3.getId());

		assertEquals("i2", i2.getId());
		assertEquals("i3", i3.getId());

		assertEquals("a2", a2.getId());
		assertEquals("a3", a3.getId());

		assertEquals("v2", v2.getId());
		assertEquals("v3", v3.getId());

		// srcCheck
		for (Media media : m) {
			System.out.println("* " + media.getClass().getSimpleName()
					+ "Test: test method 5 - srcCheck()");
			media.setSrc("&ampersand");
			assertEquals(null, media.getSrc());
			media.setSrc("\\backslash");
			assertEquals(null, media.getSrc());
			media.setSrc("^carat");
			assertEquals(null, media.getSrc());
			media.setSrc("\rcarriageReturn");
			assertEquals(null, media.getSrc());
			media.setSrc("\"doubleQuote");
			assertEquals(null, media.getSrc());
			media.setSrc(">greaterThan");
			assertEquals(null, media.getSrc());
			media.setSrc("[leftBracket");
			assertEquals(null, media.getSrc());
			media.setSrc("<lessThan");
			assertEquals(null, media.getSrc());
			media.setSrc("\nlineFeed");
			assertEquals(null, media.getSrc());
			media.setSrc("+plus");
			assertEquals(null, media.getSrc());
			media.setSrc("]rightBracket");
			assertEquals(null, media.getSrc());
			media.setSrc(" space");
			assertEquals(null, media.getSrc());
			media.setSrc("\ttab");
			assertEquals(null, media.getSrc());
			media.setSrc("source.extension");
			assertEquals("source.extension", media.getSrc());
		}
		// regionCheck
		System.out.println("* MediaTest: test method 6 - regionCheck()");
		Region r = new Region(new SmilDimension(100, 100), "#98FB98",
				new SmilDimension(10, 10), "sliced");
		t2.setRegion(r);
		assertEquals(r, t2.getRegion());
		i2.setRegion(r);
		assertEquals(r, i2.getRegion());
		a2.setRegion(r);
		assertEquals(null, a2.getRegion());
		v2.setRegion(r);
		assertEquals(r, v2.getRegion());

		// fillCheck
		System.out.println("* MediaTest: test method 7 - fillCheck()");

		t2.setFill(null);
		assertEquals(Media.REMOVE, t2.getFill());

		i2.setFill("something");
		assertEquals(Media.REMOVE, i2.getFill());

		a2.setFill(Media.FREEZE);
		assertEquals(null, a2.getFill());

		v2.setFill(Media.FREEZE);
		assertEquals(Media.FREEZE, v2.getFill());

		// clipBeginCheck
		System.out.println("* AudioTest: test method 8 - clipBeginCheck()");
		a2.setClipBegin(2.0);
		a2.setClipBegin(-1.0);
		assertEquals(2.0, a2.getClipBegin(), 0.0);

		System.out.println("* VideoTest: test method 8 - clipBeginCheck()");
		v2.setClipBegin(2.0);
		v2.setClipBegin(-1.0);
		assertEquals(2.0, v2.getClipBegin(), 0.0);

		v3.setClipBegin(Double.MAX_VALUE);

		// clipEndCheck
		System.out.println("* AudioTest: test method 9 - clipEndCheck()");
		a2.setClipEnd(2.0);
		a2.setClipEnd(-1.0);
		assertEquals(2.0, a2.getClipEnd(), 0.0);

		System.out.println("* VideoTest: test method 9 - clipEndCheck()");
		v2.setClipEnd(2.0);
		v2.setClipEnd(-1.0);
		assertEquals(2.0, v2.getClipEnd(), 0.0);

		v3.setClipEnd(Double.MAX_VALUE);

		// toXmlCheck
		System.out.println("* SequenceTest: test method 10 - toXmlCheck()");
		assertEquals(
				"<seq id=\"s2\" repeat=\"5\" begin=\"2.0\" end=\"2.0\">\n</seq>\n",
				s2.toXml());
		assertEquals("<seq id=\"s3\" repeat=\"1\" begin=\"0.0\">\n</seq>\n",
				s3.toXml());
		assertEquals(
				"<seq id=\"s4\" repeat=\"1\" begin=\"4.9E-324\" end=\"1.7976931348623157E308\">\n</seq>\n",
				s4.toXml());

		System.out.println("* ParallelTest: test method 10 - toXmlCheck()");
		assertEquals(
				"<par id=\"p2\" repeat=\"5\" begin=\"2.0\" end=\"2.0\">\n</par>\n",
				p2.toXml());
		assertEquals("<par id=\"p3\" repeat=\"1\" begin=\"0.0\">\n</par>\n",
				p3.toXml());
		assertEquals(
				"<par id=\"p4\" repeat=\"1\" begin=\"1.7976931348623157E308\" end=\"4.9E-324\">\n</par>\n",
				p4.toXml());

		System.out.println("* TextTest: test method 10 - toXmlCheck()");
		assertEquals(
				"<text id=\"t2\" repeat=\"5\" begin=\"2.0\" end=\"2.0\" src=\"source.extension\" region=\"r10\" fill=\"remove\"/>\n",
				t2.toXml());
		assertEquals(
				"<text id=\"t3\" repeat=\"1\" begin=\"0.0\" end=\"0.0\" src=\"null\" region=\"r3\" fill=\"remove\"/>\n",
				t3.toXml());

		System.out.println("* ImageTest: test method 10 - toXmlCheck()");
		assertEquals(
				"<img id=\"i2\" repeat=\"5\" begin=\"2.0\" end=\"2.0\" src=\"source.extension\" region=\"r10\" fill=\"remove\"/>\n",
				i2.toXml());
		assertEquals(
				"<img id=\"i3\" repeat=\"1\" begin=\"0.0\" end=\"0.0\" src=\"null\" region=\"r6\" fill=\"remove\"/>\n",
				i3.toXml());

		System.out.println("* AudioTest: test method 10 - toXmlCheck()");
		assertEquals(
				"<audio id=\"a2\" repeat=\"5\" begin=\"2.0\" end=\"2.0\" src=\"source.extension\" clip-begin=\"2.0\" clip-end=\"2.0\"/>\n",
				a2.toXml());
		assertEquals(
				"<audio id=\"a3\" repeat=\"1\" begin=\"0.0\" end=\"0.0\" src=\"null\" clip-begin=\"0.0\" clip-end=\"0.0\"/>\n",
				a3.toXml());

		System.out.println("* VideoTest: test method 10 - toXmlCheck()");
		assertEquals(
				"<video id=\"v2\" repeat=\"5\" begin=\"2.0\" end=\"2.0\" src=\"source.extension\" region=\"r10\" fill=\"freeze\" clip-begin=\"2.0\" clip-end=\"2.0\"/>\n",
				v2.toXml());
		assertEquals(
				"<video id=\"v3\" repeat=\"2147483647\" begin=\"1.7976931348623157E308\" end=\"1.7976931348623157E308\" src=\"null\" region=\"r9\" fill=\"remove\" clip-begin=\"1.7976931348623157E308\" clip-end=\"1.7976931348623157E308\"/>\n",
				v3.toXml());

	}
}