package compiler.lexer;

public class Token {
    public final TokenType type;   // The type of token (e.g., NUMBER, IDENTIFIER)
    public final String value;     // The actual text/characters from input (e.g., "5", "x")
    public final int line;        // The line number in the source code where the token was found
    public final int column;      // The column number in the source code where the token was found

    // Constructor to create a token
    public Token(TokenType type, String value, int line, int column) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    // Displays the token as text when printed
    @Override
    public String toString() {
        return String.format("Token(%s, \"%s\", %d:%d)",
                             type, value, line, column);
    }
}
