package com.studentsystemapp.util;

import com.studentsystemapp.validation.StringInPastOrPresentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = StringInPastOrPresentValidator.class
)
@Target( ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StringInPastOrPresent {


    String message() default "The date is not in the future!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
