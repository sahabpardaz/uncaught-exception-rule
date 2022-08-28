package ir.sahab.uncaughtexceptionrule;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper around all exceptions that are occurred during unit test execution.
 *
 * @see UncaughtExceptionBase#afterHandle()
 */
class UncaughtException extends Exception {
    static final long serialVersionUID = -7188848759039620762L;
    private final List<Throwable> exceptions;

    public UncaughtException(String message, List<Throwable> exceptions) {
        super(message);
        this.exceptions = exceptions;
    }

    public List<Throwable> getExceptions() {
        return exceptions;
    }

    @Override
    public String getMessage() {
        final String messages = exceptions.stream()
                .map(Throwable::getMessage)
                .collect(Collectors.joining(",\n\t", "List of Exception messages:\n\t", ""));
        return String.format("%s%n\t%s", super.getMessage(), messages);
    }

}
