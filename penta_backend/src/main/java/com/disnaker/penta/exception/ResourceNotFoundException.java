package com.disnaker.penta.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException forId(String entityName, Long id) {
        return new ResourceNotFoundException(entityName + " dengan id " + id + " tidak ditemukan");
    }
}
