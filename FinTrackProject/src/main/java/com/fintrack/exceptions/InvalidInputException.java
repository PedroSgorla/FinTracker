package com.fintrack.exceptions;

/**
 * Thrown to indicate that user-supplied input for a transaction (or an
 * operation on the transaction list) is invalid, such as an empty
 * description, a non-positive amount, an out-of-range day of month, or
 * an out-of-bounds index.
 */
public class InvalidInputException extends Exception{

    /**
     * Creates a new exception with the given detail message.
     *
     * @param message description of what made the input invalid
     */
    public InvalidInputException(String message){
        super(message);
    }
}
