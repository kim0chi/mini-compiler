# ✅ Sample Test Cases

## 1. Arithmetic Precedence

```text
read a
a = 1 + 2 * 3 - 4 / 2
print a
```

✔️ Output: a = 5

## 2. If / Else Block

```text
read x
if (x) {
  print x
} else {
  x = 10
  print x
}
```

## 3. While Loop

```text
read x
while (x) {
    print x
    x = x - 1
}
```

## 4. Type Mismatch Error

```text
read name
name = name + 5
```

❌ Semantic Error: Cannot apply operator '+' to types 'string' and 'int'


## 5. Boolean Enforcement

```text
read ok
if (ok) {
    print ok
}
```

❌ Input 1 causes: Variable 'ok' must be boolean (true/false)

## 6. Syntax Error

```text
if (x) {
    print x
}
```

❌ Syntax Error: Expected RPAREN but found LBRACE

## 7. Lexical Error

```text
read x
x = 5 $ 2
```

❌ Unexpected character: $ at line 2

## 8. String & Boolean

```text
read name
read flag
print name
print flag
```

✔️ Input: "Alice", true
✔️ Output: name = Alice, flag = true

## 9. Redeclaration Warning

```text
read x
x = 10
x = x + 1
print x
```

⚠️ Warn: Variable 'x' is being reassigned.
✔️ Output: x = 11
