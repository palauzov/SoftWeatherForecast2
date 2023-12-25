package softuni.exam.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import javax.persistence.Column;
import javax.validation.Validation;


@Component
public class ValidationUtilImpl implements ValidationUtil {

    private final javax.validation.Validator validator;


    public ValidationUtilImpl(Validator validator) {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public <E> boolean isValid(E entity) {
        return this.validator.validate(entity).isEmpty();
    }
}
