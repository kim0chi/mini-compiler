package compiler.ast;

public class PrintStmt implements Stmt {
    public final String var;

    public PrintStmt(String var) {
        this.var = var;
    }

    @Override
    public String toString() {
        return "Print(" + var + ")";
    }
}
