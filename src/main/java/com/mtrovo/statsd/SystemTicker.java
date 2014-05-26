package com.mtrovo.statsd;

public class SystemTicker implements Ticker {

    @Override
    public long current() {
        return System.currentTimeMillis();
    }

}
