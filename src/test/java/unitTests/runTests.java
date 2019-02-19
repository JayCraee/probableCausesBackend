package unitTests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class runTests {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(allQueryTests.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
    }
}