# 📚 Language Features – Mini Compiler

This mini compiler supports a small but powerful subset of language features, designed to demonstrate the stages of compilation from parsing to code generation.

---

## ✅ Data Types

| Type     | Description            | Example Values           |
|----------|------------------------|---------------------------|
| `int`    | Integer numbers         | `0`, `-5`, `42`           |
| `string` | Text wrapped in quotes  | `"hello"`, `"123abc"`     |
| `boolean`| Logical true/false      | `true`, `false`           |

---

## 🧠 Variable Assignment

Assign a value to a variable.

```plaintext
x = 5
name = "Alice"
isStudent = true
````

---

## 📥 Input: `read`

Reads a value from the user.

```plaintext
read age
read isLoggedIn
```

* Boolean variables used in conditions will enforce `true/false` input only.

---

## 📤 Output: `print`

Prints the value of a variable.

```plaintext
print age
print name
```

---

## ➕ Arithmetic Operations

Supports basic arithmetic expressions with correct precedence.

```plaintext
x = 3 + 5
y = x * (2 - 1)
z = (x + y) / 2
```

* Operators: `+`, `-`, `*`, `/`
* Supports integer arithmetic only.

---

## 🔁 Loops: `while`

Repeats a block of code while a condition is true.

```plaintext
while (x) {
    print x
    x = x - 1
}
```

* Condition must be either a boolean or an integer (non-zero = true).

---

## 🔀 Conditionals: `if / else`

Executes blocks based on conditions.

```plaintext
if (isStudent) {
    print name
} else {
    print "Guest"
}
```

---

## 📌 Example Program

```plaintext
read x
x = 3 + 5 * (2 - 1)
print x

if (x) {
    x = x - 1
    print x
} else {
    x = 10
    print x
}

while (x) {
    print x
    x = x - 1
}
```

---

## ⚠️ Error Handling

* Type mismatches are caught (e.g. assigning string to int).
* Boolean-only variables in `if` and `while` enforce `true/false` input.
* Semantic errors include:

  * Using undeclared variables
  * Assigning wrong data types
  * Invalid tokens

---
