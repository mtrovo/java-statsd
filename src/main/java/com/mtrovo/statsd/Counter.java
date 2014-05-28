package com.mtrovo.statsd;

/**
 * Created by mtrovo on 28/05/14.
 */
class Counter {

    private final Endpoint endpoint;

    public Counter(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public void increment(String bucket) {
        increment(bucket, 1);
    }

    public void increment(String bucket, long diff) {
        if(diff < 0) throw new IllegalArgumentException("Only positive integer values allowed");
        send(bucket, diff);
    }

    public void decrement(String bucket) {
        send(bucket, -1);
    }

    public void decrement(String bucket, long diff) {
        if(diff < 0) throw new IllegalArgumentException("Only positive integer values allowed");
        send(bucket, -diff);
    }

    public void send(String bucket, long diff) {
        endpoint.send(String.format("%s:%d|c", bucket, diff));
    }

    public BCounter build(String bucket) {
        return new BCounter(bucket);
    }

    class BCounter {
        public final String bucket;
        private final Counter counter;

        public BCounter(String bucket) {
            super();
            this.bucket = bucket;
            this.counter = Counter.this;
        }

        public void increment() {
            counter.increment(bucket);
        }

        public void increment(long diff) {
            counter.increment(bucket, diff);
        }

        public void decrement() {
            counter.decrement(bucket);
        }

        public void decrement(long diff) {
            counter.decrement(bucket, diff);
        }

        public void send(long diff) {
            counter.send(bucket, diff);
        }

    }
}
