package by.itstep.organizaer.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final ProjectConfiguration projectConfiguration;

    @Bean
    @Primary // чтобы спринг использовал его по-умолчанию
    public ObjectMapper defaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(
                DateTimeFormatter.ofPattern(projectConfiguration
                        .getFormatDateTime()));
        LocalDateDeserializer localDateDeserializer = new LocalDateDeserializer(
                DateTimeFormatter.ofPattern(projectConfiguration
                        .getFormatDate()));
        LocalDateSerializer localDateSerializer = new LocalDateSerializer(
                DateTimeFormatter.ofPattern(projectConfiguration
                        .getFormatDate()));
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(
                DateTimeFormatter.ofPattern(projectConfiguration
                        .getFormatDateTime()));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
                .registerModule(new JavaTimeModule())
                .registerModule(new SimpleModule().addDeserializer(LocalDateTime.class, localDateTimeDeserializer))
                .registerModule(new SimpleModule().addDeserializer(LocalDate.class, localDateDeserializer))
                .registerModule(new SimpleModule().addSerializer(LocalDate.class, localDateSerializer))
                .registerModule(new SimpleModule().addSerializer(LocalDateTime.class, localDateTimeSerializer));
        return objectMapper;
    }
}
