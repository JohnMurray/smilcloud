package edu.nku.cs.csc440.team2.message;

import edu.nku.cs.csc440.team2.message.Sequence;
import edu.nku.cs.csc440.team2.message.Text;
import edu.nku.cs.csc440.team2.message.Region;
import edu.nku.cs.csc440.team2.message.Image;
import edu.nku.cs.csc440.team2.message.Parallel;
import edu.nku.cs.csc440.team2.message.Message;
import edu.nku.cs.csc440.team2.message.Body;
import edu.nku.cs.csc440.team2.message.Audio;
import edu.nku.cs.csc440.team2.message.Video;
import edu.nku.cs.csc440.team2.message.Timing;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.awt.Dimension;
import java.io.File;
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
public class MessageTest {

    final static String BEFORE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<smil id=\"good_id\">\n"
            + "<head>\n"
            + "<layout>\n"
            + "<root-layout width=\"123\" height=\"456\" background-color=\"pink\"/>\n"
            + "<region id=\"r1\" width=\"480\" height=\"320\" background-color=\"transparent\" left=\"0\" top=\"0\" z-index=\"0\" fit=\"hidden\"/>\n"
            + "<region id=\"r2\" width=\"480\" height=\"320\" background-color=\"transparent\" left=\"0\" top=\"0\" z-index=\"0\" fit=\"hidden\"/>\n"
            + "<region id=\"r3\" width=\"480\" height=\"320\" background-color=\"transparent\" left=\"0\" top=\"0\" z-index=\"0\" fit=\"hidden\"/>\n"
            + "<region id=\"r4\" width=\"480\" height=\"320\" background-color=\"transparent\" left=\"0\" top=\"0\" z-index=\"0\" fit=\"hidden\"/>\n"
            + "<region id=\"r5\" width=\"480\" height=\"320\" background-color=\"transparent\" left=\"0\" top=\"0\" z-index=\"0\" fit=\"hidden\"/>\n"
            + "<region id=\"r6\" width=\"480\" height=\"320\" background-color=\"transparent\" left=\"0\" top=\"0\" z-index=\"0\" fit=\"hidden\"/>\n"
            + "</layout>\n"
            + "</head>\n"
            + "<body>\n"
            + "<text id=\"t2\" repeat=\"1\" begin=\"0.0\" end=\"2.0\" src=\"text2\" region=\"r5\" fill=\"remove\"/>\n"
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
            + "<video id=\"v2\" repeat=\"1\" begin=\"0.0\" end=\"2.0\" src=\"video2\" region=\"r4\" fill=\"remove\" clip-begin=\"0.0\" clip-end=\"2.0\"/>\n"
            + "</body>\n"
            + "</smil>";
    final static String AFTER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<smil id=\"good_id\">\n"
            + "<head>\n"
            + "<layout>\n"
            + "<root-layout width=\"123\" height=\"456\" background-color=\"pink\"/>\n"
            + "<region id=\"r3\" width=\"480\" height=\"320\" background-color=\"transparent\" left=\"0\" top=\"0\" z-index=\"0\" fit=\"hidden\"/>\n"
            + "<region id=\"r6\" width=\"480\" height=\"320\" background-color=\"transparent\" left=\"0\" top=\"0\" z-index=\"0\" fit=\"hidden\"/>\n"
            + "<region id=\"r7\" width=\"480\" height=\"320\" background-color=\"transparent\" left=\"0\" top=\"0\" z-index=\"0\" fit=\"hidden\"/>\n"
            + "<region id=\"r9\" width=\"480\" height=\"320\" background-color=\"transparent\" left=\"0\" top=\"0\" z-index=\"0\" fit=\"hidden\"/>\n"
            + "<region id=\"r11\" width=\"480\" height=\"320\" background-color=\"transparent\" left=\"0\" top=\"0\" z-index=\"0\" fit=\"hidden\"/>\n"
            + "</layout>\n"
            + "</head>\n"
            + "<body>\n"
            + "<video id=\"v4\" repeat=\"1\" begin=\"0.0\" end=\"0.0\" src=\"newVideo\" region=\"r9\" fill=\"remove\" clip-begin=\"0.0\" clip-end=\"0.0\"/>\n"
            + "<img id=\"i2\" repeat=\"1\" begin=\"0.0\" end=\"2.0\" src=\"image2\" region=\"r6\" fill=\"remove\"/>\n"
            + "<par id=\"p2\" repeat=\"1\" begin=\"1.0\">\n"
            + "<video id=\"v1\" repeat=\"1\" begin=\"0.0\" end=\"1.0\" src=\"video1\" region=\"r3\" fill=\"remove\" clip-begin=\"0.0\" clip-end=\"1.0\"/>\n"
            + "<seq id=\"s2\" repeat=\"1\" begin=\"1.0\">\n"
            + "</seq>\n"
            + "<text id=\"t3\" repeat=\"1\" begin=\"0.0\" end=\"0.0\" src=\"newText\" region=\"r7\" fill=\"remove\"/>\n"
            + "</par>\n"
            + "<audio id=\"a2\" repeat=\"1\" begin=\"0.0\" end=\"2.0\" src=\"audio2\" clip-begin=\"0.0\" clip-end=\"2.0\"/>\n"
            + "<audio id=\"a3\" repeat=\"1\" begin=\"0.0\" end=\"0.0\" src=\"newAudio\" clip-begin=\"0.0\" clip-end=\"0.0\"/>\n"
            + "<img id=\"i4\" repeat=\"1\" begin=\"0.0\" end=\"0.0\" src=\"newImage\" region=\"r11\" fill=\"remove\"/>\n"
            + "</body>\n"
            + "</smil>";
    static Message m1, m2, m3, m4;
    static Sequence s1, s2, s3;
    static Parallel p1, p2, p3;
    static Text t1, t2;
    static Image i1, i2;
    static Audio a1, a2;
    static Video v1, v2;
    LinkedList<String> l;
    File f;

    public MessageTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        m1 = new Message("good_id", new Dimension(123, 456), "pink");
        m2 = new Message("", null, "#9ACD32");
        m3 = new Message("bad id");
        System.out.println("* MessageTest: test setUpClass - create timing containers");
        s1 = new Sequence();
        p1 = new Parallel();
        s2 = new Sequence(1.0);
        p2 = new Parallel(1.0);
        s3 = new Sequence(2.0, 3.0);
        p3 = new Parallel(2.0, 3.0);
        System.out.println("* MessageTest: test setUpClass - create media elements");
        t1 = new Text(1.0, "text1", new Region());      // r1
        i1 = new Image(1.0, "image1", new Region());    // r2
        a1 = new Audio(1.0, "audio1");
        v1 = new Video(1.0, "video1", new Region());    // r3
        a2 = new Audio(2.0, "audio2");
        v2 = new Video(2.0, "video2", new Region());    // r4
        t2 = new Text(2.0, "text2", new Region());      // r5
        i2 = new Image(2.0, "image2", new Region());    // r6
        System.out.println("* MessageTest: test setUpClass - add elements to containers");
        s1.addElement(t1);
        p1.addElement(i1);
        s2.addElement(a1);
        p2.addElement(v1);
        System.out.println("* MessageTest: test setUpClass - add containers to containers");
        s1.addElement(p1);
        p2.addElement(s2);
        System.out.println("* MessageTest: test setUpClass - add elements and containers to implicit sequence");
        m1.addElement(t2);
        m1.addElement(i2);
        m1.addElement(s1);
        m1.addElement(p2);
        m1.addElement(a2);
        m1.addElement(v2);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        l = new LinkedList<String>();
    }

    @After
    public void tearDown() {
    }

//    @Test (expected=Exception.class)
//    public void constructorExceptionCheck() {
//        System.out.println("* MessageTest: test method 1 - constructorExceptionCheck()");
//            new Message(new File(""));
//    }
    @Test
    public void getIdCheck() {
        System.out.println("* MessageTest: test method 2 - getIdCheck()");
        assertEquals("good_id", m1.getId());
        assertEquals("", m2.getId());
        assertEquals(null, m3.getId());
    }

    @Test
    public void getRootLayoutCheck() {
        System.out.println("* MessageTest: test method 3 - getRootLayoutCheck()");
        assertEquals(new Dimension(123, 456), m1.getRootLayout().getDimensions());
        assertEquals("pink", m1.getRootLayout().getBackgroundColor());
        assertEquals(new Dimension(480, 320), m2.getRootLayout().getDimensions());
        assertEquals("#9ACD32", m2.getRootLayout().getBackgroundColor());
        assertEquals(new Dimension(480, 320), m3.getRootLayout().getDimensions());
        assertEquals("transparent", m3.getRootLayout().getBackgroundColor());
    }

    @Test
    public void getDownloadPriority() {
        System.out.println("* MessageTest: test method 4 - getDownloadPriority");
        assertEquals(null, m1.getDownloadPriority());
        assertEquals(null, m2.getDownloadPriority());
        assertEquals(null, m3.getDownloadPriority());
    }

    @Test
    public void getAllElementIdsCheck() {
        System.out.println("* MessageTest: test method 5 - getAllElementIdsCheck()");
        assertEquals("[a1, a2, i1, i2, p1, p2, s1, s2, t1, t2, v1, v2]", m1.getAllElementIds().toString());
    }

    @Test
    public void getBodyCheck() {
        System.out.println("* MessageTest: test method 6 - getBodyCheck()");
        for (Body body : m1.getBody()) {
            l.add(body.getId());
        }
        assertEquals("[t2, i2, s1, p2, a2, v2]", l.toString());
    }

    @Test
    public void toXmlCheck() {
        System.out.println("* MessageTest: test method 7 - toXmlCheck()");
        assertEquals(BEFORE, m1.toXml());
    }

    @Test
    public void getElementCheck() {
        System.out.println("* MessageTest: test method 8 - getElementCheck()");
        assertEquals("s1", m1.getElement("s1").getId());
        assertEquals("a1", m1.getElement("a1").getId());
        assertEquals("t2", m1.getElement("t2").getId());
        assertEquals("v2", m1.getElement("v2").getId());
    }

    @Test
    public void removeElementCheck() {
        System.out.println("* MessageTest: test method 9 - removeElementCheck()");
        m1.removeElement("t2");                                 // -r5
        m1.removeElement("s1");                                 // -r1, -r2
        m1.removeElement("a1");
        m1.removeElement("v2");                                 // -r4
        assertEquals(null, m1.getElement("s1"));
        assertEquals(null, m1.getElement("a1"));
        assertEquals(null, m1.getElement("t2"));
        assertEquals(null, m1.getElement("v2"));
    }

    @Test
    public void addElementCheck() {
        System.out.println("* MessageTest: test method 10 - addElementCheck()");
        m1.addElement(new Audio(0.0, "newAudio"));              // a3
        Timing t = (Timing) m1.getElement("p2");
        t.addElement(new Text(0.0, "newText", new Region()));   // t3, r7
        m1.addElement(new Video(0.0, null, null), -1);          // v3, r8
        m1.addElement(new Video(0.0, "newVideo", null), 0);     // v4, r9
        m1.addElement(new Image(0.0, null, null), 6);           // i3, r10
        m1.addElement(new Image(0.0, "newImage", null), 5);     // i4, r11
    }

    @Test
    public void afterCheck() {
        System.out.println("* MessageTest: test method 11 - afterCheck()");
        System.out.println("* \trepeat getAllElementIdsCheck()");
        assertEquals("[a2, a3, i2, i4, p2, s2, t3, v1, v4]", m1.getAllElementIds().toString());
        System.out.println("* \trepeat getBodyCheck()");
        for (Body body : m1.getBody()) {
            l.add(body.getId());
        }
        assertEquals("[v4, i2, p2, a2, a3, i4]", l.toString());
        System.out.println("* \trepeat toXmlCheck()");
        assertEquals(AFTER, m1.toXml());
    }

    @Test
    public void toFileCheck() throws FileNotFoundException {
        System.out.println("* MessageTest: test method 12 - toFileCheck()");
        //f = m1.toFile("temp.xml");
    }

    @Test
    public void ioCheck() throws Exception {
        System.out.println("* MessageTest: test method 13 - ioCheck()");
        f = m1.toFile("temp.smil");
        m4 = new Message(f);
        System.out.println("* \trepeat getIdCheck()");
        assertEquals("good_id", m4.getId());
        System.out.println("* \trepeat getRootLayoutCheck()");
        assertEquals(new Dimension(123, 456), m4.getRootLayout().getDimensions());
        assertEquals("pink", m4.getRootLayout().getBackgroundColor());
        System.out.println("* \trepeat getDownloadPriorityCheck()");
        assertEquals("[v4, v1, a2, a3]", m4.getDownloadPriority().toString());
        System.out.println("* \trepeat getAllElementIdsCheck()");
        assertEquals("[a2, a3, i2, i4, p2, s2, t3, v1, v4]", m4.getAllElementIds().toString());
        System.out.println("* \trepeat getBodyCheck()");
        for (Body body : m4.getBody()) {
            l.add(body.getId());
        }
        assertEquals("[v4, i2, p2, a2, a3, i4]", l.toString());
        System.out.println("* \trepeat toXmlCheck()");
        assertEquals(AFTER, m4.toXml());
    }
}
