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
        // Clear the exception for passing the running unit test
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

    // In case of exception in test thread and another thread we should show all exceptions
    @Test
    @Disabled
    void testFail() throws RuntimeException, InterruptedException {
        Thread t = new Thread(() -> {
            throw new ArithmeticException();
        });
        t.start();
        t.join();
        throw new IllegalStateException("Bad");
    }

}
