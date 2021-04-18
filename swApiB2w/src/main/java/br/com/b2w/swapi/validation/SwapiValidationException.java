package br.com.b2w.swapi.validation;

import org.springframework.http.HttpStatus;

public class SwapiValidationException extends Exception {

	private static final long serialVersionUID = 7657913129164631528L;
	
	private HttpStatus httpStatus;

    public SwapiValidationException() {
        super();
    }

    public SwapiValidationException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public SwapiValidationException(HttpStatus httpStatus, Throwable cause) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public SwapiValidationException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
    
    public HttpStatus getHttpStatus() {
    	return httpStatus;
    }
    
    public void setHttpStatus(HttpStatus httpStatus) {
    	this.httpStatus = httpStatus;
    }

}
