# ❌ ERROR TEST CASES

This file contains test programs that are designed to trigger specific compiler errors. It also describes where the error should occur and what kind of message the user should expect.

---

## 📕 Lexical Errors

### ❗ Unexpected Character

**Input:**
```text
read x
x = 5 $ 3
```

**Expected Output:**
```
Unexpected character at line 2: $
```

---

## 📗 Syntax Errors

### ❗ Missing Closing Parenthesis

**Input:**
```text
if (x {
  print x
}
```

**Expected Output:**
```
Syntax Error: Expected RPAREN but found LBRACE
```

---

## 📘 Semantic Errors

### ❗ Type Mismatch in Expression

**Input:**
```text
read name
name = name + 5
```

**Expected Output:**
```
Semantic Error: Cannot apply operator '+' to types 'string' and 'int'
```

---

### ❗ Non-Boolean Used in Condition

**Input:**
```text
read flag
if (flag) {
  print flag
}
```

**User Input:** `flag = 1`

**Expected Output:**
```
Semantic Error: Variable 'flag' must be boolean (true/false)
```

---

### ❗ Undeclared Variable Used

**Input:**
```text
print y
```

**Expected Output:**
```
Semantic Error: Variable 'y' not declared
```