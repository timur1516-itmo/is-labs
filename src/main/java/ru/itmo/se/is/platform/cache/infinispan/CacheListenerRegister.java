package ru.itmo.se.is.platform.cache.infinispan;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.infinispan.Cache;
import org.infinispan.hibernate.cache.v6.InfinispanRegionFactory;
import org.infinispan.manager.EmbeddedCacheManager;

@Slf4j
@Singleton
@Startup
public class CacheListenerRegister {

    @PersistenceUnit(unitName = "my-persistence-unit")
    private EntityManagerFactory emf;

    @PostConstruct
    public void init() {
        SessionFactoryImplementor sfi = emf.unwrap(SessionFactoryImplementor.class);

        RegionFactory regionFactory = sfi.getCache().getRegionFactory();

        if (!(regionFactory instanceof InfinispanRegionFactory infinispanRegionFactory)) {
            log.warn("[L2] RegionFactory не InfinispanRegionFactory, listener wont be registered");
            return;
        }

        EmbeddedCacheManager cacheManager = infinispanRegionFactory.getCacheManager();

        CacheListener listener = new CacheListener();

        cacheManager.getCacheNames().stream()
                .filter(cacheName -> !cacheName.endsWith("pending-puts"))
                .forEach(cacheName -> {
                    Cache<Object, Object> cache = cacheManager.getCache(cacheName);
                    cache.addListener(listener);
                    log.info("[L2] Listener registered for cache: {}", cacheName);
                });
    }
}
