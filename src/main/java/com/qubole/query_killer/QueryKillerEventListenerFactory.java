package com.qubole.query_killer;

import com.facebook.presto.spi.eventlistener.EventListener;
import com.facebook.presto.spi.eventlistener.EventListenerFactory;

import java.util.Map;

public class QueryKillerEventListenerFactory
        implements EventListenerFactory
{
    public String getName()
    {
        return "query-killer-event-listener";
    }

    public EventListener create(Map<String, String> config)
    {
        return new QueryKiller();
    }

}
