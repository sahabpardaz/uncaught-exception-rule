package ir.sahab.uncaughtexceptionrule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * A Junit 4 {@link TestRule} which catches unhandled exceptions during unit test.
 * By default during a unit test, unhandled exceptions that occur in working threads (i.e. all
 * threads except the main one) are missed and do not cause test failure. Even standard JUnit
 * assertion methods can not fail a test in a working thread. But usually you do not expect any
 * unhandled exception in any working thread, and you want the test to fail if it occurs. On the
 * other hand there are other situations in which you do expect a specific exception in some working
 * thread. Using this class, you can implement both.
 * You should just define this line of code in your test class:
 * <pre>
 * {@literal @}Rule
 *  UncaughtExceptionRule rule = new UncaughtExceptionRule();
 * </pre>
 * <p>
 * By default test will be failed if exception occurs. You can also override default behavior for a
 * specific test case. Where you expect an exception in a working thread (but not others), put the
 * line below at the bottom of that specific test case:
 * <pre>
 *  Assert.assertNotNull(rule.getException());
 *  Assert.assertTrue(rule.getException() instanceof ...);
 *  rule.clearException();  // Clears the exception to avoid failure on tear down.
 * </pre>
 * <p>
 * Note that the cached exception from a previous test will be cleared before running new tests and
 * just fails that test and is no need to manually clear exception.
 */

public class UncaughtExceptionRule extends UncaughtExceptionBase implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                registerHandler();
                try {
                    // Run one test or all tests of a test class depending on whether it is a
                    // @Rule or @ClassRule
                    base.evaluate();
                } catch (Throwable t) {
                    setUnhandledException(t);
                } finally {
                    afterHandle();
                }
            }
        };
    }

}

