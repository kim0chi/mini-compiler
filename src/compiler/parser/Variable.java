package compiler.parser;

public class Variable {
    public final String type; // "int", "string", "boolean"
    public int intValue;
    public String stringValue;
    public boolean boolValue;

    public Variable(String type, int intValue) {
        this.type = type;
        this.intValue = intValue;
    }

    public Variable(String type, String stringValue) {
        this.type = type;
        this.stringValue = stringValue;
    }

    public Variable(String type, boolean boolValue) {
        this.type = type;
        this.boolValue = boolValue;
    }

    @Override
    public String toString() {
        return switch (type) {
            case "int" -> "int = " + intValue;
            case "string" -> "string = \"" + stringValue + "\"";
            case "boolean" -> "boolean = " + boolValue;
            default -> type + " (unknown)";
        };
    }
}
