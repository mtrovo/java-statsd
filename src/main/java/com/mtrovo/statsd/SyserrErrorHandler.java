package com.mtrovo.statsd;

/**
 * Created by mtrovo on 28/05/14.
 */
class SyserrErrorHandler implements StatdErrorHandler {

    @Override
    public void handle(String msg, Throwable t) {
        System.err.println(String.format("Error while writing message to statsd server: %s", msg));
        t.printStackTrace(System.err);
    }

}
