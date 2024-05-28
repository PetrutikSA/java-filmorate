package ru.yandex.practicum.filmorate.model.validator.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class AfterValidator implements ConstraintValidator<After, LocalDate> {
    private LocalDate date;

    @Override
    public void initialize(After annotation) {
        date = LocalDate.parse(annotation.value());
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = false;
        if (value != null) {
            if (!value.isBefore(date)) {
                valid = true;
            }
        }
        return valid;
    }
}