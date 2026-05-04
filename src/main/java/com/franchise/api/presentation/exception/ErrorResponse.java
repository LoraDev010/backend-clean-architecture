package com.franchise.api.presentation.exception;

public record ErrorResponse(int status, String error, String message) {}
