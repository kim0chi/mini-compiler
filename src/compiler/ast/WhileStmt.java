package compiler.ast;

import java.util.List;

public class WhileStmt implements Stmt {
    public final Expr cond;
    public final List<Stmt> body;

    public WhileStmt(Expr cond, List<Stmt> body) {
        this.cond = cond;
        this.body = body;
    }

    @Override
    public String toString() {
        return "While(" + cond + ", body=" + body + ")";
    }
}