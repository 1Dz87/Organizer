package by.itstep.organizaer.model.dto.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Forecast {

    private String temperature;

    private String wind;

    private String description;

    private List<DailyDto> forecast;
}
