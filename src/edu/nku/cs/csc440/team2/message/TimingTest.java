package edu.nku.cs.csc440.team2.message;

import edu.nku.cs.csc440.team2.message.Sequence;
import edu.nku.cs.csc440.team2.message.Text;
import edu.nku.cs.csc440.team2.message.Region;
import edu.nku.cs.csc440.team2.message.Image;
import edu.nku.cs.csc440.team2.message.Parallel;
import edu.nku.cs.csc440.team2.message.Body;
import edu.nku.cs.csc440.team2.message.Audio;
import edu.nku.cs.csc440.team2.message.Video;
import edu.nku.cs.csc440.team2.message.Timing;
import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Shane Crandall
 * @version 0
 */
public class TimingTest {

    final static String BEFORE = "<text id=\"t2\" repeat=\"1\" begin=\"0.0\" end=\"2.0\" src=\"text2\" region=\"r5\" fill=\"remove\"/>\n"
            + "<img id=\"i2\" repeat=\"1\" begin=\"0.0\" end=\"2.0\" src=\"image2\" region=\"r6\" fill=\"remove\"/>\n"
            + "<seq id=\"s1\" repeat=\"1\" begin=\"0.0\">\n"
            + "<text id=\"t1\" repeat=\"1\" begin=\"0.0\" end=\"1.0\" src=\"text1\" region=\"r1\" fill=\"remove\"/>\n"
            + "<par id=\"p1\" repeat=\"1\" begin=\"0.0\">\n"
            + "<img id=\"i1\" repeat=\"1\" begin=\"0.0\" end=\"1.0\" src=\"image1\" region=\"r2\" fill=\"remove\"/>\n"
            + "</par>\n"
            + "</seq>\n"
            + "<par id=\"p2\" repeat=\"1\" begin=\"1.0\">\n"
            + "<video id=\"v1\" repeat=\"1\" begin=\"0.0\" end=\"1.0\" src=\"video1\" region=\"r3\" fill=\"remove\" clip-begin=\"0.0\" clip-end=\"1.0\"/>\n"
            + "<seq id=\"s2\" repeat=\"1\" begin=\"1.0\">\n"
            + "<audio id=\"a1\" repeat=\"1\" begin=\"0.0\" end=\"1.0\" src=\"audio1\" clip-begin=\"0.0\" clip-end=\"1.0\"/>\n"
            + "</seq>\n"
            + "</par>\n"
            + "<audio id=\"a2\" repeat=\"1\" begin=\"0.0\" end=\"2.0\" src=\"audio2\" clip-begin=\"0.0\" clip-end=\"2.0\"/>\n"
            + "<video id=\"v2\" repeat=\"1\" begin=\"0.0\" end=\"2.0\" src=\"video2\" region=\"r4\" fill=\"remove\" clip-begin=\"0.0\" clip-end=\"2.0\"/>\n";
    final static String AFTER = "<video id=\"v4\" repeat=\"1\" begin=\"0.0\" end=\"0.0\" src=\"newVideo\" region=\"r9\" fill=\"remove\" clip-begin=\"0.0\" clip-end=\"0.0\"/>\n"
            + "<img id=\"i2\" repeat=\"1\" begin=\"0.0\" end=\"2.0\" src=\"image2\" region=\"r6\" fill=\"remove\"/>\n"
            + "<par id=\"p2\" repeat=\"1\" begin=\"1.0\">\n"
            + "<video id=\"v1\" repeat=\"1\" begin=\"0.0\" end=\"1.0\" src=\"video1\" region=\"r3\" fill=\"remove\" clip-begin=\"0.0\" clip-end=\"1.0\"/>\n"
            + "<seq id=\"s2\" repeat=\"1\" begin=\"1.0\">\n"
            + "</seq>\n"
            + "<text id=\"t3\" repeat=\"1\" begin=\"0.0\" end=\"0.0\" src=\"newText\" region=\"r7\" fill=\"remove\"/>\n"
            + "</par>\n"
            + "<audio id=\"a2\" repeat=\"1\" begin=\"0.0\" end=\"2.0\" src=\"audio2\" clip-begin=\"0.0\" clip-end=\"2.0\"/>\n"
            + "<audio id=\"a3\" repeat=\"1\" begin=\"0.0\" end=\"0.0\" src=\"newAudio\" clip-begin=\"0.0\" clip-end=\"0.0\"/>\n"
            + "<img id=\"i4\" repeat=\"1\" begin=\"0.0\" end=\"0.0\" src=\"newImage\" region=\"r11\" fill=\"remove\"/>\n";
    static Sequence s, s1, s2, s3;
    static Parallel p1, p2, p3;
    static Text t1, t2;
    static Image i1, i2;
    static Audio a1, a2;
    static Video v1, v2;
    LinkedList<String> l;

    public TimingTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        System.out.println("* TimingTest: test setUpClass - create timing containers");
        s = new Sequence(new LinkedList<Body>(), true);         // implicit sequence
        s1 = new Sequence();
        p1 = new Parallel();
        s2 = new Sequence(1.0);
        p2 = new Parallel(1.0);
        s3 = new Sequence(2.0, 3.0);
        p3 = new Parallel(2.0, 3.0);
        System.out.println("* TimingTest: test setUpClass - create media elements");
        t1 = new Text(1.0, "text1", new Region());              // r1
        i1 = new Image(1.0, "image1", new Region());            // r2
        a1 = new Audio(1.0, "audio1");
        v1 = new Video(1.0, "video1", new Region());            // r3
        a2 = new Audio(2.0, "audio2");
        v2 = new Video(2.0, "video2", new Region());            // r4
        t2 = new Text(2.0, "text2", new Region());              // r5
        i2 = new Image(2.0, "image2", new Region());            // r6
        System.out.println("* TimingTest: test setUpClass - add elements to containers");
        s1.addElement(t1);
        p1.addElement(i1);
        s2.addElement(a1);
        p2.addElement(v1);
        System.out.println("* TimingTest: test setUpClass - add containers to containers");
        s1.addElement(p1);
        p2.addElement(s2);
        System.out.println("* TimingTest: test setUpClass - add elements and containers to implicit sequence");
        s.addElement(t2);
        s.addElement(i2);
        s.addElement(s1);
        s.addElement(p2);
        s.addElement(a2);
        s.addElement(v2);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        l = new LinkedList<String>();
    }

    @After
    public void tearDown() {
    }

    @Test //TreeSet<String> getAllElementIds() {
    public void getAllElementIdsCheck() {
        System.out.println("* TimingTest: test method 1 - getAllElementIdsCheck()");
        assertEquals("[a1, a2, i1, i2, p1, p2, s1, s2, t1, t2, v1, v2]", s.getAllElementIds().toString());
    }

    @Test //public LinkedList<Body> getBody() {
    public void getBodyCheck() {
        System.out.println("* TimingTest: test method 2 - getBodyCheck()");
        for (Body body : s.getBody()) {
            l.add(body.getId());
        }
        assertEquals("[t2, i2, s1, p2, a2, v2]", l.toString());
    }

    @Test //TreeSet<Region> getRegions() {
    public void getRegionsCheck() {
        System.out.println("* TimingTest: test method 3 - getRegionsCheck()");
        for (Region region : s.getRegions()) {
            l.add(region.getId());
        }
        assertEquals("[r1, r2, r3, r4, r5, r6]", l.toString());
    }

    @Test //String toXml() {
    public void toXmlCheck() {
        System.out.println("* TimingTest: test method 4 - toXmlCheck()");
        assertEquals(BEFORE, s.toXml());
    }

    @Test //Body getElement(String id, boolean isRemoving) {
    public void getElementCheck() {
        System.out.println("* TimingTest: test method 5 - getElementCheck()");
        assertEquals("s1", s.getElement("s1", false).getId());
        assertEquals("a1", s.getElement("a1", false).getId());
        assertEquals("t2", s.getElement("t2", false).getId());
        assertEquals("v2", s.getElement("v2", false).getId());
    }

    @Test //void removeElement(String id) {
    public void removeElementCheck() {
        System.out.println("* TimingTest: test method 6 - removeElementCheck()");
        s.removeElement("t2");      // -r5
        s.removeElement("s1");      // -r1, -r2
        s.removeElement("a1");
        s.removeElement("v2");      // -r4
        assertEquals(null, s.getElement("s1", false));
        assertEquals(null, s.getElement("a1", false));
        assertEquals(null, s.getElement("t2", false));
        assertEquals(null, s.getElement("v2", false));
    }

    @Test //public String addElement(Body element) {
    public void addElementCheck() {
        System.out.println("* TimingTest: test method 7 - addElementCheck()");
        s.addElement(new Audio(0.0, "newAudio"));               // a3
        Timing t = (Timing) s.getElement("p2", false);
        t.addElement(new Text(0.0, "newText", new Region()));   // t3, r7
        s.addElement(new Video(0.0, null, null), -1);           // v3, r8
        s.addElement(new Video(0.0, "newVideo", null), 0);      // v4, r9
        s.addElement(new Image(0.0, null, null), 6);            // i3, r10
        s.addElement(new Image(0.0, "newImage", null), 5);      // i4, r11
    }

    @Test
    public void afterCheck() {
        System.out.println("* TimingTest: test method 8 - afterCheck()");
        System.out.println("* \trepeat getAllElementIdsCheck()");
        assertEquals("[a2, a3, i2, i4, p2, s2, t3, v1, v4]", s.getAllElementIds().toString());
        System.out.println("* \trepeat getBodyCheck()");
        for (Body body : s.getBody()) {
            l.add(body.getId());
        }
        assertEquals("[v4, i2, p2, a2, a3, i4]", l.toString());
        setUp();
        System.out.println("* \trepeat getRegionsCheck()");
        for (Region region : s.getRegions()) {
            l.add(region.getId());
        }
        assertEquals("[r3, r6, r7, r9, r11]", l.toString());
        System.out.println("* \trepeat toXmlCheck()");
        assertEquals(AFTER, s.toXml());
    }

    @Test
    public void loopCheck() {
        System.out.println("* TimingTest: test method 9 - loopCheck()");
        s.addElement(p3);
        p3.addElement(s3);
        s3.addElement(s);
        s.getAllElementIds();
        s3.getAllElementIds();
        p3.getAllElementIds();
    }
}
