package compiler.gui;

import compiler.ast.Program;
import compiler.codegen.ASMGenerator;
import compiler.codegen.TACGenerator;
import compiler.codegen.TACOptimizer;
import compiler.error.SemanticException;
import compiler.error.SyntaxException;
import compiler.lexer.Lexer;
import compiler.lexer.Token;
import compiler.parser.Interpreter;
import compiler.parser.Parser;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // --- Main Window ---
            JFrame frame = new JFrame("Mini‑Compiler IDE");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // --- Source Code Panel ---
            JTextArea codeArea = new JTextArea(20, 60);
            codeArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
            JScrollPane codeScroll = new JScrollPane(codeArea);
            codeScroll.setBorder(BorderFactory.createTitledBorder("Source Code"));

            // --- Tabs for each output stage ---
            JTabbedPane tabs = new JTabbedPane();

            JTextArea outputArea = new JTextArea();
            outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            outputArea.setEditable(false);
            tabs.addTab("Output", new JScrollPane(outputArea));

            JTextArea rawTacArea = new JTextArea();
            rawTacArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            rawTacArea.setEditable(false);
            tabs.addTab("Raw TAC", new JScrollPane(rawTacArea));

            JTextArea optTacArea = new JTextArea();
            optTacArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            optTacArea.setEditable(false);
            tabs.addTab("Optimized TAC", new JScrollPane(optTacArea));

            JTextArea asmArea = new JTextArea();
            asmArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            asmArea.setEditable(false);
            tabs.addTab("Assembly", new JScrollPane(asmArea));

            // --- Compile & Run Button ---
            JButton runBtn = new JButton("Compile & Run");
            runBtn.addActionListener(e -> {
                // Clear previous outputs
                outputArea.setText("");
                rawTacArea.setText("");
                optTacArea.setText("");
                asmArea.setText("");

                try {
                    // 1) Lexing
                    String source = codeArea.getText();
                    List<Token> tokens = Lexer.tokenize(source);

                    // 2) Parsing
                    Program program = new Parser(tokens).parseProgram();

                    // 3) Interpretation (runtime output)
                    List<String> runtimeOut = Interpreter.run(program);
                    runtimeOut.forEach(line -> outputArea.append(line + "\n"));

                    // 4) TAC generation
                    List<String> rawTac = new TACGenerator().generate(program.statements);
                    rawTac.forEach(line -> rawTacArea.append(line + "\n"));

                    // 5) TAC optimization
                    List<String> optTac = new TACOptimizer().optimize(rawTac);
                    optTac.forEach(line -> optTacArea.append(line + "\n"));

                    // 6) Assembly generation
                    List<String> asm = new ASMGenerator(optTac).generate();
                    asm.forEach(line -> asmArea.append(line + "\n"));

                    // Show the runtime output first
                    tabs.setSelectedIndex(0);

                } catch (SyntaxException se) {
                    outputArea.append(
                        "Syntax Error at " + se.line + ":" + se.column + " – " + se.getMessage() + "\n"
                    );
                    tabs.setSelectedIndex(0);
                } catch (SemanticException se) {
                    outputArea.append("Semantic Error – " + se.getMessage() + "\n");
                    tabs.setSelectedIndex(0);
                } catch (Exception ex) {
                    outputArea.append("Unexpected Error – " + ex.getMessage() + "\n");
                    ex.printStackTrace();
                    tabs.setSelectedIndex(0);
                }
            });

            // --- Layout ---
            JPanel controlPanel = new JPanel(new BorderLayout(5,5));
            controlPanel.add(codeScroll, BorderLayout.CENTER);
            controlPanel.add(runBtn, BorderLayout.SOUTH);

            frame.getContentPane().setLayout(new BorderLayout(5,5));
            frame.getContentPane().add(controlPanel, BorderLayout.NORTH);
            frame.getContentPane().add(tabs, BorderLayout.CENTER);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
