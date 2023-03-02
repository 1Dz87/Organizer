package by.itstep.organizaer.model.dto.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyDto {

    private Integer day;

    private String temperature;

    private String wind;
}
