package ru.itmo.se.is.platform.cache.infinispan;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@CacheLogged
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_AFTER)
public class CacheCheckInterceptor {

    @Inject
    EntityManager em;

    @AroundInvoke
    public Object around(InvocationContext ctx) throws Exception {
        CacheLogged ann = getAnn(ctx);
        if (ann == null) {
            return ctx.proceed();
        }

        Class<?> entityClass = ann.entity() == Object.class ?
                resolveEntityClassFromTarget(ctx.getTarget()) :
                ann.entity();

        Object[] params = ctx.getParameters();
        Object id = params == null ? null : params[0];

        if (entityClass == null || id == null) {
            CacheLog.usageSkip(entityClass, ctx, "no-id");
            return ctx.proceed();
        }

        Session session = em.unwrap(Session.class);
        SessionFactory sf = session.getSessionFactory();
        org.hibernate.Cache cache = sf.getCache();

        boolean inL2 = cache.containsEntity(entityClass, id);

        if (inL2) {
            CacheLog.usageHit(entityClass, id, ctx);
        } else {
            CacheLog.usageMiss(entityClass, id, ctx);
        }

        return ctx.proceed();
    }

    private static CacheLogged getAnn(InvocationContext ctx) throws NoSuchMethodException {
        CacheLogged ann = ctx.getMethod().getAnnotation(CacheLogged.class);
        if (ann == null) {
            Class<?> targetClass = ctx.getTarget().getClass();
            var implMethod = targetClass.getMethod(
                    ctx.getMethod().getName(),
                    ctx.getMethod().getParameterTypes()
            );
            ann = implMethod.getAnnotation(CacheLogged.class);
        }
        if (ann == null) {
            Class<?> cls = ctx.getTarget().getClass();
            while (cls != null && ann == null) {
                ann = cls.getAnnotation(CacheLogged.class);
                cls = cls.getSuperclass();
            }
        }
        return ann;
    }

    private static Class<?> resolveEntityClassFromTarget(Object target) {
        Class<?> cls = target.getClass();
        while (cls != null) {
            try {
                var field = cls.getDeclaredField("entityClass");
                field.setAccessible(true);
                Object val = field.get(target);
                if (val instanceof Class<?> c) {
                    return c;
                }
            } catch (NoSuchFieldException ignored) {
            } catch (IllegalAccessException ignored) {
                break;
            }
            cls = cls.getSuperclass();
        }
        return null;
    }
}
