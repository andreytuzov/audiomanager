package by.tuzau.andrey.rusandroidapp.data.service.exception;

/**
 * Created by User on 19.11.2017.
 */

public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
