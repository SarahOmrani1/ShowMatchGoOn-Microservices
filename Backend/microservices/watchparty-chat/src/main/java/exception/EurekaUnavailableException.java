package exception;

public class EurekaUnavailableException extends RuntimeException {
    public EurekaUnavailableException(String message) {
        super(message);
    }
}