package my.notinhas.project.exception.runtime;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DuplicateVoteAttemptException extends RuntimeException {

    public DuplicateVoteAttemptException(String msg){
        super(msg);
    }
}