package ru.itmo.se.is.platform.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.itmo.se.is.platform.validation.annotation.ValidEmbedded;
import ru.itmo.se.is.shared.dto.common.EmbeddedObjectDto;

public class EmbeddedObjectValidator implements ConstraintValidator<ValidEmbedded, EmbeddedObjectDto<?, ?>> {

    boolean nullable;

    @Override
    public void initialize(ValidEmbedded constraintAnnotation) {
        nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(EmbeddedObjectDto<?, ?> value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) return nullable;
        return value.isNew() || value.isReference();
    }
}
