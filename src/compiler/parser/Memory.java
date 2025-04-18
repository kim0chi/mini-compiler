package compiler.parser;

import compiler.error.SemanticException;
import java.util.HashMap;
import java.util.Map;

public class Memory {
    // Variable table (symbol table)
    private static final Map<String, Variable> vars = new HashMap<>();

    public static void store(String name, int value) {
        if (vars.containsKey(name)) {
            System.out.println("Warning: Variable '" + name + "' is being reassigned.");
        }
        vars.put(name, new Variable("int", value));
    }

    public static void store(String name, String value) {
        vars.put(name, new Variable("string", value));
    }

    public static void store(String name, boolean value) {
        vars.put(name, new Variable("boolean", value));
    }

    // Retrieve the integer value of a variable
    public static int getInt(String name) {
        if (!vars.containsKey(name)) {
            throw new RuntimeException("Semantic Error: '" + name + "' used before assignment.");
        }
        return vars.get(name).intValue;
    }

    public static String getString(String name) {
        return vars.get(name).stringValue;
    }

    public static boolean getBoolean(String name) {
        return vars.get(name).boolValue;
    }

    // in compiler.parser.Memory.java

    /** Fetches the Variable object or throws a semantic error if missing */
    public static Variable getVariable(String name) {
        if (!vars.containsKey(name)) {
            throw new SemanticException(
                "Variable '" + name + "' used before assignment."
            );
        }
        return vars.get(name);
    }

    // Optional: For debugging
    public static void printAll() {
        System.out.println("Symbol Table:");
        for (Map.Entry<String, Variable> entry : vars.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
    }

    // In Memory.java (src/compiler/parser/Memory.java), add:
    public static void clear() {
        vars.clear();
    }

}
