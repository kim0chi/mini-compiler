package compiler.ast;

public class ReadStmt implements Stmt {
    public final String var;

    public ReadStmt(String var) {
        this.var = var;
    }

    @Override
    public String toString() {
        return "Read(" + var + ")";
    }
}
