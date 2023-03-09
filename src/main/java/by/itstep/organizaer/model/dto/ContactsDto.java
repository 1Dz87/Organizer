package by.itstep.organizaer.model.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Schema(description = "Описание объекта \"Контакты\"")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults( level = AccessLevel.PRIVATE)
public class ContactsDto {

    @Schema(title = "Адрес", description = "Адрес", maxLength = 255)
    String address;

    @Schema(title = "Номер телефона в международном формате", description = "Номер телефона в международном формате", pattern = "^\\(\\+\\d[4]\\)\\d+$")
    @NotBlank
    @Pattern(regexp = "^\\(\\+\\d[4]\\)\\d+$")
    @Size(max = 16, min = 6)
    String phone;


    @ArraySchema(maxItems = 5, uniqueItems = true, schema = @Schema(title = "Адрес электронной почты", description = "Список адресов электронной почты"))
    List<String> email;

    @ArraySchema(maxItems = 10, uniqueItems = true, schema = @Schema(title = "Контакт в мессенджерах", description = "Список контактов в мессенджерах"))
    List<String> messengers;
}
