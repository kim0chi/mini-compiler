# 📜 Grammar Specification (BNF)

This grammar defines the syntax of the custom mini-language.

<program> ::= { <statement> }

<statement> ::= "read" <id> | "print" <id> | <id> "=" <expression> | "if" "(" <expression> ")" <block> [ "else" <block> ] | "while" "(" <expression> ")" <block>

<block> ::= "{" { <statement> } "}"

<expression> ::= <term> { ("+" | "-") <term> }

<term> ::= <factor> { ("*" | "/") <factor> }

<factor> ::= <number> | <string> | "true" | "false" | <id> | "(" <expression> ")"

<id> ::= letter { letter | digit | "_" } <number> ::= digit { digit } <string> ::= """ { character } """


### Notes

- **Booleans**: `true` and `false` evaluate to 1 or 0 in arithmetic context, or as `boolean` in conditions.
- **Semicolons**: Not required—line-based separation assumed.
- **Variable types**: Dynamically inferred as `int`, `boolean`, or `string` during read or assignment.