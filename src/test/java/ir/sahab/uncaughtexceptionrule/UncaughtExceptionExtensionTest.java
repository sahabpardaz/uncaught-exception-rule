package ir.sahab.uncaughtexceptionrule;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(UncaughtExceptionExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UncaughtExceptionExtensionTest {

    @Test
    @Order(1)
    public void testAAShouldPasse() {
        assertTrue(true);
    }

    // It is a test to test failure of test. So it is disabled.
    // If you are in doubt, you can enable it!
    @Test
    @Order(2)
    @Disabled
    public void testFailureForUnhandledException() throws InterruptedException {
        Thread t = new Thread(() -> {
            throw new ArithmeticException();
        });
        t.start();
        t.join();
    }

    @Test
    @Order(3)
    @Disabled
    public void thisThrowsExceptionInMainThread() {
        throw new ArithmeticException();
    }

    @Test
    @Order(4)
    @Disabled
    public void thisTestFails() {
        fail("Just failed");
    }

    @Test
    @Order(5)
    public void thisTestExpectsAnException() {
        assertThrows(ArithmeticException.class, () -> {
            throw new ArithmeticException();
        });
    }

}
