package compiler.codegen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Generates x86-64 NASM assembly from three-address code (TAC).
 */
public class ASMGenerator {
    private final List<String> tac;
    private final List<String> asm = new ArrayList<>();
    private final Set<String> variables = new HashSet<>();

    // Patterns for matching TAC formats
    private static final Pattern CONST_PATTERN = Pattern.compile("^t\\d+ = -?\\d+$");
    private static final Pattern BINOP_PATTERN = Pattern.compile("^t\\d+ = t\\d+ [\\+\\-\\*/] t\\d+$");
    private static final Pattern LABEL_PATTERN = Pattern.compile("^L\\d+:");

    public ASMGenerator(List<String> tac) {
        this.tac = tac;
        collectVariables();
    }

    private void collectVariables() {
        for (String line : tac) {
            if (line.startsWith("READ ") || line.startsWith("PRINT ")) {
                String[] parts = line.split(" ");
                variables.add(parts[1]);
            } else if (line.matches("^t\\d+ =.*")) {
                String varName = line.split(" = ")[0];
                variables.add(varName);
            }
        }
    }

    /**
     * Main entry: emits full assembly.
     */
    public List<String> generate() {
        emitPreamble();
        for (String line : tac) {
            emitInstruction(line);
        }
        emitPostamble();
        return asm;
    }

    private void emitPreamble() {
        asm.add("section .data");
        asm.add("  print_fmt: db \"%d\", 10, 0");
        asm.add("  scan_fmt: db \"%d\", 0");
        for (String var : variables) {
            asm.add("  " + var + ": dd 0");
        }
        asm.add("");
        asm.add("section .text");
        asm.add("global main");
        asm.add("extern printf, scanf");
        asm.add("main:");
    }

    private void emitInstruction(String line) {
        if (line.startsWith("READ ")) {
            String var = line.split(" ")[1];
            asm.add("  ; READ " + var);
            asm.add("  lea rdi, [rel scan_fmt]");
            asm.add("  lea rsi, [" + var + "]");
            asm.add("  xor eax, eax");
            asm.add("  call scanf");
        } else if (line.startsWith("PRINT ")) {
            String var = line.split(" ")[1];
            asm.add("  ; PRINT " + var);
            asm.add("  lea rdi, [rel print_fmt]");
            asm.add("  mov esi, [" + var + "]");
            asm.add("  xor eax, eax");
            asm.add("  call printf");
        } else if (CONST_PATTERN.matcher(line).matches()) {
            String[] parts = line.split(" = ");
            asm.add("  mov dword [" + parts[0] + "], " + parts[1]);
        } else if (BINOP_PATTERN.matcher(line).matches()) {
            String[] parts = line.split(" ");
            String dest = parts[0];
            String left = parts[2];
            String op = parts[3];
            String right = parts[4];
            asm.add("  ; " + line);
            asm.add("  mov eax, [" + left + "]");
            if ("+".equals(op)) {
                asm.add("  add eax, [" + right + "]");
            } else if ("-".equals(op)) {
                asm.add("  sub eax, [" + right + "]");
            } else if ("*".equals(op)) {
                asm.add("  imul eax, [" + right + "]");
            } else if ("/".equals(op)) {
                asm.add("  cdq");
                asm.add("  idiv dword [" + right + "]");
            }
            asm.add("  mov [" + dest + "], eax");
        } else if (line.startsWith("IFZ ")) {
            String[] parts = line.split(" ");
            String var = parts[1];
            String label = parts[3];
            asm.add("  ; IFZ " + var + " GOTO " + label);
            asm.add("  mov eax, [" + var + "]");
            asm.add("  cmp eax, 0");
            asm.add("  je " + label);
        } else if (line.startsWith("GOTO ")) {
            String label = line.split(" ")[1];
            asm.add("  jmp " + label);
        } else if (LABEL_PATTERN.matcher(line).matches()) {
            asm.add(line);
        } else {
            asm.add("  ; unhandled: " + line);
        }
    }

    private void emitPostamble() {
        asm.add("  mov eax, 0");
        asm.add("  ret");
    }
}
