package ru.itmo.se.is.platform.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.itmo.se.is.platform.validation.annotation.AllowedValues;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AllowedValuesValidator implements ConstraintValidator<AllowedValues, Object> {

    Set<String> allowedValues = new HashSet<>();

    @Override
    public void initialize(AllowedValues constraintAnnotation) {
        allowedValues = new HashSet<>(Arrays.asList(constraintAnnotation.value()));
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        String strValue = value.toString();

        boolean valid = allowedValues.contains(strValue);

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Value: '" + strValue + "' is not allowed. Allowed: " + allowedValues
            ).addConstraintViolation();
        }

        return valid;
    }
}
