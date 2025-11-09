package ru.itmo.se.is.platform.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.itmo.se.is.platform.validation.validator.AllowedValuesValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AllowedValuesValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedValues {
    String message() default "Values is not allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value();
}
