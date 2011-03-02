package edu.nku.cs.csc440.team2.message;

import edu.nku.cs.csc440.team2.message.CdataValidator;
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
public class CdataValidatorTest {

    static CdataValidator v;

    public CdataValidatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("* CdataValidatorTest: test setUpClass - constructors");
        v = new CdataValidator();
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
    public void validateCheck() {
        System.out.println("* CdataValidatorTest: test method 1 - validateCheck()");
        assertEquals(null, v.validate("#pound", ""));
        assertEquals("#pound", v.validate("#pound", "#"));
        assertEquals(null, v.validate("%percent", ""));
        assertEquals("%percent", v.validate("%percent", "%"));
        assertEquals(null, v.validate(" space", ""));
        assertEquals(null, v.validate(null, null));
    }
}
