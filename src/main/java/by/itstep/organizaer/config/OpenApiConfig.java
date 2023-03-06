package by.itstep.organizaer.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        type = SecuritySchemeType.APIKEY,
        name = "basicAuth",
        scheme = "basic",
        in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {


}
