package project.models.util.entity;

import lombok.Data;

import java.util.Date;

@Data
abstract class Response {

    private String error = "";

    private Long timestamp = new Date().getTime();
}
