	.text
	.align	4
	.global	main
	.type	main, %function
main:
	ldr	r0, =mystring
	mov	r1, #15
	MOV	r2, #1
	ldr 	r3, =param2
	bl	myprintf
__mainend:
	mov	r7, #1
	svc	0

	.data
	.align	4
mystring:
	.asciz	"C - %llx."
param2:
	.asciz	"garbage"
