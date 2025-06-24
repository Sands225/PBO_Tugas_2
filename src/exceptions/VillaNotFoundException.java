package exceptions;

public class VillaNotFoundException extends RuntimeException {
    public VillaNotFoundException(String message) {
        super(message);
    }
}
