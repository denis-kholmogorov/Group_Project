package project.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.models.Person;

@Data
@NoArgsConstructor
@AllArgsConstructor
public
class PersonDtoWithToken
{
    private Person person;
    private String token;

}


