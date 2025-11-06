package fr.checkconsulting.scpi_doc_validation_api.ressource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/app")
@CrossOrigin(origins = "http://localhost:4200")
public class AppRessource {

    @GetMapping
    public String welcome() {
        return "Bienvenue sur votre application de validation des documents";
    }

}
