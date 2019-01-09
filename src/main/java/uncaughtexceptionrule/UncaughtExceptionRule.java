package uncaughtexceptionrule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * A rule which catches unhandled exceptions during unit test.
 *
 * By default during a unit test, unhandled exceptions that occur in working threads (i.e. all
 * threads except the main one) are missed and do not cause test failure. Even standard JUnit
 * assertion methods can not fail a test in a working thread. But usually you do not expect any
 * unhandled exception in any working thread, and you want the test to fail if it occurs. On the
 * other hand there are other situations in which you do expect a specific exception in some working
 * thread. Using this class, you can implement both.
 *
 * You should just define this line of code in your test class:
 * <pre>
 * {@literal @}Rule
 *  UncaughtExceptionRule rule = new UncaughtExceptionRule();
 * </pre>
 *
 * By default test will be failed if exception occurs. You can also override default behavior for a
 * specific test case. Where you expect an exception in a working thread (but not others), put the
 * line below at the bottom of that specific test case:
 * <pre>
 *  Assert.assertNotNull(rule.getException());
 *  Assert.assertTrue(rule.getException() instanceof ...);
 *  rule.clearException();  // Clears the exception to avoid failure on tear down.
 * </pre>
 *
 * Note that the cached exception from a previous test will be cleared before running new tests and
 * just fails that test and is no need to manually clear exception.
 */

public class UncaughtExceptionRule implements TestRule {

    private Throwable unhandledException = null;
    private Thread.UncaughtExceptionHandler oldHandler;

    @Override
    public Statement apply(Statement base, Description description) {
        oldHandler = Thread.getDefaultUncaughtExceptionHandler();

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                unhandledException = null;
                // Set default exception handler so that we can catch exception on all working
                // threads.
                Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
                    unhandledException = throwable;
                    // Let whatever that was going to happen to this exception, still happen.
                    if (oldHandler != null) {
                        oldHandler.uncaughtException(thread, throwable);
                    }
                });

                try {
                    // Run one test or all of tests of a test class depending on whether it is a
                    // @Rule or @ClassRule
                    base.evaluate();
                } finally {
                    // Throw the exception in the main thread of the test. So it cause test failure.
                    if (unhandledException != null) {
                        throw unhandledException;
                    }
                    // Set default exception handler to the old one.
                    Thread.setDefaultUncaughtExceptionHandler(oldHandler);
                }
            }
        };
    }

    public Throwable getException() {
        return unhandledException;
    }

    public void clearException() {
        this.unhandledException = null;
    }
}

