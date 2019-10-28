package com.wexom.zonkydemo.marketplace.api.exception;

/**
 * Runtime exception for handling problems during the connection to Zonky API.
 */
public class ZonkyClientException extends RuntimeException {

    /**
     * C-tor.
     *
     * @param message exception message
     * @param cause   cause of the exception
     */
    public ZonkyClientException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
