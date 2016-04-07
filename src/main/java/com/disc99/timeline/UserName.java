package com.disc99.timeline;


import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Size(min = 4)
public @interface UserName {

    String message() default "{com.disc99.timeline.UserName.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
