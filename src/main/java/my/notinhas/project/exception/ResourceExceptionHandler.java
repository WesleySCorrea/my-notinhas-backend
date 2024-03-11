package my.notinhas.project.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;


@ControllerAdvice
public class ResourceExceptionHandler {


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
