package compiler.error;

public class SemanticException extends RuntimeException {
    public SemanticException(String msg) {
        super("Semantic Error — " + msg);
    }
}
