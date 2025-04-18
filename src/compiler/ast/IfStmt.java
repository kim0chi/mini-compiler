package compiler.ast;

import java.util.List;

public class IfStmt implements Stmt {
    public final Expr cond;
    public final List<Stmt> thenBlock;
    public final List<Stmt> elseBlock;

    public IfStmt(Expr cond, List<Stmt> thenBlock, List<Stmt> elseBlock) {
        this.cond = cond;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public String toString() {
        return "If(" + cond + ", then=" + thenBlock + ", else=" + elseBlock + ")";
    }
}
