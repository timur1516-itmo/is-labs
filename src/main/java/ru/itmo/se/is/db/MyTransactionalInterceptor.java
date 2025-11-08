package ru.itmo.se.is.db;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.eclipse.persistence.sessions.UnitOfWork;
import ru.itmo.se.is.annotation.MyTransactional;
import ru.itmo.se.is.service.UniqueLockProvider;

@Interceptor
@MyTransactional
public class MyTransactionalInterceptor {
    @Inject
    private UnitOfWorkManager uowManager;
    @Inject
    private UniqueLockProvider uniqueLockProvider;

    @AroundInvoke
    public Object around(InvocationContext ctx) throws Exception {

        MyTransactional ann = ctx.getMethod()
                .getAnnotation(MyTransactional.class);
        if (ann == null) {
            Class<?> cls = ctx.getTarget().getClass();
            while (cls != null && ann == null) {
                ann = cls.getAnnotation(MyTransactional.class);
                cls = cls.getSuperclass();
            }
        }

        MyTransactionMode mode = ann != null ? ann.mode() : MyTransactionMode.REQUIRED;

        UnitOfWork existing = uowManager.getCurrent();
        UnitOfWork uow;
        boolean owner;
        boolean lockAcquired;

        switch (mode) {
            case REQUIRES_NEW:
                uow = uowManager.beginRequiresNew();
                owner = true;
                lockAcquired = false;
                break;
            case REQUIRED:
            default:
                if (existing == null) {
                    uniqueLockProvider.getUniqueLock().lock();
                    uow = uowManager.beginRequired();
                    owner = true;
                    lockAcquired = true;
                } else {
                    uow = existing;
                    owner = false;
                    lockAcquired = false;
                }
                break;
        }

        try {
            Object result = ctx.proceed();
            if (owner) {
                uow.commit();
            }
            return result;
        } catch (Exception ex) {
            if (owner && uow != null && uow.isActive()) {
                uow.release();
            }
            throw ex;
        } finally {
            if (owner) {
                if(lockAcquired) {
                    uniqueLockProvider.getUniqueLock().unlock();
                }
                uowManager.end();
            }
        }
    }
}

