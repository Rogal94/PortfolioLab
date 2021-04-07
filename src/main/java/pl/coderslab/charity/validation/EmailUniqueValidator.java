package pl.coderslab.charity.validation;

import org.springframework.beans.factory.annotation.Autowired;
import pl.coderslab.charity.user.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, String> {

    public EmailUniqueValidator() {
    }

    private UserService userService;
    @Autowired
    public EmailUniqueValidator(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void initialize(EmailUnique constraintAnnotation) {
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if ( value == null ) {
            return true;
        }
        return userService.findByUserName(value) == null;
    }
}
