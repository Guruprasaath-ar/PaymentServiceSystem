package dev.guru.TransactionService.customExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class TransactionExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalCurrencyException.class)
    public ResponseEntity<String> handleIllegalCurrencyException(IllegalCurrencyException e) {
        return new ResponseEntity<>("Unsupported Currency Code", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalTransactionArgumentException.class)
    public ResponseEntity<String> handleIllegalTransactionArgumentException(IllegalTransactionArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}
