package dm.demo.countryfinder.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.Map;

/**
 * Base handling of exceptions passed to frontend
 */
@Slf4j
@ControllerAdvice
public class RestExceptionResolver extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    protected ResponseEntity<Object> handleRestException(Exception e, WebRequest request) {
        log.error("Unexpected REST exception", e);

        String message = "Server error occurred";
        Map<String, Object> body = Collections.singletonMap("message", message);
        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
