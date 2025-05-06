# 🛠 Design Overview

## 📦 Architecture

The project follows a classic compiler pipeline:

```text
Source Code
    ↓ Lexer → Token Stream
    ↓ Parser → AST
    ↓ Semantic Analyzer
    ↓ TAC Generator → Raw 3-address code
    ↓ Optimizer → Optimized TAC
    ↓ Codegen → NASM Assembly
    ↓ Execution (Interpreter)
```

---

## 💡 Key Design Choices

- **LL(1) parser**: Implemented via recursive descent, with proper precedence climbing for expressions
- **AST-based pipeline**: Every stage operates on high-level AST nodes for clarity and modularity
- **Swing GUI**: Allows user input, live compilation, and separate output panes for each stage
- **Type inference**: Dynamically determines variable type during `read` or assignment
- **Error reporting**: All major errors (syntax, semantic, runtime) tracked with line/column positions

## 🧪 Testing Strategy

Each stage is verified through:
- Manual test cases via GUI
- Console output logs for TAC and ASM
- Semantic failure checks (wrong types, undeclared vars, division by zero)

---

## 📚 Dependencies

- Java 17+
- No external libraries (pure Java)