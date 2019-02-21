package integrationTests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import unitTests.AllUnitTests;

public class RunTests {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(AllUnitTests.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
    }
}