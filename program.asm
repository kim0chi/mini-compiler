section .data
  print_fmt: db "%d", 10, 0
  scan_fmt: db "%d", 0
  t4: dd 0
  t5: dd 0
  t6: dd 0
  t7: dd 0
  t8: dd 0
  t9: dd 0
  t10: dd 0
  t12: dd 0
  t11: dd 0
  x: dd 0
  t1: dd 0
  t2: dd 0
  t3: dd 0

section .text
global main
extern printf, scanf
main:
  ; READ x
  lea rdi, [rel scan_fmt]
  lea rsi, [x]
  xor eax, eax
  call scanf
  mov dword [t1], 3
  mov dword [t2], 5
  mov dword [t3], 2
  mov dword [t4], 1
  mov dword [t5], 1
  mov dword [t6], 5
  mov dword [t7], 8
  ; unhandled: x = t7
  ; PRINT x
  lea rdi, [rel print_fmt]
  mov esi, [x]
  xor eax, eax
  call printf
  ; IFZ x GOTO L1
  mov eax, [x]
  cmp eax, 0
  je L1
  mov dword [t8], 1
  ; unhandled: t9 = x - t8
  ; unhandled: x = t9
  ; PRINT x
  lea rdi, [rel print_fmt]
  mov esi, [x]
  xor eax, eax
  call printf
  jmp L2
L1:
  mov dword [t10], 10
  ; unhandled: x = t10
  ; PRINT x
  lea rdi, [rel print_fmt]
  mov esi, [x]
  xor eax, eax
  call printf
L2:
L3:
  ; IFZ x GOTO L4
  mov eax, [x]
  cmp eax, 0
  je L4
  ; PRINT x
  lea rdi, [rel print_fmt]
  mov esi, [x]
  xor eax, eax
  call printf
  mov dword [t11], 1
  ; unhandled: t12 = x - t11
  ; unhandled: x = t12
  jmp L3
L4:
  mov eax, 0
  ret
