package unitTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		EstimateTest.class,
        SimulateTest.class,
		InferTest.class}) // TODO add: FindOutliersTest, RepairTest

public class AllQueryTests {
}
