package ru.study.shop.adapters.controllers.utils.dto_validation.custom_constraints;

import ru.study.shop.adapters.controllers.utils.dto_validation.constraint_impl.NotEmptyObjectValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The annotated element must have at least one non-null field.
 * Accepts objects with get/set name conventions.
 */
@Target({ TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = NotEmptyObjectValidator.class)
@Documented
public @interface NotEmptyObject {
    String message() default "{ru.study.shop.adapters.controllers.utils.dto_validation.custom_constraints.NotEmptyObject.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
