package com.mtrovo.statsd;

/**
 * Created by mtrovo on 28/05/14.
 */
class Gauge {
    private final Endpoint endpoint;

    public Gauge(Endpoint endpoint) {
        super();
        this.endpoint = endpoint;
    }

    public void increment(String bucket, long delta) {
        if(delta < 0) throw new IllegalArgumentException("Only positive integer values allowed");
        sendDelta(bucket, delta);
    }

    public void decrement(String bucket, long delta) {
        if(delta < 0) throw new IllegalArgumentException("Only positive integer values allowed");
        sendDelta(bucket, -delta);
    }

    public void sendDelta(String bucket, long delta) {
        this.endpoint.send(String.format("%s:%+d|g", bucket, delta));
    }

    public void set(String bucket, long val){
        // disambiguation of negative values message
        if(val < 0) {
            set(bucket, 0);
        }
        this.endpoint.send(String.format("%s:%d|g", bucket, val));
    }

    public BGauge build(String bucket) {
        return new BGauge(bucket);
    }

    class BGauge {
        public final String bucket;
        private final Gauge gauge;

        public BGauge(String bucket) {
            this.bucket = bucket;
            this.gauge = Gauge.this;
        }

        public void increment(long delta) {
            gauge.increment(bucket, delta);
        }

        public void decrement(long delta) {
            gauge.decrement(bucket, delta);
        }

        public void sendDelta(long delta) {
            gauge.sendDelta(bucket, delta);
        }

        public void set(long val) {
            gauge.set(bucket, val);
        }
    }
}
