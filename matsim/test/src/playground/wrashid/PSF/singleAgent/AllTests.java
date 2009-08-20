package playground.wrashid.PSF.singleAgent;



import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests for " + AllTests.class.getPackage().getName());

		suite.addTestSuite(BasicTests.class);
		suite.addTestSuite(AdvancedTest1.class);
		suite.addTestSuite(AdvancedTest2.class);
		suite.addTestSuite(AdvancedTest3.class);
		suite.addTestSuite(AdvancedTest4.class);
		suite.addTestSuite(AdvancedTest5.class);

		return suite;
	}

}
           