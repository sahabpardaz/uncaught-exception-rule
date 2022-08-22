package ir.sahab.uncaughtexceptionrule;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.lang.reflect.Method;

/**
 * A JUnit 5 extension for catching exceptions in threads other than the JUnit main test thread.
 * Note: you can't use this extension if you're running Concurrent tests.
 */
public class UncaughtExceptionExtension extends UncaughtExceptionBase implements InvocationInterceptor {

    @Override
    public void interceptTestMethod(
            Invocation<Void> invocation,
            ReflectiveInvocationContext<Method> invocationContext,
            ExtensionContext extensionContext
    ) throws Throwable {
        registerHandler();
        try {
            invocation.proceed();
        } catch (Throwable t) {
            setUnhandledException(t);
        } finally {
            afterHandle();
        }
    }
}
