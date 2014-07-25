package com.mtrovo.statsd;

/**
 * Created by mtrovo on 28/05/14.
 */
class Timing {
    private final Endpoint endpoint;
    private Ticker ticker = new SystemTicker();

    public Timing(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public Context time(String bucket) {
        return new Context(bucket, ticker.current());
    }
    
    public void time(String bucket, Runnable runnable){
        long ini = ticker.current();
        runnable.run();
        this.sendTimeMs(bucket, ticker.current() - ini);
    }

    public void sendTimeMs(String bucket, long ms) {
        endpoint.send(String.format("%s:%d|ms", bucket, ms));
    }

    public BTiming build(String bucket) {
        return new BTiming(bucket);
    }

    public void setTicker(Ticker ticker){
        this.ticker = ticker;
    }

    class BTiming {
        public final String bucket;

        public BTiming(String bucket) {
            super();
            this.bucket = bucket;
        }

        public Context time() {
            return Timing.this.time(bucket);
        }
        
        public void time(Runnable runnable) {
        	Timing.this.time(bucket, runnable);
        }

        public void sendTimeMs(long ms) {
            Timing.this.sendTimeMs(bucket, ms);
        }
    }

    class Context implements AutoCloseable {

        private final String bucket;
        private final long ini;

        public Context(String bucket, long ini) {
            super();
            this.bucket = bucket;
            this.ini = ini;
        }

        @Override
        public void close() {
            sendTimeMs(bucket, ticker.current() - ini);
        }

    }
}
