package project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import project.models.enums.MessagesPermission;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PersonDto {
    private Integer id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("reg_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime regDate;

    @JsonProperty("birth_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

    private String email;

    private String phone;

    private String photo;

    private String about;

    private CityDto city;

    private CountryDto country;

    @JsonProperty("messages_permission")
    private MessagesPermission messagePermission;

    @JsonProperty("last_online_time")
    private LocalDateTime lastOnlineTime;

    @JsonProperty("is_blocked")
    private boolean isBlocked;

}
