package project.models.util.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import project.models.enums.Errors;
import project.models.enums.ErrorsDescription;

@Data
public class Error {

    private Errors error;

    @JsonProperty("error_description")
    private ErrorsDescription errorDescription;
}
