package ir.sahab.uncaughtexceptionrule;

import org.junit.Assert;
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

        Assert.assertNotNull(rule.getException());
        Assert.assertTrue(rule.getException() instanceof ArithmeticException);
        rule.clearException();
    }

    // It is a test to test failure of test. So it is commented.
    // If you are in doubt, you can uncomment it!
    // @Test
    public void testFailureForUnhandledException() throws InterruptedException {
        Thread t = new Thread(() -> {
            throw new ArithmeticException();
        });
        t.start();
        t.join();
    }
}
