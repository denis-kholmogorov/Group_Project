package project.models.util.entity;

import lombok.Data;

import java.util.Date;

@Data
abstract class Response {

    private String error = "string";

    private Long timestamp = new Date().getTime();
}
