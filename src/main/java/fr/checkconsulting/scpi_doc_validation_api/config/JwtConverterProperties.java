package fr.checkconsulting.scpi_doc_validation_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt.auth.converter")
public class JwtConverterProperties {

    private String ressourceId;
    private String principalAttribute;
}
