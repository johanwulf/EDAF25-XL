package model;

public class CircularError extends Throwable {

    public CircularError(String message) {
        super(message);
    }
}
