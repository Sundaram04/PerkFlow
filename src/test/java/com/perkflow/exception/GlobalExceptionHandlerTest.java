package com.perkflow.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TestExceptionController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void resourceNotFoundReturnsConsistent404Response() throws Exception {
        assertErrorResponse(
                TestExceptionController.NOT_FOUND_PATH,
                HttpStatus.NOT_FOUND,
                "Not Found",
                "Reward with id 42 was not found");
    }

    @Test
    void badRequestReturnsConsistent400Response() throws Exception {
        assertErrorResponse(
                TestExceptionController.BAD_REQUEST_PATH,
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                "The amount must be greater than zero");
    }

    @Test
    void unexpectedExceptionReturnsSafe500Response() throws Exception {
        assertErrorResponse(
                TestExceptionController.UNEXPECTED_ERROR_PATH,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred.");
    }

    private void assertErrorResponse(
            String path,
            HttpStatus expectedStatus,
            String expectedError,
            String expectedMessage) throws Exception {
        mockMvc.perform(get(path))
                .andExpect(status().is(expectedStatus.value()))
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(expectedStatus.value()))
                .andExpect(jsonPath("$.error").value(expectedError))
                .andExpect(jsonPath("$.message").value(expectedMessage))
                .andExpect(jsonPath("$.path").value(path));
    }
}
