package compiler.codegen;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Performs simple optimization passes on three-address code (TAC).
 * Currently implements constant folding for temporaries.
 */
public class TACOptimizer {
    // Pattern for direct constant assignment: tX = 123
    private static final Pattern CONST_ASSIGN = Pattern.compile("(t\\d+) = (\\d+)");
    // Pattern for binary operations: tX = op1 + op2 (or -, *, /)
    private static final Pattern BINARY_OP = Pattern.compile("(t\\d+) = (\\w+) ([+\\-*/]) (\\w+)");

    /**
     * Optimize the given TAC by performing constant folding.
     * @param code List of TAC instructions
     * @return Optimized list of TAC instructions
     */
    public static List<String> optimize(List<String> code) {
        Map<String, Integer> constants = new HashMap<>();
        List<String> optimized = new ArrayList<>();

        for (String instr : code) {
            Matcher mConst = CONST_ASSIGN.matcher(instr);
            if (mConst.matches()) {
                String temp = mConst.group(1);
                int value = Integer.parseInt(mConst.group(2));
                // Record the constant for this temporary
                constants.put(temp, value);
                optimized.add(instr);
                continue;
            }

            Matcher mBin = BINARY_OP.matcher(instr);
            if (mBin.matches()) {
                String temp = mBin.group(1);
                String left = mBin.group(2);
                String op = mBin.group(3);
                String right = mBin.group(4);
                if (constants.containsKey(left) && constants.containsKey(right)) {
                    int l = constants.get(left);
                    int r = constants.get(right);
                    int result;
                    switch (op) {
                        case "+" -> result = l + r;
                        case "-" -> result = l - r;
                        case "*" -> result = l * r;
                        case "/" -> result = l / r;
                        default -> result = 0;
                    }
                    // Replace with a constant assignment
                    String folded = temp + " = " + result;
                    constants.put(temp, result);
                    optimized.add(folded);
                } else {
                    optimized.add(instr);
                }
                continue;
            }

            // Default: keep instruction unchanged
            optimized.add(instr);
        }

        return optimized;
    }
}
