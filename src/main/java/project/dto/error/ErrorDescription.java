package project.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import project.dto.error.enums.ErrorDescriptionEnum;

public class ErrorDescription
{
    @JsonProperty(value = "error_description")
    ErrorDescriptionEnum errorDescription;
}
