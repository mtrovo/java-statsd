package com.mtrovo.statsd;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class StatsdClient {
    private final Endpoint endpoint;
    public final Counter counter;
    public final Gauge gauge;
    
    public StatsdClient(String group, int port) {
        this(createUdpEndpoint(group, port));
    }

    public StatsdClient(Endpoint endpoint){
        this.endpoint = endpoint;
        this.counter = new Counter(endpoint);
        this.gauge = new Gauge(endpoint);
    }
    
    private static Endpoint createUdpEndpoint(String group, int port) {
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            socket.bind(new InetSocketAddress(group, port));
        } catch (SocketException e) {
            throw new StatsdException("Unable to startup statsd client", e);
        }
        
        Endpoint endpoint = new UdpEndpoint(socket);
        endpoint.setErrorHandler(new SyserrErrorHandler());
        return endpoint;
    }
}

class SyserrErrorHandler implements StatdErrorHandler {

    @Override
    public void handle(String msg, Throwable t) {
        System.err.println(String.format("Error while writing message to statsd server: %s", msg));
        t.printStackTrace(System.err);
    }
    
}

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