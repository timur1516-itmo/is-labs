package ru.itmo.se.is.platform.db.eclipselink.tx.annotation;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;
import ru.itmo.se.is.platform.db.eclipselink.tx.TransactionalMode;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@InterceptorBinding
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface Transactional {
    @Nonbinding
    TransactionalMode mode() default TransactionalMode.REQUIRED;
}
