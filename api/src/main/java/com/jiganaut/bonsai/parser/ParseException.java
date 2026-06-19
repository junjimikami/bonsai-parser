package com.jiganaut.bonsai.parser;

import java.util.Objects;

import com.jiganaut.bonsai.impl.Message;

/**
 * Exception thrown when parsing fails.
 *
 * @author Junji Mikami
 */
public class ParseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ErrorNode<?> errorNode;

    /**
     * Creates a parse exception from an error node.
     *
     * @param errorNode parse error details
     */
    public ParseException(ErrorNode<?> errorNode) {
        super(getMessage(errorNode));
        this.errorNode = errorNode;
    }

    private static String getMessage(ErrorNode<?> errorNode) {
        Objects.requireNonNull(errorNode, Message.VALIDATION_PARAMETER_NULL.format("errorNode"));
        return errorNode.getMessage();
    }

    /**
     * Returns the parse error details.
     *
     * @return the associated error node
     */
    public ErrorNode<?> getErrorNode() {
        return errorNode;
    }

}
