package by.itstep.organizaer.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@ConfigurationProperties(prefix = "project")
@Component
public class ProjectConfiguration {

    private String formatDate;

    private String formatDateTime;

    private Security security;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Security {

        private String secretKey;

        private Integer tokenLifetimeHours;
    }
}
