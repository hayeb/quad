package nl.quad.opentrivia.exception;

import nl.quad.opentrivia.exception.rest.dto.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OpenTriviaExceptionHandler {

    @ExceptionHandler(OpenTriviaException.class)
    public ResponseEntity<ErrorDto> handleOpenTriviaException(OpenTriviaException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }
}
