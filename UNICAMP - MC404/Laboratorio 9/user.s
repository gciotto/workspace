	.section	.rodata
	.align	2
.LC0:
	.ascii	"Userland code.\012\000"
	.text
	.align	4
	.global	main
main:
	@ args = 0, pretend = 0, frame = 16
	@ frame_needed = 1, uses_anonymous_args = 0
	stmfd	sp!, {fp, lr}
	add	fp, sp, #4
	sub	sp, sp, #16
	ldr	r3, .L3
	sub	ip, fp, #20
	ldmia	r3, {r0, r1, r2, r3}
	stmia	ip, {r0, r1, r2, r3}
	sub	r3, fp, #20
	mov	r0, #1
	mov	r1, r3
	mov	r2, #15
	bl	write
	mov	r0, #0
	bl	exit
	sub	sp, fp, #4
	ldmfd	sp!, {fp, pc}
.L4:
	.align	2
.L3:
	.word	.LC0
