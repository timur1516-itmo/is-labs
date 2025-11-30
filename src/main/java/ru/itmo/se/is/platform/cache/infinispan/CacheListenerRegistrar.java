package ru.itmo.se.is.platform.cache.infinispan;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.hibernate.SessionFactory;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.infinispan.Cache;
import org.infinispan.hibernate.cache.v6.InfinispanRegionFactory;
import org.infinispan.manager.EmbeddedCacheManager;

@Singleton
@Startup
public class CacheListenerRegistrar {

    private final CacheListener cacheListener = new CacheListener();
    @PersistenceUnit(unitName = "my-persistence-unit")
    private EntityManagerFactory emf;

    @PostConstruct
    public void init() {
        SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
        SessionFactoryImplementor sfi = (SessionFactoryImplementor) sessionFactory;

        RegionFactory regionFactory = sfi
                .getSessionFactoryOptions()
                .getServiceRegistry()
                .getService(RegionFactory.class);

        if (regionFactory instanceof InfinispanRegionFactory infinispanRegionFactory) {
            EmbeddedCacheManager cacheManager = infinispanRegionFactory.getCacheManager();

            Cache<Object, Object> personCache =
                    cacheManager.getCache("Person");
            personCache.addListener(cacheListener);

            Cache<Object, Object> movieCache =
                    cacheManager.getCache("Movie");
            movieCache.addListener(cacheListener);

            Cache<Object, Object> importOperationCache =
                    cacheManager.getCache("ImportOperation");
            importOperationCache.addListener(cacheListener);
        }
    }
}
