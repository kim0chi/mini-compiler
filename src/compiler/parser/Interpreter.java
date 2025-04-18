// File: src/compiler/parser/Interpreter.java
package compiler.parser;

import compiler.ast.*;
import compiler.error.SemanticException;
import java.util.*;
import javax.swing.*;

/**
 * Interpreter that:
 * 1) Collects all vars used in boolean contexts (if/while conditions)
 * 2) On read, enforces boolean-only input for those vars
 * 3) Executes the program and collects print outputs
 */
public class Interpreter {
    /**
     * Runs the program in-memory, prompting for any reads
     * and collecting prints. Returns the list of output lines.
     */
    public static List<String> run(Program program) {
        Memory.clear();
        // 1) Gather variables used in boolean contexts
        Set<String> boolVars = collectBooleanVars(program);
        // 2) Execute statements
        List<String> out = new ArrayList<>();
        for (Stmt stmt : program.statements) {
            evalStmt(stmt, out, boolVars);
        }
        return out;
    }

    // Scan the AST for variables appearing in if/while conditions only
    private static Set<String> collectBooleanVars(Program program) {
        Set<String> result = new HashSet<>();
        for (Stmt stmt : program.statements) {
            scanStmtForBooleans(stmt, result);
        }
        return result;
    }

    private static void scanStmtForBooleans(Stmt stmt, Set<String> result) {
        if (stmt instanceof IfStmt is) {
            scanExpr(is.cond, result);
            is.thenBlock.forEach(s -> scanStmtForBooleans(s, result));
            is.elseBlock.forEach(s -> scanStmtForBooleans(s, result));
        }
        else if (stmt instanceof WhileStmt ws) {
            scanExpr(ws.cond, result);
            ws.body.forEach(s -> scanStmtForBooleans(s, result));
        }
        // Do not scan assignments, reads, or prints for boolean enforcement
    }

    private static void scanExpr(Expr expr, Set<String> result) {
        if (expr instanceof VarExpr ve) {
            result.add(ve.name);
        }
        else if (expr instanceof BinaryExpr be) {
            scanExpr(be.left, result);
            scanExpr(be.right, result);
        }
        // LiteralExpr has no variables
    }

    // Evaluate a statement, enforcing boolean-only reads for vars in boolVars
    private static void evalStmt(Stmt stmt, List<String> out, Set<String> boolVars) {
        if (stmt instanceof ReadStmt rs) {
            String name = rs.var;
            String inp = JOptionPane.showInputDialog(null, "Enter value for " + name + ":");
            if (inp == null) inp = "";

            if (boolVars.contains(name)) {
                // must be true/false
                if (!(inp.equalsIgnoreCase("true") || inp.equalsIgnoreCase("false"))) {
                    throw new SemanticException(
                        "Variable '" + name + "' must be boolean (true/false)"
                    );
                }
                Memory.store(name, Boolean.parseBoolean(inp));
            } else {
                // int or string
                if (inp.matches("-?\\d+")) {
                    Memory.store(name, Integer.parseInt(inp));
                } else {
                    Memory.store(name, inp);
                }
            }
        }
        else if (stmt instanceof PrintStmt ps) {
            Variable v = Memory.getVariable(ps.var);
            String line = switch (v.type) {
                case "int"     -> ps.var + " = " + v.intValue;
                case "boolean" -> ps.var + " = " + v.boolValue;
                case "string"  -> ps.var + " = " + v.stringValue;
                default        -> ps.var + " = [unknown]";
            };
            out.add(line);
        }
        else if (stmt instanceof AssignStmt as) {
            Object val = evalExpr(as.expr);
            if (val instanceof Integer i)       Memory.store(as.var, i);
            else if (val instanceof Boolean b)   Memory.store(as.var, b);
            else if (val instanceof String s)    Memory.store(as.var, s);
        }
        else if (stmt instanceof IfStmt is) {
            Object c = evalExpr(is.cond);
            boolean cond = (c instanceof Boolean) ? (Boolean)c : ((Integer)c != 0);
            if (cond) {
                is.thenBlock.forEach(s -> evalStmt(s, out, boolVars));
            } else {
                is.elseBlock.forEach(s -> evalStmt(s, out, boolVars));
            }
        }
        else if (stmt instanceof WhileStmt ws) {
            while (true) {
                Object c = evalExpr(ws.cond);
                boolean cond = (c instanceof Boolean) ? (Boolean)c : ((Integer)c != 0);
                if (!cond) break;
                ws.body.forEach(s -> evalStmt(s, out, boolVars));
            }
        }
        else {
            throw new RuntimeException("Interpreter: unknown stmt " + stmt);
        }
    }

    // Evaluate an expression, returning Integer, Boolean, or String
    private static Object evalExpr(Expr expr) {
        if (expr instanceof LiteralExpr le) {
            String v = le.value;
            if (v.matches("-?\\d+"))      return Integer.parseInt(v);
            if (v.equalsIgnoreCase("true"))  return true;
            if (v.equalsIgnoreCase("false")) return false;
            return v;  // string literal
        }
        if (expr instanceof VarExpr ve) {
            Variable var = Memory.getVariable(ve.name);
            return switch (var.type) {
                case "int"     -> var.intValue;
                case "boolean" -> var.boolValue;
                case "string"  -> var.stringValue;
                default        -> null;
            };
        }
        if (expr instanceof BinaryExpr be) {
            Object L = evalExpr(be.left);
            Object R = evalExpr(be.right);
            // ensure both are integers
            if (!(L instanceof Integer) || !(R instanceof Integer)) {
                String lt = typeName(L), rt = typeName(R);
                throw new SemanticException(
                    "Cannot apply operator '" + be.op +
                    "' to types '" + lt + "' and '" + rt + "'"
                );
            }
            int li = (Integer)L, ri = (Integer)R;
            return switch (be.op) {
                case "+" -> li + ri;
                case "-" -> li - ri;
                case "*" -> li * ri;
                case "/" -> {
                    if (ri == 0) throw new SemanticException("Division by zero");
                    yield li / ri;
                }
                default -> throw new SemanticException("Unknown operator '" + be.op + "'");
            };
        }
        throw new SemanticException("Cannot evaluate expression: " + expr);
    }

    // Helper to get a readable type name from an Object
    private static String typeName(Object v) {
        if      (v instanceof Integer) return "int";
        else if (v instanceof Boolean) return "boolean";
        else if (v instanceof String)  return "string";
        else return v == null ? "null" : v.getClass().getSimpleName();
    }
}
