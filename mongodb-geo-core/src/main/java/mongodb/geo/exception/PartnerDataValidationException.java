package mongodb.geo.exception;

import org.springframework.validation.FieldError;

import java.util.List;

public class PartnerDataValidationException extends RuntimeException {

    private List<FieldError> errors;

    public PartnerDataValidationException(final List<FieldError> errors) {
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        if(errors == null || errors.isEmpty()) {
            return getMessage();
        }

        if(errors.size() == 1) {
            return "Partner validation error. There is 1 error.";
        }

        return String.format("Partner validation error. There are %d errors.", errors.size());
    }

    public List<FieldError> getErrors() {
        return errors;
    }

}
