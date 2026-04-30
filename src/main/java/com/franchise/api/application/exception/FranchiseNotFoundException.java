package com.franchise.api.application.exception;

public class FranchiseNotFoundException extends RuntimeException {
    public FranchiseNotFoundException(String id) {
        super("Franchise not found: " + id);
    }
}
