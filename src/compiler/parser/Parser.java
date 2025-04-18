package compiler.parser;

import compiler.ast.*;
import compiler.error.SyntaxException;
import compiler.lexer.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser that builds an AST (Program) from a list of tokens,
 * throwing SyntaxException on any parse error with accurate line/column.
 */
public class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Always returns a token from the list. The last token must be EOF
     * (emitted by the Lexer with correct position).
     */
    private Token currentToken() {
        if (pos < tokens.size()) {
            return tokens.get(pos);
        }
        // If we ever run past EOF, just return the EOF token itself:
        return tokens.get(tokens.size() - 1);
    }

    /**
     * Consume the current token if it matches the expected type.
     * Otherwise throw a SyntaxException at the token’s location.
     */
    private void eat(TokenType expected) {
        Token actual = currentToken();
        if (actual.type == expected) {
            pos++;
        } else {
            throw new SyntaxException(
                "Expected " + expected + " but found " + actual.type,
                actual.line, actual.column
            );
        }
    }

    /**
     * Entry point: parses the entire token stream into a Program AST node.
     */
    public Program parseProgram() {
        List<Stmt> stmts = new ArrayList<>();
        while (currentToken().type != TokenType.EOF) {
            stmts.add(parseStatement());
        }
        return new Program(stmts);
    }

    /**
     * Parses a single statement and returns its AST node.
     */
    private Stmt parseStatement() {
        Token t = currentToken();
        switch (t.type) {
            case READ -> {
                eat(TokenType.READ);
                Token id = currentToken();
                eat(TokenType.IDENTIFIER);
                return new ReadStmt(id.value);
            }
            case PRINT -> {
                eat(TokenType.PRINT);
                Token id = currentToken();
                eat(TokenType.IDENTIFIER);
                return new PrintStmt(id.value);
            }
            case IDENTIFIER -> {
                Token id = t;
                eat(TokenType.IDENTIFIER);
                eat(TokenType.ASSIGN);
                Expr expr = parseExpression();
                return new AssignStmt(id.value, expr);
            }
            case IF -> {
                eat(TokenType.IF);
                eat(TokenType.LPAREN);
                Expr cond = parseExpression();
                eat(TokenType.RPAREN);
                List<Stmt> thenBlock = parseBlock();
                List<Stmt> elseBlock = new ArrayList<>();
                if (currentToken().type == TokenType.ELSE) {
                    eat(TokenType.ELSE);
                    elseBlock = parseBlock();
                }
                return new IfStmt(cond, thenBlock, elseBlock);
            }
            case WHILE -> {
                eat(TokenType.WHILE);
                eat(TokenType.LPAREN);
                Expr cond = parseExpression();
                eat(TokenType.RPAREN);
                List<Stmt> body = parseBlock();
                return new WhileStmt(cond, body);
            }
            default -> {
                // No valid statement start found
                throw new SyntaxException(
                    "Invalid statement start: " + t.value,
                    t.line, t.column
                );
            }
        }
    }

    /**
     * Parses a block of statements enclosed in braces { … }.
     */
    private List<Stmt> parseBlock() {
        Token lbrace = currentToken();
        eat(TokenType.LBRACE);
        List<Stmt> stmts = new ArrayList<>();
        while (currentToken().type != TokenType.RBRACE) {
            stmts.add(parseStatement());
        }
        eat(TokenType.RBRACE);
        return stmts;
    }

    /**
     * Parses expressions with + and - (lowest precedence).
     */
    private Expr parseExpression() {
        Expr left = parseTerm();
        while (currentToken().type == TokenType.PLUS
            || currentToken().type == TokenType.MINUS) {

            Token op = currentToken();
            eat(op.type);
            Expr right = parseTerm();
            left = new BinaryExpr(op.value, left, right);
        }
        return left;
    }

    /**
     * Parses terms with * and /.
     */
    private Expr parseTerm() {
        Expr left = parseFactor();
        while (currentToken().type == TokenType.MUL
            || currentToken().type == TokenType.DIV) {

            Token op = currentToken();
            eat(op.type);
            Expr right = parseFactor();
            left = new BinaryExpr(op.value, left, right);
        }
        return left;
    }

    /**
     * Parses atomic factors: NUMBER, STRING, BOOLEAN, IDENTIFIER, or (expression).
     */
    private Expr parseFactor() {
        Token t = currentToken();
        switch (t.type) {
            case NUMBER -> {
                eat(TokenType.NUMBER);
                return new LiteralExpr(t.value);
            }
            case STRING -> {
                eat(TokenType.STRING);
                // strip the surrounding quotes
                String s = t.value.substring(1, t.value.length() - 1);
                return new LiteralExpr(s);
            }
            case BOOLEAN_TRUE -> {
                eat(TokenType.BOOLEAN_TRUE);
                return new LiteralExpr("true");
            }
            case BOOLEAN_FALSE -> {
                eat(TokenType.BOOLEAN_FALSE);
                return new LiteralExpr("false");
            }
            case IDENTIFIER -> {
                eat(TokenType.IDENTIFIER);
                return new VarExpr(t.value);
            }
            case LPAREN -> {
                eat(TokenType.LPAREN);
                Expr expr = parseExpression();
                eat(TokenType.RPAREN);
                return expr;
            }
            default -> {
                // Unexpected token in an expression context
                throw new SyntaxException(
                    "Unexpected token in factor: " + t.value,
                    t.line, t.column
                );
            }
        }
    }
}
