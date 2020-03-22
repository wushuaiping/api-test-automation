package io.idwangmo.testing.config;

public class TestException extends RuntimeException {

    private String error;

    public TestException(String message, String error) {
        super(message);
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
