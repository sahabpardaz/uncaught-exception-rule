package ir.sahab.uncaughtexceptionrule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for handling uncaught exceptions raised from other threads in JUnit tests.
 */
abstract class UncaughtExceptionBase {

    private final List<Throwable> unhandledExceptions = Collections.synchronizedList(new ArrayList<>());

    private Thread.UncaughtExceptionHandler oldHandler;

    protected void gotUnhandledException(Throwable t) {
        unhandledExceptions.add(t);
    }

    public List<Throwable> getExceptions() {
        return Collections.unmodifiableList(unhandledExceptions);
    }

    public void clearException() {
        unhandledExceptions.clear();
    }

    protected void registerHandler() {
        clearException();
        oldHandler = Thread.getDefaultUncaughtExceptionHandler();
        // Set default exception handler so that we can catch exception on all working
        // threads.
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            gotUnhandledException(throwable);
            // Let whatever that was going to happen to this exception, still happen.
            if (oldHandler != null) {
                oldHandler.uncaughtException(thread, throwable);
            }
        });
    }

    protected void afterHandle() throws Throwable {
        Thread.setDefaultUncaughtExceptionHandler(oldHandler);
        if (!unhandledExceptions.isEmpty()) {
            if (unhandledExceptions.size() > 1) {
                throw new UncaughtException("Got multiple concurrent exceptions", getExceptions());
            } else {
                throw unhandledExceptions.get(0);
            }
        }
    }
}
