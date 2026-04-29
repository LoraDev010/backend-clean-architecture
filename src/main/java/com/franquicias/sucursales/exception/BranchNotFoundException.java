package com.franquicias.sucursales.exception;

public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(String id) {
        super("Branch not found: " + id);
    }
}
