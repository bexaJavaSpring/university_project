package uz.java.spring_boot_application.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.java.spring_boot_application.exception.CustomAccessDeniedException;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.util.Translator;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
//@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Translator translator;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleError(final MethodArgumentNotValidException ex) {
//        log.error("MethodArgumentNotValidException on: {}", ErrorUtil.getStacktrace(ex));
        String message = translator.toLocale(ex.getMessage());
        return new ResponseEntity<>(Map.of("message", message), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GenericNotFoundException.class)
    public ResponseEntity<?> handleGenericNotFoundException(final GenericNotFoundException e) {
//        log.error("GenericNotFoundException on: {}", ErrorUtil.getStacktrace(e));
        String message = translator.toLocale(e.getMessage());
        return new ResponseEntity<>(Map.of("message", List.of(message)), new HttpHeaders(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(final AccessDeniedException e) {
        String message = translator.toLocale(e.getMessage());
        return new ResponseEntity<>(Map.of("message", List.of(message)), new HttpHeaders(),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomAccessDeniedException.class)
    public ResponseEntity<?> handleCustomAccessDeniedException(final CustomAccessDeniedException e) {
        String message = translator.toLocale(e.getMessage());
        return new ResponseEntity<>(Map.of("message", List.of(message)), new HttpHeaders(),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({RuntimeException.class, Throwable.class})
    public ResponseEntity<?> handleException(final Exception e) {
//        log.error("Exception on: {}", ErrorUtil.getStacktrace(e));
        return new ResponseEntity<>(Map.of("message", e.getMessage()), new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
