package JunitTests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class TestRunner {

	static String pathToCsv="data/kd.csv";
	public static void main(String[] args) {
		Result result=JUnitCore.runClasses(WekaTest.class);
		for (Failure failure : result.getFailures()) {
		      System.out.println(failure.toString());
		    }
		System.out.println("WekaTest successful :" + result.wasSuccessful());

		result=JUnitCore.runClasses(DiagramTest.class);
		for (Failure failure : result.getFailures()) {
		      System.out.println(failure.toString());
		    }
		System.out.println("DiagrammCreate successful :" + result.wasSuccessful());

	}

}
