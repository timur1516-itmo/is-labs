package ru.itmo.se.is.platform.cache.infinispan;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.*;
import org.infinispan.notifications.cachelistener.event.*;

@Listener
public class CacheListener {
    @CacheEntryCreated
    public void entryCreated(CacheEntryCreatedEvent<Object, Object> event) {
        this.printLog("Adding key '" + event.getKey()
                + "' to cache", event);
    }

    @CacheEntryExpired
    public void entryExpired(CacheEntryExpiredEvent<Object, Object> event) {
        this.printLog("Expiring key '" + event.getKey()
                + "' from cache", event);
    }

    @CacheEntryVisited
    public void entryVisited(CacheEntryVisitedEvent<Object, Object> event) {
        this.printLog("Key '" + event.getKey() + "' was visited", event);
    }

    @CacheEntryModified
    public void entryModified(CacheEntryModifiedEvent<Object, Object> event) {
        printLog("Key '" + event.getKey() + "' was modified", event);
    }

    @CacheEntryRemoved
    public void entryRemoved(CacheEntryRemovedEvent<Object, Object> event) {
        printLog("Key '" + event.getKey() + "' was removed from cache", event);
    }

    @CacheEntryActivated
    public void entryActivated(CacheEntryActivatedEvent<Object, Object> event) {
        this.printLog("Activating key '" + event.getKey()
                + "' on cache", event);
    }

    @CacheEntryPassivated
    public void entryPassivated(CacheEntryPassivatedEvent<Object, Object> event) {
        this.printLog("Passivating key '" + event.getKey()
                + "' from cache", event);
    }

    @CacheEntryLoaded
    public void entryLoaded(CacheEntryLoadedEvent<Object, Object> event) {
        this.printLog("Loading key '" + event.getKey()
                + "' to cache", event);
    }

    @CacheEntriesEvicted
    public void entriesEvicted(CacheEntriesEvictedEvent<Object, Object> event) {
        StringBuilder builder = new StringBuilder();
        event.getEntries().forEach(
                (key, value) -> builder.append(key).append(", "));
        System.out.println("Evicting following entries from cache: "
                + builder);
    }

    private void printLog(String log, CacheEntryEvent<?, ?> event) {
        if (!event.isPre()) {
            System.out.println(log);
        }
    }
}
