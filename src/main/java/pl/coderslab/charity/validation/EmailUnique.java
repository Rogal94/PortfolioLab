package pl.coderslab.charity.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailUniqueValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface EmailUnique {
    String message() default "{UsernameUnique.error.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
