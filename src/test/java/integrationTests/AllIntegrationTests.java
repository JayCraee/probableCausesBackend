package integrationTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import unitTests.EstimateTest;
import unitTests.InferTest;
import unitTests.SelectTest;
import unitTests.SimulateTest;

@RunWith(Suite.class)
@SuiteClasses({
		EstimateTest.class})

public class AllIntegrationTests {
}
