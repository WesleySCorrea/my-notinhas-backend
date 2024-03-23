package my.notinhas.project.exception;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
public class ValidationError extends StandardError{
    private List<FieldMessage> errors = new ArrayList<>();

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addError(String fieldName, String message){
        errors.add(new FieldMessage(fieldName,message));
    }
}
