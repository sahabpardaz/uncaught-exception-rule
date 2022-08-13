package ir.sahab.uncaughtexceptionrule;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * A JUnit 5 extension for catching exceptions in threads other than the JUnit main test thread.
 * Note: you can't use this extension if you're running Concurrent tests.
 */
public class UncaughtExceptionExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private Throwable unhandledException = null;
    private Thread.UncaughtExceptionHandler oldHandler;

    public Throwable getException() {
        return unhandledException;
    }

    public void clearException() {
        unhandledException = null;
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        oldHandler = Thread.getDefaultUncaughtExceptionHandler();
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
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(oldHandler);

        if (unhandledException != null) {
            throw new Exception("There was an exception in other thread. There is no Error, your test Failed", unhandledException);
        }
    }
}
