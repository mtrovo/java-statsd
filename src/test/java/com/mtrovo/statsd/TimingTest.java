package com.mtrovo.statsd;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.mtrovo.statsd.Timing.BTiming;
import com.mtrovo.statsd.Timing.Context;

public class TimingTest {


    private Endpoint endpoint;
    private Timing timing;

    @Before 
    public void init() {
        this.endpoint = mock(Endpoint.class);
        this.timing = new Timing(endpoint);
    }
    
    @Test
    public void testSendTimeInMs() {
        this.timing.sendTimeMs("@bucket", 100);
        verify(endpoint).send("@bucket:100|ms");
    }
    
    @Test
    public void testFieldSendTimeInMs() {
        BTiming field = this.timing.build("@bucket-a");
        field.sendTimeMs(256);
        verify(endpoint).send("@bucket-a:256|ms");
    }

    private int compute;
    @Test
    public void testTime() {
        Ticker ticker = mock(Ticker.class);
        when(ticker.current()).thenReturn(100L, 142L);
        
        // change the ticker so we can instrument
        this.timing.setTicker(ticker);
        try (Context c = this.timing.time("@bucket-b")){
            int a = 10 + 20;
            int b = 20 + 30;
            
            compute = a * b;
        }
        verify(endpoint).send("@bucket-b:42|ms");
    }
    @Test
    public void testFieldTime() {
        Ticker ticker = mock(Ticker.class);
        when(ticker.current()).thenReturn(1000L, 2618L);
        
        // change the ticker so we can instrument
        this.timing.setTicker(ticker);
        BTiming field = this.timing.build("@bucket-d");
        try (Context c = field.time()){
            int a = 10 + 20;
            int b = 20 + 30;
            
            compute = a * b;
        }
        verify(endpoint).send("@bucket-d:1618|ms");
    }
    
    @Test
    public void testTimeClosure() {
        Ticker ticker = mock(Ticker.class);
        when(ticker.current()).thenReturn(100L, 142L);
        
        // change the ticker so we can instrument
        this.timing.setTicker(ticker);
        this.timing.time("@bucket-b", new Runnable() {

			@Override
			public void run() {
				 
	            int a = 10 + 20;
	            int b = 20 + 30;
	            
	            compute = a * b;				
			}
        });
        verify(endpoint).send("@bucket-b:42|ms");
    }
}
