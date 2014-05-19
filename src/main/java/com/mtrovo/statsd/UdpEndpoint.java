package com.mtrovo.statsd;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UdpEndpoint implements Endpoint {
    private final DatagramSocket serverSocket;    
    private StatdErrorHandler errorHandler; 


    private static DatagramSocket createSocket(String group, int port) {
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            socket.bind(new InetSocketAddress(group, port));
            return socket;
        } catch (SocketException e) {
            throw new StatsdException("Unable to startup statsd client", e);
        }
    }
    
    public UdpEndpoint(String group, int port) {
        this(createSocket(group, port));
    }
    
    public UdpEndpoint(DatagramSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /* (non-Javadoc)
     * @see com.mtrovo.statsd.Endpoint#send(java.lang.String)
     */
    @Override
    public void send(String msg) throws StatsdException{
        try {
            byte[] bytes = msg.getBytes();
            this.serverSocket.send(new DatagramPacket(bytes, bytes.length));
        } catch (Exception e) {
            errorHandler.handle(msg, e);
        }
    }

    /* (non-Javadoc)
     * @see com.mtrovo.statsd.Endpoint#getErrorHandler()
     */
    @Override
    public StatdErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /* (non-Javadoc)
     * @see com.mtrovo.statsd.Endpoint#setErrorHandler(com.mtrovo.statsd.StatdErrorHandler)
     */
    @Override
    public void setErrorHandler(StatdErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
}