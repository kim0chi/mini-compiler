package compiler.error;

public class SyntaxException extends RuntimeException {
    public final int line, column;
    public SyntaxException(String msg, int line, int column) {
        super("Syntax Error at " + line + ":" + column + " — " + msg);
        this.line   = line;
        this.column = column;
    }
}
