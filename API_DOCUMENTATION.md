# API Documentation

## Current API status

There are no production business API endpoints in the application. No request/response contracts for rewards, accounts, transactions, or promotions have been implemented.

## Error response foundation

Spring MVC exceptions are handled centrally by `GlobalExceptionHandler` when a production controller is added:

| Exception | HTTP status | Client message |
| --- | ---: | --- |
| `ResourceNotFoundException` | 404 | The exception's message |
| `BadRequestException` | 400 | The exception's message |
| Other `Exception` | 500 | `An unexpected error occurred.` |

The error body uses `ApiErrorResponse`:

```json
{
  "timestamp": "2026-01-01T12:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Reward with id 42 was not found",
  "path": "/api/example"
}
```

The timestamp is an `Instant`; status is numeric, error is the HTTP reason phrase, and path comes from the request URI. The sample message above illustrates the tested error shape, not an implemented production endpoint. Unexpected exception messages and stack traces are not returned to clients.

## Test-only routes

`TestExceptionController` defines `/_test/exceptions/not-found`, `/_test/exceptions/bad-request`, and `/_test/exceptions/unexpected` only in test source. MockMvc uses them to verify handler behavior. They are not production API routes and are not part of the packaged application.
