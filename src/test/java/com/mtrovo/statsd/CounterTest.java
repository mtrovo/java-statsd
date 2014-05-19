package com.mtrovo.statsd;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.mtrovo.statsd.Counter.BCounter;

public class CounterTest {

    
    private Endpoint endpoint;
    private Counter counter;

    @Before 
    public void init() {
        this.endpoint = mock(Endpoint.class);
        this.counter = new Counter(endpoint);
    }
    
    @Test
    public void testConstructor() {
        this.counter.send("@bucket", 1);
        verify(endpoint, times(1)).send("@bucket:1|c");
    }

    @Test
    public void testIncrementString() {
        this.counter.increment("@bucket");
        verify(endpoint, times(1)).send("@bucket:1|c");
    }

    @Test
    public void testIncrementStringLongPositive() {
        this.counter.increment("@bucket2", 42);
        verify(endpoint, times(1)).send("@bucket2:42|c");
    }

    @Test
    public void testIncrementStringLongNegative() {
        try {
            this.counter.increment("@bucket2", -42);
            fail();
        }catch(IllegalArgumentException e) {
            // ok
        }
        verifyZeroInteractions(endpoint);
    }
    
    @Test
    public void testDecrementString() {
        this.counter.decrement("@bucket");
        verify(endpoint, times(1)).send("@bucket:-1|c");
    }

    @Test
    public void testDecrementStringLongNegative() {
        this.counter.decrement("@bucket", 10);
        verify(endpoint, times(1)).send("@bucket:-10|c");
    }
    
    @Test
    public void testDecrementStringLongPositive() {
        try {
            this.counter.decrement("@bucket", -10);
            fail();
        }catch(IllegalArgumentException e){
            // ok
        }
        
        verifyZeroInteractions(endpoint);
    }

    @Test
    public void testSend() {
        this.counter.send("@bucket", 10);
        verify(endpoint, times(1)).send("@bucket:10|c");
        this.counter.send("@bucket", -10);
        verify(endpoint, times(1)).send("@bucket:-10|c");
    }

    @Test
    public void testBuild() {
        BCounter bucketc = this.counter.build("@bucketc");
        assertEquals("@bucketc", bucketc.bucket);
    }

    @Test
    public void testBuildDecrement() {
        BCounter bucketc = this.counter.build("@bucketc");
        bucketc.decrement();
        verify(endpoint, times(1)).send("@bucketc:-1|c");
    }
    
    @Test
    public void testBuildDecrementDiffPositive() {
        BCounter bucketc = this.counter.build("@bucketc");
        bucketc.decrement(10);
        verify(endpoint, times(1)).send("@bucketc:-10|c");
    }
    
    @Test
    public void testBuildDecrementDiffNegative() {
        BCounter bucketc = this.counter.build("@bucketc");
        try {
            bucketc.decrement(-10);
            fail();
        }catch(IllegalArgumentException e) {
            // ok
        }
        verifyZeroInteractions(endpoint);
    }
    
    @Test
    public void testBuildIncrement() {
        BCounter bucketc = this.counter.build("@bucketc");
        bucketc.increment();
        verify(endpoint, times(1)).send("@bucketc:1|c");
    }

    @Test
    public void testBuildIncrementDiffPositive() {
        BCounter bucketc = this.counter.build("@bucketc");
        bucketc.increment(10);
        verify(endpoint, times(1)).send("@bucketc:10|c");
    }
    
    @Test
    public void testBuildIncrementDiffNegative() {
        BCounter bucketc = this.counter.build("@bucketc");
        try {
            bucketc.increment(-10);
            fail();
        }catch(IllegalArgumentException e) {
            // ok
        }
        verifyZeroInteractions(endpoint);
    }
}
