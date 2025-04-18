package compiler.lexer;

// This enum lists all types of tokens our language can recognize
public enum TokenType {
    NUMBER, IDENTIFIER,
    STRING, BOOLEAN_TRUE, BOOLEAN_FALSE,
    ASSIGN, PLUS, MINUS, MUL, DIV,
    LPAREN, RPAREN,
    PRINT, READ,
    IF, ELSE, WHILE,
    LBRACE, RBRACE,
    EOF
}

