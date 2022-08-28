package ir.sahab.uncaughtexceptionrule;

import java.util.List;
import java.util.stream.Collectors;

class AggregateException extends Exception {
    static final long serialVersionUID = -7188848759039620762L;
    private final List<Throwable> exceptions;

    public AggregateException(String message, List<Throwable> exceptions) {
        super(message);
        this.exceptions = exceptions;
    }

    @Override
    public String getMessage() {
        final String messages = exceptions.stream().map(Throwable::getMessage).collect(Collectors.joining(",\n\t", "List of Exception messages:\n\t", ""));
        return String.format("%s\n\t%s", super.getMessage(), messages);
    }

}
