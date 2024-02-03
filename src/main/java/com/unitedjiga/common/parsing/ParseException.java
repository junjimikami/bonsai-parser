package com.unitedjiga.common.parsing;

/**
 * @author Junji Mikami
 *
 */
public class ParseException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public ParseException() {
        super();
    }

    /**
     * 
     */
    public ParseException(String message) {
        super(message);
    }
}
