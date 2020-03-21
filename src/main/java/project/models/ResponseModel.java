package project.models;

import lombok.Data;

import java.util.Date;

@Data
public abstract class ResponseModel {
    private String error = "";

    private Long timestamp = new Date().getTime();
}
