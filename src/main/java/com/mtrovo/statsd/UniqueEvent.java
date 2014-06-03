package com.mtrovo.statsd;

/**
 * Created by mtrovo on 30/05/14.
 */
public class UniqueEvent {

    private final Endpoint endpoint;

    public UniqueEvent(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public void set(String bucket){
        this.endpoint.send(String.format("%s|s", bucket));
    }

    public BUniqueEvent build(String bucket) {
        return new BUniqueEvent(bucket);
    }

    class BUniqueEvent {
        public final String bucket;

        BUniqueEvent(String bucket) {
            this.bucket = bucket;
        }

        public void set() {
            UniqueEvent.this.set(bucket);
        }
    }
}
