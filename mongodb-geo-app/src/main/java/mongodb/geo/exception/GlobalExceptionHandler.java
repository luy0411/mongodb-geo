package mongodb.geo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoPartnerException.class)
    public ResponseEntity<Object> handlePartnerNotFound(final NoPartnerException exception,
                                                        final HttpServletRequest request) {
        final RestErrorWrapper restError = new RestErrorWrapper(HttpStatus.NOT_FOUND,
                                                                exception.getMessage(),
                                                                request.getServletPath());
        LOGGER.error(restError.toString());
        return new ResponseEntity<>(restError, new HttpHeaders(), restError.httpStatus());
    }

    @ExceptionHandler(PartnerDataValidationException.class)
    public ResponseEntity<Object> handlePartnerValidationException(final PartnerDataValidationException exception,
                                                                   final HttpServletRequest request) {
        final RestErrorWrapper restError = new RestErrorWrapper(HttpStatus.BAD_REQUEST,
                            exception.getMessage(), request.getServletPath());

        readValidations(exception, restError);
        LOGGER.error(restError.toString());
        return new ResponseEntity<>(restError, new HttpHeaders(), restError.httpStatus());
    }

    private void readValidations(final PartnerDataValidationException exception,
                                 final RestErrorWrapper restError) {
        final List<RestErrorWrapper.Validation> validations = new ArrayList<>();
        for (final FieldError error : exception.getErrors()) {
            validations.add(new RestErrorWrapper.Validation(error.getCode(), error.getField(), error.getRejectedValue().toString()));
        }
        restError.setValidations(validations);
    }

}
