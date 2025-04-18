package compiler.codegen;

import compiler.ast.*;         // your Stmt/Expr classes
import java.util.*;

public class TACGenerator {
    private int tempCount = 0;
    private int labelCount = 0;
    private final List<String> code = new ArrayList<>();

    private String newTemp() { return "t" + (++tempCount); }
    private String newLabel() { return "L" + (++labelCount); }

    public List<String> generate(List<Stmt> stmts) {
        for (Stmt s : stmts) emitStmt(s);
        return code;
    }

    private void emitStmt(Stmt s) {
        if (s instanceof AssignStmt a) {
            String t = emitExpr(a.expr);
            code.add(a.var + " = " + t);
        } else if (s instanceof ReadStmt r) {
            code.add("READ " + r.var);
        } else if (s instanceof PrintStmt p) {
            code.add("PRINT " + p.var);
        } else if (s instanceof IfStmt i) {
            String elseLabel = newLabel();
            String endLabel  = newLabel();
            String condTemp  = emitExpr(i.cond);
            code.add("IFZ " + condTemp + " GOTO " + elseLabel);
            i.thenBlock.forEach(this::emitStmt);
            code.add("GOTO " + endLabel);
            code.add(elseLabel + ":");
            i.elseBlock.forEach(this::emitStmt);
            code.add(endLabel + ":");
        } else if (s instanceof WhileStmt w) {
            String start = newLabel(), end = newLabel();
            code.add(start + ":");
            String condTemp = emitExpr(w.cond);
            code.add("IFZ " + condTemp + " GOTO " + end);
            w.body.forEach(this::emitStmt);
            code.add("GOTO " + start);
            code.add(end + ":");
        }
    }

    private String emitExpr(Expr e) {
        if (e instanceof LiteralExpr l) {
            String t = newTemp();
            code.add(t + " = " + l.value);
            return t;
        }
        if (e instanceof VarExpr v) {
            return v.name;
        }
        if (e instanceof BinaryExpr b) {
            String t1 = emitExpr(b.left);
            String t2 = emitExpr(b.right);
            String t = newTemp();
            code.add(t + " = " + t1 + " " + b.op + " " + t2);
            return t;
        }
        throw new RuntimeException("Unknown Expr: " + e);
    }
}
