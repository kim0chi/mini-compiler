package compiler.ast;

// src/compiler/ast/VarExpr.java
public class VarExpr implements Expr {
    public final String name;

    public VarExpr(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Var(" + name + ")";
    }
}