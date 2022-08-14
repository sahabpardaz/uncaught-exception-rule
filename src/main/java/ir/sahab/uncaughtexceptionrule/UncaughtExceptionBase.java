package ir.sahab.uncaughtexceptionrule;

abstract class UncaughtExceptionBase {
    private Throwable unhandledException = null;

    private Thread.UncaughtExceptionHandler oldHandler;

    public Throwable getException() {
        return unhandledException;
    }

    public void clearException() {
        unhandledException = null;
    }

    protected void registerHandler() {
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

    protected void afterHandle() throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(oldHandler);
        if (unhandledException != null) {
            throw new Exception("There was an exception in other thread. There is no Error, your test Failed", unhandledException);
        }
    }
}
