package com.franquicias.sucursales.exception;

public class FranchiseNotFoundException extends RuntimeException {
    public FranchiseNotFoundException(String id) {
        super("Franchise not found: " + id);
    }
}
