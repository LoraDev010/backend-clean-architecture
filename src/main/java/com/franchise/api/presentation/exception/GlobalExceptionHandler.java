package com.franchise.api.presentation.exception;

import com.franchise.api.application.exception.BranchNotFoundException;
import com.franchise.api.application.exception.FranchiseNotFoundException;
import com.franchise.api.application.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FranchiseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFranchiseNotFound(FranchiseNotFoundException ex) {
        return new ErrorResponse(404, "Not Found", ex.getMessage());
    }

    @ExceptionHandler(BranchNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBranchNotFound(BranchNotFoundException ex) {
        return new ErrorResponse(404, "Not Found", ex.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductNotFound(ProductNotFoundException ex) {
        return new ErrorResponse(404, "Not Found", ex.getMessage());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(WebExchangeBindException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ErrorResponse(400, "Bad Request", message);
    }
}
