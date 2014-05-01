package com.mtrovo.statsd;

public class StatsdException extends RuntimeException {

    public StatsdException() {
        super();
    }

    public StatsdException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatsdException(String message) {
        super(message);
    }

    public StatsdException(Throwable cause) {
        super(cause);
    }

}
