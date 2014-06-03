package com.mtrovo.statsd;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UniqueEventTest {

    private Endpoint endpoint;
    private UniqueEvent event;

    @Before
    public void init() {
        this.endpoint = mock(Endpoint.class);
        this.event = new UniqueEvent(endpoint);
    }

    @Test
    public void testSet() throws Exception {
        this.event.set("@bucket");
        verify(endpoint).send("@bucket|s");
    }

    @Test
    public void testFieldSet() throws Exception {
        UniqueEvent.BUniqueEvent field = this.event.build("@bucket-b");
        field.set();
        verify(endpoint).send("@bucket-b|s");
    }
}