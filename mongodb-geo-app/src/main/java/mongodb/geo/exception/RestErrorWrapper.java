package mongodb.geo.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import java.util.List;

public class RestErrorWrapper {

    @JsonIgnore
    private HttpStatus status;
    private String error;
    private String message;
    private String path;
    private List<Validation> validations;

    public RestErrorWrapper() { }

    public RestErrorWrapper(final HttpStatus status,
                            final String message,
                            final String path) {
        this.error = status.getReasonPhrase();
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public Integer getStatus() { return status.value(); }
    public String getError() { return error; }
    public String getMessage() {
        return message;
    }
    public String getPath() {
        return path;
    }

    public HttpStatus httpStatus() { return status; }

    public List<Validation> getValidations() {
        return validations;
    }

    public void setValidations(final List<Validation> validations) {
        this.validations = validations;
    }

    @Override
    public String toString() {
        final ObjectMapper objectMapper = new ObjectMapper();
        String jsonErrorString = "";

        try {
            jsonErrorString = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonErrorString;
    }

    public static class Validation {
        private String code;
        private String field;
        private String rejectedValue;

        public Validation() {}

        public Validation(final String code,
                          final String field,
                          final String rejectedValue) {
            this.code = code;
            this.field = field;
            this.rejectedValue = rejectedValue;
        }

        public String getCode() {
            return code;
        }

        public void setCode(final String code) {
            this.code = code;
        }

        public String getField() {
            return field;
        }

        public void setField(final String field) {
            this.field = field;
        }

        public String getRejectedValue() {
            return rejectedValue;
        }

        public void setRejectedValue(final String rejectedValue) {
            this.rejectedValue = rejectedValue;
        }
    }
}
