package ru.itmo.se.is.platform.db.eclipselink.tx;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.eclipse.persistence.sessions.UnitOfWork;
import ru.itmo.se.is.platform.db.eclipselink.UnitOfWorkManager;
import ru.itmo.se.is.platform.db.eclipselink.tx.annotation.Transactional;

@Interceptor
@Transactional
public class TransactionalInterceptor {
    @Inject
    private UnitOfWorkManager uowManager;
    @Inject
    private TransactionLockProvider transactionLockProvider;

    @AroundInvoke
    public Object around(InvocationContext ctx) throws Exception {

        Transactional ann = ctx.getMethod()
                .getAnnotation(Transactional.class);
        if (ann == null) {
            Class<?> cls = ctx.getTarget().getClass();
            while (cls != null && ann == null) {
                ann = cls.getAnnotation(Transactional.class);
                cls = cls.getSuperclass();
            }
        }

        TransactionalMode mode = ann != null ? ann.mode() : TransactionalMode.REQUIRED;

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
                    transactionLockProvider.getLock().lock();
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
                if (lockAcquired) {
                    transactionLockProvider.getLock().unlock();
                }
                uowManager.end();
            }
        }
    }
}

