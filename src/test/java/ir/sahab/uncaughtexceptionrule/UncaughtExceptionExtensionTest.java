package ir.sahab.uncaughtexceptionrule;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UncaughtExceptionExtensionTest {

    @RegisterExtension
    UncaughtExceptionExtension uncaughtExceptionExtension = new UncaughtExceptionExtension();

    @Test
    void testAssertForUnhandledExceptions() throws InterruptedException {
        Thread t = new Thread(() -> {
            throw new ArithmeticException();
        });
        t.start();
        t.join();

        assertNotNull(uncaughtExceptionExtension.getException());
        assertTrue(uncaughtExceptionExtension.getException() instanceof ArithmeticException);
        // clear the exception for test passes.
        uncaughtExceptionExtension.clearException();
    }

    /*
     * This test will fail to show that our extension can catch exceptions from other threads.
     */
    @Test
    @Disabled
    void testFailureForUnhandledException() throws InterruptedException {
        Thread t = new Thread(() -> {
            throw new ArithmeticException();
        });
        t.start();
        t.join();
    }

}
