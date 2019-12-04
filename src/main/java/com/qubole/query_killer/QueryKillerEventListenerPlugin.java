package com.qubole.query_killer;

import com.facebook.presto.spi.Plugin;
import com.facebook.presto.spi.eventlistener.EventListenerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryKillerEventListenerPlugin
    implements Plugin
{
    @Override
    public Iterable<EventListenerFactory> getEventListenerFactories()
    {
        EventListenerFactory listenerFactory = new QueryKillerEventListenerFactory();
        List<EventListenerFactory> listenerFactoryList = new ArrayList<>();
        listenerFactoryList.add(listenerFactory);
        List<EventListenerFactory> immutableList = Collections.unmodifiableList(listenerFactoryList);
        return immutableList;
    }
}
