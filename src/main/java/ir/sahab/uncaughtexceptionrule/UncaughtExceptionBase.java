package ir.sahab.uncaughtexceptionrule;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Base class for handling uncaught exceptions raised from other threads in JUnit tests.
 */
abstract class UncaughtExceptionBase {
    private final AtomicReference<Throwable> unhandledException = new AtomicReference<>();
    private Thread.UncaughtExceptionHandler oldHandler;

    protected void setUnhandledException(Throwable t) {
        final Throwable previous = unhandledException.getAndSet(t);
        if (previous != null) {
            previous.printStackTrace();
        }
    }

    public Throwable getException() {
        return unhandledException.get();
    }

    public void clearException() {
        unhandledException.set(null);
    }

    protected void registerHandler() {
        clearException();
        oldHandler = Thread.getDefaultUncaughtExceptionHandler();
        // Set default exception handler so that we can catch exception on all working
        // threads.
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            setUnhandledException(throwable);
            // Let whatever that was going to happen to this exception, still happen.
            if (oldHandler != null) {
                oldHandler.uncaughtException(thread, throwable);
            }
        });
    }

    protected void afterHandle() throws Throwable {
        Thread.setDefaultUncaughtExceptionHandler(oldHandler);
        if (unhandledException.get() != null) {
            throw unhandledException.get();
        }
    }
}
