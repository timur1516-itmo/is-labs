package ru.itmo.se.is.platform.cache.infinispan;


import jakarta.interceptor.InvocationContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheLog {

    public static void usageHit(Class<?> entityClass, Object id, InvocationContext ctx) {
        log("HIT", simpleName(entityClass), id, source(ctx));
    }

    public static void usageMiss(Class<?> entityClass, Object id, InvocationContext ctx) {
        log("MISS", simpleName(entityClass), id, source(ctx));
    }

    public static void usageSkip(Class<?> entityClass, InvocationContext ctx, String reason) {
        log("SKIP",
                entityClass != null ? simpleName(entityClass) : "-",
                "-", source(ctx) + " reason=" + reason);
    }

    public static void cachePut(String cacheName, Object key) {
        log("PUT", entityFromCacheName(cacheName), idFromKey(key), "listener");
    }

    public static void cacheRemove(String cacheName, Object key) {
        log("REMOVE", entityFromCacheName(cacheName), idFromKey(key), "listener");
    }

    public static void cacheUpdate(String cacheName, Object key) {
        log("UPDATE", entityFromCacheName(cacheName), idFromKey(key), "listener");
    }

    public static void cacheVisit(String cacheName, Object key) {
        log("VISIT", entityFromCacheName(cacheName), idFromKey(key), "listener");
    }

    private static void log(
            String event,
            Object entity,
            Object id,
            Object source
    ) {
        log.info("[L2][{}] entity={} id={} source={}", event, entity, id, source);
    }

    private static String simpleName(Class<?> entityClass) {
        if (entityClass == null) return "-";
        return entityClass.getSimpleName();
    }

    private static String entityFromCacheName(String cacheName) {
        if (cacheName == null) return "-";
        int i = cacheName.lastIndexOf('.');
        if (i >= 0 && i < cacheName.length() - 1) {
            return cacheName.substring(i + 1);
        }
        return cacheName;
    }

    private static String idFromKey(Object key) {
        return key.toString().split("#")[1];
    }

    private static String source(InvocationContext ctx) {
        return ctx.getTarget().getClass().getSimpleName() +
                "." +
                ctx.getMethod().getName();
    }
}