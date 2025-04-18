package compiler.ast;

public class LiteralExpr implements Expr {
    public final String value;

    public LiteralExpr(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Literal(" + value + ")";
    }
}