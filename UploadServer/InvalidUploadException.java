public class InvalidUploadException extends Exception {
    public InvalidUploadException(String message) {
        super(message);
    }

    public InvalidUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}

