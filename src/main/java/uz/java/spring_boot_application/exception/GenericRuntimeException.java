package uz.java.spring_boot_application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GenericRuntimeException extends RuntimeException {

    public String field;

    public GenericRuntimeException(String message) {
        super(message);
    }

    public GenericRuntimeException(String field, String message) {
        super(message);
        this.field = field;
    }
}
