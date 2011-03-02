package edu.nku.cs.csc440.team2.message;

import edu.nku.cs.csc440.team2.message.RootLayout;
import edu.nku.cs.csc440.team2.message.Region;
import java.awt.Dimension;
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
public class LayoutTest {

    static Region r2, r3, r4;
    static RootLayout l1, l2, l3;

    public LayoutTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("* RegionTest: test setUpClass - constructors");
        new Region();
        r2 = new Region();
        r3 = new Region(null);
        r4 = new Region(new Dimension(100, 100), "dark red", new Dimension(25, 25), "filled");

        System.out.println("* RootLayoutTest: test setUpClass - constructors");
        l1 = new RootLayout();
        l2 = new RootLayout(null);
        l3 = new RootLayout(new Dimension(100, 100), "#9ACD32");
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
    public void backgroundColorCheck() {
        System.out.println("* LayoutTest: test method 1 - backgroundColorCheck()");
        assertEquals(RootLayout.TRANSPARENT, r4.getBackgroundColor());
        assertEquals("#9ACD32", l3.getBackgroundColor());
    }

    @Test
    public void dimensionsCheck() {
        System.out.println("* LayoutTest: test method 2 - dimensionsCheck()");
        assertEquals(new Dimension(480, 320), r2.getDimensions());
        assertEquals(new Dimension(100, 100), l3.getDimensions());
    }

    @Test
    public void idCheck() {
        System.out.println("* RegionTest: test method 3 - idCheck()");
        assertEquals("r2", r2.getId());
        assertEquals("r3", r3.getId());
        assertEquals("r4", r4.getId());
    }

    @Test
    public void originCheck() {
        System.out.println("* RegionTest: test method 4 - originCheck()");
        assertEquals(new Dimension(0, 0), r2.getOrigin());
        r2.setOrigin(new Dimension(16, 17));
        r2.setOrigin(null);
        assertEquals(new Dimension(16, 17), r2.getOrigin());
    }

    @Test
    public void zIndexCheck() {
        System.out.println("* RegionTest: test method 5 - zIndexCheck()");
        assertEquals(0, r2.getZindex());
        r2.setZindex(Integer.MAX_VALUE);
        r3.setZindex(Integer.MIN_VALUE);
        assertEquals(Integer.MAX_VALUE, r2.getZindex());
        assertEquals(Integer.MIN_VALUE, r3.getZindex());
    }

    @Test
    public void fitCheck() {
        System.out.println("* RegionTest: test method 6 - fitCheck()");
        assertEquals(Region.HIDDEN, r4.getFit());
        r4.setFit(Region.SLICE);
        r4.setFit(null);
        r4.setFit("wrong fit");
        assertEquals(Region.SLICE, r4.getFit());
    }

    @Test
    public void toXmlCheck() {
        System.out.println("* RegionTest: test method 7 - toXmlCheck()");
        assertEquals("<region id=\"r2\" width=\"480\" height=\"320\" background-color=\"transparent\" left=\"16\" top=\"17\" z-index=\"2147483647\" fit=\"hidden\"/>\n", r2.toXml());
        assertEquals("<region id=\"r3\" width=\"480\" height=\"320\" background-color=\"transparent\" left=\"0\" top=\"0\" z-index=\"-2147483648\" fit=\"hidden\"/>\n", r3.toXml());
        assertEquals("<region id=\"r4\" width=\"100\" height=\"100\" background-color=\"transparent\" left=\"25\" top=\"25\" z-index=\"0\" fit=\"slice\"/>\n", r4.toXml());
        assertEquals("<root-layout width=\"480\" height=\"320\" background-color=\"transparent\"/>\n", l1.toXml());
        assertEquals("<root-layout width=\"480\" height=\"320\" background-color=\"transparent\"/>\n", l2.toXml());
        assertEquals("<root-layout width=\"100\" height=\"100\" background-color=\"#9ACD32\"/>\n", l3.toXml());
    }
}
