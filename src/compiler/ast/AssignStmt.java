package compiler.ast;

public class AssignStmt implements Stmt {
    public final String var;
    public final Expr expr;

    public AssignStmt(String var, Expr expr) {
        this.var = var;
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "Assign(" + var + ", " + expr + ")";
    }
}