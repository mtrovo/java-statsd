package com.mtrovo.statsd;

public interface StatdErrorHandler {
    public void handle(String msg, Throwable t);
}
