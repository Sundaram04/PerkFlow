package com.perkflow.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestExceptionController {

    static final String NOT_FOUND_PATH = "/_test/exceptions/not-found";
    static final String BAD_REQUEST_PATH = "/_test/exceptions/bad-request";
    static final String UNEXPECTED_ERROR_PATH = "/_test/exceptions/unexpected";

    @GetMapping(NOT_FOUND_PATH)
    public void notFound() {
        throw new ResourceNotFoundException("Reward with id 42 was not found");
    }

    @GetMapping(BAD_REQUEST_PATH)
    public void badRequest() {
        throw new BadRequestException("The amount must be greater than zero");
    }

    @GetMapping(UNEXPECTED_ERROR_PATH)
    public void unexpected() {
        throw new IllegalStateException("Database password must never appear in a response");
    }
}
