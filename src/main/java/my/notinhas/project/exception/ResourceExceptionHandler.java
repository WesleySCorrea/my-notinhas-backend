package my.notinhas.project.exception;

import jakarta.servlet.http.HttpServletRequest;
import my.notinhas.project.exception.runtime.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.Collections;

@ControllerAdvice
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Object> objectNotFoundException(ObjectNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ValidationError err = new ValidationError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError(e.getMessage());
        err.setPath(request.getRequestURI());
        err.setErrors(Collections.singletonList(new FieldMessage("ObjectNotFoundException",e.getMessage())));
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(UnauthorizedIdTokenException.class)
    public ResponseEntity<Object> unauthorizedIdTokenException(UnauthorizedIdTokenException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ValidationError err = new ValidationError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError(e.getMessage());
        err.setPath(request.getRequestURI());
        err.setErrors(Collections.singletonList(new FieldMessage("UnauthorizedIdTokenException",e.getMessage())));
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(CallHttpErrorException.class)
    public ResponseEntity<Object> callHttpErrorException(CallHttpErrorException e,HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidationError err = new ValidationError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError(e.getMessage());
        err.setPath(request.getRequestURI());
        err.setErrors(Collections.singletonList(new FieldMessage("CallHttpErrorException",e.getMessage())));
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ObjectConversionException.class)
    public ResponseEntity<Object> objectConversionException(ObjectConversionException e,HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidationError err = new ValidationError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError(e.getMessage());
        err.setPath(request.getRequestURI());
        err.setErrors(Collections.singletonList(new FieldMessage("ObjectConversionException",e.getMessage())));
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(PersistFailedException.class)
    public ResponseEntity<Object> persistFailedException(PersistFailedException e,HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ValidationError err = new ValidationError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError(e.getMessage());
        err.setPath(request.getRequestURI());
        err.setErrors(Collections.singletonList(new FieldMessage("PersistFailedException",e.getMessage())));
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DuplicateVoteAttemptException.class)
    public ResponseEntity<Object> duplicateVoteAttemptException(DuplicateVoteAttemptException e,HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ValidationError err = new ValidationError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError(e.getMessage());
        err.setPath(request.getRequestURI());
        err.setErrors(Collections.singletonList(new FieldMessage("DuplicateVoteAttemptException",e.getMessage())));
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InvalidUserNameException.class)
    public ResponseEntity<Object> invalidUserNameException(InvalidUserNameException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidationError err = new ValidationError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError(e.getMessage());
        err.setPath(request.getRequestURI());
        err.setErrors(Collections.singletonList(new FieldMessage("InvalidUserNameException",e.getMessage())));
        return ResponseEntity.status(status).body(err);
    }
}
