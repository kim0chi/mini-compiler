// src/compiler/ast/Program.java
package compiler.ast;

import java.util.List;

public class Program {
    public final List<Stmt> statements;

    public Program(List<Stmt> statements) {
        this.statements = statements;
    }

    @Override
    public String toString() {
        return "Program" + statements;
    }
}
