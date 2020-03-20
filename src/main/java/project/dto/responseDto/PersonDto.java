package project.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto
{

    private Integer id = 2;

    private String first_name = "Вася";

    private String last_name = "Форточкин";

    private Long reg_date = new Date().getTime();

    private Long birth_date = new Date().getTime();

    private String email = "fortochkin@mail.ru";

    private String phone = "1123213";

    private String photo = "https://yandex.ru/collections/card/5bbdd8555c6b7600713464c1/";

    private String about = "Нечего";

    private String city = "New York";

    private String country = "USA";

    private String messages_permission = "ALL";

    private Long last_online_time = new Date().getTime();

    private boolean is_blocked = false;

}

