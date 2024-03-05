package my.notinhas.project.controllers.testes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    private String verify() {
        return "Você esta conectado";
    }


    @GetMapping("/login")
    private String login() {
        return "Livre para login";
    }

}
