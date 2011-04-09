package edu.nku.cs.csc440.team2.message;

import com.example.helloandroid.HelloAndroid;
import android.test.ActivityInstrumentationTestCase2;

public class CdataValidatorTest extends
		ActivityInstrumentationTestCase2<HelloAndroid> {

	static CdataValidator v;

	public CdataValidatorTest() {
		super("com.example.helloandroid", HelloAndroid.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		System.out
				.println("* CdataValidatorTest: test setUpClass - constructors");
		v = new CdataValidator();
	}

	public void testAll() {

		// validateCheck
		System.out
				.println("* CdataValidatorTest: test method 1 - validateCheck()");
		assertEquals(null, v.validate("#pound", ""));
		assertEquals("#pound", v.validate("#pound", "#"));
		assertEquals(null, v.validate("%percent", ""));
		assertEquals("%percent", v.validate("%percent", "%"));
		assertEquals(null, v.validate(" space", ""));
		assertEquals(null, v.validate(null, null));
	}
}