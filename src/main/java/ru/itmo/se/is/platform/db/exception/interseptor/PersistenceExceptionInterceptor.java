package ru.itmo.se.is.platform.db.exception.interseptor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.persistence.OptimisticLockException;
import ru.itmo.se.is.platform.db.exception.annotation.TranslatePersistenceExceptions;
import ru.itmo.se.is.shared.exception.BusinessException;
import ru.itmo.se.is.shared.exception.ConcurrentModificationException;
import ru.itmo.se.is.shared.exception.EntityAlreadyExistsException;
import ru.itmo.se.is.shared.exception.EntityConnectedToAnotherEntityException;

import java.sql.SQLException;

@TranslatePersistenceExceptions
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE)
public class PersistenceExceptionInterceptor {

    @AroundInvoke
    public Object handle(InvocationContext ctx) throws Exception {
        try {
            return ctx.proceed();
        } catch (Exception ex) {
            BusinessException be = translateToBusiness(ex);
            if (be != null) {
                throw be;
            }
            throw ex;
        }
    }

    private BusinessException translateToBusiness(Throwable ex) {
        Throwable cause = ex;

        while (cause != null) {

            if (cause instanceof BusinessException be) {
                return be;
            }

            if (cause instanceof OptimisticLockException ole) {
                return new ConcurrentModificationException(
                        "Entity has already been modified by another user",
                        ole
                );
            }

            if (cause instanceof SQLException sql) {
                BusinessException fromSql = mapSqlState(sql, sql);
                if (fromSql != null) {
                    return fromSql;
                }
            }

            cause = cause.getCause();
        }

        return null;
    }

    private BusinessException mapSqlState(SQLException sql, Throwable original) {
        String state = sql.getSQLState();

        if ("23503".equals(state)) {
            return new EntityConnectedToAnotherEntityException(
                    "Operation violates foreign key constraints",
                    original
            );
        }

        if ("23505".equals(state)) {
            return new EntityAlreadyExistsException(
                    "Entity with given unique value already exists",
                    original
            );
        }

        if ("40001".equals(state)) {
            return new ConcurrentModificationException(
                    "Concurrent modification detected (serialization failure)",
                    original
            );
        }

        return null;
    }
}
