package com.brennandDigital.Projeto.Services.Exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(Object id){
        super("Resource not found. " + id);
    }
}

