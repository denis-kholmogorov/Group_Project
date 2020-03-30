package project.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.models.Person;

@Data
@NoArgsConstructor
@AllArgsConstructor
public
class PersonDtoWithToken extends Person
{
    private String token;
}


