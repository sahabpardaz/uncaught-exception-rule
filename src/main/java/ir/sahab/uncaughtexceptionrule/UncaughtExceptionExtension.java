package ir.sahab.uncaughtexceptionrule;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * A JUnit 5 extension for catching exceptions in threads other than the JUnit main test thread.
 * Note: you can't use this extension if you're running Concurrent tests.
 */
public class UncaughtExceptionExtension extends UncaughtExceptionBase implements BeforeTestExecutionCallback, AfterTestExecutionCallback {


    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        registerHandler();
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        afterHandle();
    }
}
