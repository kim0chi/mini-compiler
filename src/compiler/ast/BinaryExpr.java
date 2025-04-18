package compiler.ast;

public class BinaryExpr implements Expr {
    public final String op;
    public final Expr left;
    public final Expr right;

    public BinaryExpr(String op, Expr left, Expr right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "Binary(" + op + ", " + left + ", " + right + ")";
    }
}
