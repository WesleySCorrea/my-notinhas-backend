package my.notinhas.project.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.ObjectNotFoundException;
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
//    @ExceptionHandler(PersistFailedException.class)
//    public ResponseEntity<Object> persistFailedException(PersistFailedException e,HttpServletRequest request) {
//        HttpStatus status = HttpStatus.NOT_FOUND;
//        ValidationError err = new ValidationError();
//        err.setTimestamp(Instant.now());
//        err.setStatus(status.value());
//        err.setError(e.getMessage());
//        err.setPath(request.getRequestURI());
//        err.setErrors(Collections.singletonList(new FieldMessage("PersistFailedException",e.getMessage())));
//        return ResponseEntity.status(status).body(err);
//    }
//
//    @ExceptionHandler(CallHttpErrorException.class)
//    public ResponseEntity<Object> callHttpErrorException(CallHttpErrorException e,HttpServletRequest request) {
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        ValidationError err = new ValidationError();
//        err.setTimestamp(Instant.now());
//        err.setStatus(status.value());
//        err.setError(e.getMessage());
//        err.setPath(request.getRequestURI());
//        err.setErrors(Collections.singletonList(new FieldMessage("CallHttpErrorException",e.getMessage())));
//        return ResponseEntity.status(status).body(err);
//    }
//
//    @ExceptionHandler(UnauthorizedAccessTokenException.class)
//    public ResponseEntity<Object> unauthorizedAccessTokenException(UnauthorizedAccessTokenException e,HttpServletRequest request) {
//        HttpStatus status = HttpStatus.UNAUTHORIZED;
//        ValidationError err = new ValidationError();
//        err.setTimestamp(Instant.now());
//        err.setStatus(status.value());
//        err.setError(e.getMessage());
//        err.setPath(request.getRequestURI());
//        err.setErrors(Collections.singletonList(new FieldMessage("UnauthorizedAccessTokenException",e.getMessage())));
//        return ResponseEntity.status(status).body(err);
//    }
//MONTAR AS EXCEPTIONS

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
//        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
//        ValidationError err = new ValidationError();
//        err.setTimestamp(Instant.now());
//        err.setStatus(status.value());
//        err.setError("Validation exception");
//        err.setPath(request.getRequestURI());
//        for (FieldError f : e.getBindingResult().getFieldErrors()){
//            err.addError(f.getField(),f.getDefaultMessage());
//        }
//        return ResponseEntity.status(status).body(err);
//    }

//    MONTAR EXCEPTIONS PERSONALZADAS
//@ExceptionHandler(ObjectNotFoundException.class)
//public ResponseEntity<Object> objectNotFoundException(ObjectNotFoundException e,HttpServletRequest request) {
//    HttpStatus status = HttpStatus.NOT_FOUND;
//    ValidationError err = new ValidationError();
//    err.setTimestamp(Instant.now());
//    err.setStatus(status.value());
//    err.setError(e.getMessage());
//    err.setPath(request.getRequestURI());
//    err.setErrors(Collections.singletonList(new FieldMessage("ObjectNotFoundException",e.getMessage())));
//    log.error(e.getMessage());
//    return ResponseEntity.status(status).body(err);
//}
}
