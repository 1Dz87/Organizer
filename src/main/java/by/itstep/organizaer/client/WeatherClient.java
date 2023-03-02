package by.itstep.organizaer.client;

import by.itstep.organizaer.exceptions.CityNotFoundException;
import by.itstep.organizaer.exceptions.InteractionException;
import by.itstep.organizaer.model.dto.weather.Forecast;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@RequiredArgsConstructor
public class WeatherClient {

    @Value("${project.business.client.weather-url}")
    private String url;

    private final ObjectMapper defaultObjectMapper;

    public Forecast getForecat(String city) {
        if (StringUtils.isBlank(city)) {
            throw new CityNotFoundException(city);
        }
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url.concat(city))).GET().build();
            String responseBody = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
            return defaultObjectMapper.readValue(responseBody, Forecast.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new InteractionException("Не удалось получить прогноз погоды. Попробуйте позднее", e);
        }
    }
}
