package ru.itmo.se.is.platform.cache.infinispan;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryVisited;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryVisitedEvent;


@Listener(observation = Listener.Observation.POST)
public class CacheListener {
    @CacheEntryCreated
    public void created(CacheEntryCreatedEvent<Object, Object> e) {
        CacheLog.cachePut(e.getCache().getName(), e.getKey());
    }

    @CacheEntryVisited
    public void entryVisited(CacheEntryVisitedEvent<Object, Object> e) {
        CacheLog.cacheVisit(e.getCache().getName(), e.getKey());
    }

    @CacheEntryRemoved
    public void removed(CacheEntryRemovedEvent<Object, Object> e) {
        CacheLog.cacheRemove(e.getCache().getName(), e.getKey());
    }

    @CacheEntryModified
    public void modified(CacheEntryModifiedEvent<Object, Object> e) {
        CacheLog.cacheUpdate(e.getCache().getName(), e.getKey());
    }
}
