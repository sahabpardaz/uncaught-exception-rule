package ir.sahab.uncaughtexceptionrule;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

public class UncaughtExceptionRuleTest {

    @Rule
    public UncaughtExceptionRule rule = new UncaughtExceptionRule();

    @Test
    public void testAssertOnUnhandledException() throws InterruptedException {
        Thread t = new Thread(() -> {
            throw new ArithmeticException();
        });
        t.start();
        t.join();

        Assert.assertFalse(rule.getExceptions().isEmpty());
        Assert.assertTrue(rule.getExceptions().get(0) instanceof ArithmeticException);
        // Clear the exception for passing the running unit test
        rule.clearException();
    }

    // It is a test to test failure of test. So it is ignored.
    // If you are in doubt, you can delete the @Ignore annotation!
    @Ignore
    @Test
    public void testFailureForUnhandledException() throws InterruptedException {
        Thread t = new Thread(() -> {
            throw new ArithmeticException();
        });
        t.start();
        t.join();
    }

    // In case of exception in test thread and another thread we should show all exceptions
    @Ignore
    @Test
    public void testFail() throws RuntimeException, InterruptedException {
        Thread t = new Thread(() -> {
            throw new IllegalStateException("Exception occurred in another thread");
        });
        t.start();
        t.join();
        throw new IllegalStateException("Exception occurred in unit test's thread");
    }

}
