package com.studentsystemapp.validation;

import com.studentsystemapp.util.StringInPastOrPresent;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class StringInPastOrPresentValidator implements ConstraintValidator<StringInPastOrPresent, String> {



        @Override
        public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
            if (value != null && !value.isBlank()) {
                LocalDate parse = LocalDate.parse(value);

                return parse.isBefore(LocalDate.now()) || parse.isEqual(LocalDate.now());
            }

            return false;
        }

        @Override
        public void initialize(StringInPastOrPresent constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }

}
