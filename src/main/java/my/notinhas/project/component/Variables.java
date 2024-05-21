package my.notinhas.project.component;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Variables {

    @Value("${delete.after.one.day}")
    private Boolean deleteAfterOneDay;
}
