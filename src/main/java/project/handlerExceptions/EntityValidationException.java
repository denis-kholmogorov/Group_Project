package project.handlerExceptions;

public class EntityValidationException extends Exception {

    public <T> EntityValidationException(Class<T> entityClass, String message) {
        super("Error on validation entity: " + entityClass + "\n" + message);
    }
}
