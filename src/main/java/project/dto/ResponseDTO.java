package project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.models.Person;

import java.sql.Timestamp;
import java.time.Period;

@Data
@AllArgsConstructor
public class ResponseDTO<T> {

    private String error;
    private Timestamp timestamp;
    private T t;
}
