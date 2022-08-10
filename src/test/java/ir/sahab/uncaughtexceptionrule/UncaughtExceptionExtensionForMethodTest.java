package ir.sahab.uncaughtexceptionrule;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UncaughtExceptionExtension.class)
class UncaughtExceptionExtensionForMethodTest {

    // It is a test to test failure of test. So it is disabled.
    // If you are in doubt, you can enable it!
    @Test
    @ExtendWith(UncaughtExceptionExtension.class)
    @Disabled
    public void testFailureForUnhandledException() throws InterruptedException {
        Thread t = new Thread(() -> {
            throw new ArithmeticException();
        });
        t.start();
        t.join();
    }

}
