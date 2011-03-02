package edu.nku.cs.csc440.team2.message;

import edu.nku.cs.csc440.team2.message.Sequence;
import edu.nku.cs.csc440.team2.message.Text;
import edu.nku.cs.csc440.team2.message.Region;
import edu.nku.cs.csc440.team2.message.Image;
import edu.nku.cs.csc440.team2.message.Parallel;
import edu.nku.cs.csc440.team2.message.Body;
import edu.nku.cs.csc440.team2.message.Audio;
import edu.nku.cs.csc440.team2.message.Video;
import java.util.TreeSet;
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
public class ComparableTest {

    static Region r1, r2;
    static Sequence s1, s2;
    static Parallel p1, p2;
    static Text t1, t2;
    static Image i1, i2;
    static Audio a1, a2;
    static Video v1, v2;

    public ComparableTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("* ComparableTest: test setUpClass - constructors");
        r1 = new Region();
        r2 = new Region();
        s1 = new Sequence();
        s2 = new Sequence();
        p1 = new Parallel();
        p2 = new Parallel();
        t1 = new Text(0.0, null, null);
        t2 = new Text(0.0, null, null);
        i1 = new Image(0.0, null, null);
        i2 = new Image(0.0, null, null);
        a1 = new Audio(0.0, null);
        a2 = new Audio(0.0, null);
        v1 = new Video(0.0, null, null);
        v2 = new Video(0.0, null, null);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void compareToCheck() {
        System.out.println("* RegionTest: test method 1 - compareToCheck()");
        TreeSet<Region> tsr = new TreeSet<Region>();
        tsr.add(r2);
        tsr.add(r1);
        assertEquals(r1, tsr.first());
        System.out.println("* MediaTest: test method 1 - compareToCheck()");
        TreeSet<Body> tsb = new TreeSet<Body>();
        tsb.add(v2);
        tsb.add(a2);
        tsb.add(i2);
        tsb.add(t2);
        tsb.add(p2);
        tsb.add(s2);
        tsb.add(v1);
        tsb.add(a1);
        tsb.add(i1);
        tsb.add(t1);
        tsb.add(p1);
        tsb.add(s1);
        assertEquals(a1, tsb.first());
        assertEquals(v2, tsb.last());

//        System.out.print("* ");
//        for (Body body : tsb) {
//            System.out.print(body.getId() + " ");
//        }
//        System.out.println("");
    }
}
