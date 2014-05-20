package com.mtrovo.statsd;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.mtrovo.statsd.Gauge.BGauge;

public class GaugeTest {

    private Endpoint endpoint;
    private Gauge gauge;

    @Before 
    public void init() {
        this.endpoint = mock(Endpoint.class);
        this.gauge = new Gauge(endpoint);
    }
    
    @Test
    public void testConstructor() {
        this.gauge.set("@bucket", 42);
        verify(endpoint).send("@bucket:42|g");
    }
    
    @Test
    public void testIncrementPositive() {
        this.gauge.increment("@bucket.a", 100);
        verify(endpoint).send("@bucket.a:+100|g");
    }
    
    @Test
    public void testIncrementNegative() {
        try {
            this.gauge.increment("@bucket.a", -100);
            fail();
        }catch(IllegalArgumentException e){
            // ok
        }
        verifyZeroInteractions(endpoint);
    }

    @Test
    public void testDecrementPositive() {
        this.gauge.decrement("@bucket.a", 100);
        verify(endpoint).send("@bucket.a:-100|g");
    }
    
    @Test
    public void testDecrementNegative() {
        try {
            this.gauge.decrement("@bucket.a", -100);
            fail();
        }catch(IllegalArgumentException e){
            // ok
        }
        verifyZeroInteractions(endpoint);
    }

    @Test
    public void testSendDeltaPositive() {
        this.gauge.sendDelta("@bucket.a", 100);
        verify(endpoint).send("@bucket.a:+100|g");
    }
    
    @Test
    public void testSendDeltaNegative() {
        this.gauge.sendDelta("@bucket.a", -100);
        verify(endpoint).send("@bucket.a:-100|g");
    }

    @Test
    public void testSetPositive() {
        this.gauge.set("@bucket.a", 100);
        verify(endpoint).send("@bucket.a:100|g");
    }
    
    @Test
    public void testSetNegative() {
        this.gauge.set("@bucket.a", -100);
        verify(endpoint).send("@bucket.a:0|g");
        verify(endpoint).send("@bucket.a:-100|g");
    }

    @Test
    public void testFieldIncrementPositive() {
        BGauge field = this.gauge.build("@bucket.field");
        field.increment(100);
        verify(endpoint).send("@bucket.field:+100|g");
    }
    
    @Test
    public void testFieldIncrementNegative() {
        try {
            BGauge field = this.gauge.build("@bucket.field");
            field.increment(-100);
            fail();
        }catch(IllegalArgumentException e){
            // ok
        }
        verifyZeroInteractions(endpoint);
    }

    @Test
    public void testFieldDecrementPositive() {
        BGauge field = this.gauge.build("@bucket.field");
        field.decrement(100);
        verify(endpoint).send("@bucket.field:-100|g");
    }
    
    @Test
    public void testFieldDecrementNegative() {
        try {
            BGauge field = this.gauge.build("@bucket.field");
            field.decrement(-100);
            fail();
        }catch(IllegalArgumentException e){
            // ok
        }
        verifyZeroInteractions(endpoint);
    }

    @Test
    public void testFieldSendDeltaPositive() {
        BGauge field = this.gauge.build("@bucket.field");
        field.sendDelta(100);
        verify(endpoint).send("@bucket.field:+100|g");
    }
    
    @Test
    public void testFieldSendDeltaNegative() {
        BGauge field = this.gauge.build("@bucket.field");
        field.sendDelta(-100);
        verify(endpoint).send("@bucket.field:-100|g");
    }

    @Test
    public void testFieldSetPositive() {
        BGauge field = this.gauge.build("@bucket.field");
        field.set(100);
        verify(endpoint).send("@bucket.field:100|g");
    }
    
    @Test
    public void testFieldSetNegative() {
        BGauge field = this.gauge.build("@bucket.field");
        field.set(-100);
        verify(endpoint).send("@bucket.field:0|g");
        verify(endpoint).send("@bucket.field:-100|g");
    }
}
