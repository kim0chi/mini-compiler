package compiler.lexer;

import java.util.*;
import java.util.regex.*;

public class Lexer {
    // List of all token rules (keyword, symbol, or identifier matchers)
    private static final List<TokenRule> rules = new ArrayList<>();

    // Define the regex rules when the class loads
    static {
        // Order matters: keywords go before generic identifiers
        rules.add(new TokenRule("IF", TokenType.IF, "\\bif\\b"));
        rules.add(new TokenRule("ELSE", TokenType.ELSE, "\\belse\\b"));
        rules.add(new TokenRule("WHILE", TokenType.WHILE, "\\bwhile\\b"));
        rules.add(new TokenRule("LBRACE", TokenType.LBRACE, "\\{"));
        rules.add(new TokenRule("RBRACE", TokenType.RBRACE, "\\}"));
        rules.add(new TokenRule("PRINT", TokenType.PRINT, "\\bprint\\b"));
        rules.add(new TokenRule("READ", TokenType.READ, "\\bread\\b"));
        rules.add(new TokenRule("NUMBER", TokenType.NUMBER, "\\b\\d+\\b"));
        rules.add(new TokenRule("STRING", TokenType.STRING, "\"[^\"]*\""));
        rules.add(new TokenRule("BOOLEAN_TRUE", TokenType.BOOLEAN_TRUE, "\\btrue\\b"));
        rules.add(new TokenRule("BOOLEAN_FALSE", TokenType.BOOLEAN_FALSE, "\\bfalse\\b"));

        rules.add(new TokenRule("IDENTIFIER", TokenType.IDENTIFIER, "\\b[a-zA-Z_]\\w*\\b"));
        rules.add(new TokenRule("ASSIGN", TokenType.ASSIGN, "="));
        rules.add(new TokenRule("PLUS", TokenType.PLUS, "\\+"));
        rules.add(new TokenRule("MINUS", TokenType.MINUS, "-"));
        rules.add(new TokenRule("MUL", TokenType.MUL, "\\*"));
        rules.add(new TokenRule("DIV", TokenType.DIV, "/"));
        rules.add(new TokenRule("LPAREN", TokenType.LPAREN, "\\("));
        rules.add(new TokenRule("RPAREN", TokenType.RPAREN, "\\)"));

        // Whitespace (including newlines) — skip but still update line/column
        rules.add(new TokenRule("WHITESPACE", null, "\\s+"));
    }

    /**
     * Tokenize input string into a list of tokens, each annotated with line/column.
     */
    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int pos = 0;            // index in input
        int line = 1;           // current line number (1-based)
        int column = 1;         // current column number (1-based)

        while (pos < input.length()) {
            boolean matched = false;

            for (TokenRule rule : rules) {
                Matcher m = rule.pattern.matcher(input);
                m.region(pos, input.length());

                if (m.lookingAt()) {
                    String val = m.group();
                    int startLine = line;
                    int startColumn = column;

                    // advance pos, line, and column counters over the matched text
                    for (char c : val.toCharArray()) {
                        if (c == '\n') {
                            line++;
                            column = 1;
                        } else {
                            column++;
                        }
                        pos++;
                    }

                    // only produce a token if rule.type != null
                    if (rule.type != null) {
                        tokens.add(new Token(rule.type, val, startLine, startColumn));
                    }

                    matched = true;
                    break;
                }
            }

            if (!matched) {
                throw new RuntimeException(
                    "Unexpected character at " + line + ":" + column +
                    " → '" + input.charAt(pos) + "'"
                );
            }
        }

        // EOF token at the end, annotated with final position
        tokens.add(new Token(TokenType.EOF, "", line, column));
        return tokens;
    }

    // Helper class to store a regex rule and its associated token type
    private static class TokenRule {
        final TokenType type;
        final Pattern pattern;

        TokenRule(String name, TokenType type, String regex) {
            this.type    = type;
            this.pattern = Pattern.compile(regex);
        }
    }
}
