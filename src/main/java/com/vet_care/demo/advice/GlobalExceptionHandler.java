package com.vet_care.demo.advice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

/**
 * @author Alish
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidation(MethodArgumentNotValidException ex, Model model) {
        String errors = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        model.addAttribute("errorMessage", errors);
        return "error";
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleUnsupportedExceptions(HttpRequestMethodNotSupportedException ex, Model model) {
        model.addAttribute("errorMessage", "This operation is not supported");
        return "error";
    }

    @ExceptionHandler({
            EntityNotFoundException.class,
            AccessDeniedException.class,
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public String handleExpectedExceptions(RuntimeException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralError(Exception ex, Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred");
        return "error";
    }
}

