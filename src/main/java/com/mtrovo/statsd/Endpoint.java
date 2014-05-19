package com.mtrovo.statsd;



public interface Endpoint {

    public abstract void send(String msg) throws StatsdException;

    public abstract StatdErrorHandler getErrorHandler();

    public abstract void setErrorHandler(StatdErrorHandler errorHandler);

}
