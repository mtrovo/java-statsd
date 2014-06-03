package com.mtrovo.statsd;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import com.mtrovo.statsd.Timing.BTiming;

public class StatsdClient {
    private final Endpoint endpoint;
    public final Counter counter;
    public final Gauge gauge;
    public final UniqueEvent event;
    
    public StatsdClient(String group, int port) {
        this(new UdpEndpoint(group, port));
    }

    public StatsdClient(Endpoint endpoint){
        this.endpoint = endpoint;
        this.counter = new Counter(endpoint);
        this.gauge = new Gauge(endpoint);
        this.event = new UniqueEvent(endpoint);
    }
}

