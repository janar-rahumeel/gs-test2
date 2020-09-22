package ee.geckosolutions.demo.test.service;

import org.springframework.http.HttpStatus;

public class ShellHelperException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ShellHelperException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        if (httpStatus == HttpStatus.NOT_FOUND) {
            return super.getMessage();
        }
        return "Sorry mate, Bitcoin is Bonzi! I can't provide any information to you. Please try again later :)";
    }

}
