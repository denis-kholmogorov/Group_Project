package project.models.util.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import project.dto.error.ErrorDescription;

@Data
public class Error {

    private Error error;

    @JsonProperty("error_description")
    private ErrorDescription errorDescription;
}
