@ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ @
@  main.s
@
@  Chama uma função para multiplicar matrizes. Alguns parâmetros são passados
@  pela pilha.
@ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ @
	.text
	.align	2
	.global	main
	.type	main, %function
main:
	str	r4, [sp, #-4]!
	ldr	r0, =matriz_c
	ldr	r1, =matriz_a
	ldr	r2, =matriz_b
	ldr	r3, =a_linhas
	ldr	r3, [r3]
	ldr	r4, =b_colunas
	ldr	r4, [r4]
	str	r4, [sp, #-4]!
	ldr	r4, =b_linhas
	ldr	r4, [r4]
	str	r4, [sp, #-4]!
	ldr	r4, =a_colunas
	ldr	r4, [r4]
	str	r4, [sp, #-4]!
	bl	mm
	add	sp, sp, #12
	ldr	r4, [sp], #4
__resultado:
	ldr	r0, =matriz_c
	ldr	r1, =a_linhas
	ldr	r1, [r1]
	ldr	r2, =b_colunas
	ldr	r2, [r2]
	bl	imprime_resultado
__termina:
	mov	r7, #1
	svc	0

	.data
	.align	2
a_linhas:
	.word	5
a_colunas:
	.word	5
matriz_a:
	.word	3
	.word	1
	.word	3
	.word	6
	.word	7
	.word	2
	.word	2
	.word	3
	.word	2
	.word	2
	.word	3
	.word	3
	.word	1
	.word	1
	.word	2
	.word	2
	.word	3
	.word	3
	.word	1
	.word	1
	.word	2
	.word	2
	.word	3
	.word	3
	.word	3
b_linhas:
	.word	5
b_colunas:
	.word	5
matriz_b:
	.word	3
	.word	1
	.word	3
	.word	6
	.word	7
	.word	2
	.word	2
	.word	3
	.word	2
	.word	2
	.word	3
	.word	3
	.word	1
	.word	1
	.word	2
	.word	2
	.word	3
	.word	3
	.word	1
	.word	1
	.word	2
	.word	2
	.word	3
	.word	3
	.word	3
matriz_c:
	.fill	10*10,4,0
