	.file	"arq2.c"
	.section	.rodata
.LC0:
	.string	"Estou no arquivo 2!"
	.text
.globl funcao
	.type	funcao, @function
funcao:
	pushl	%ebp
	movl	%esp, %ebp
	subl	$24, %esp
	movl	$.LC0, (%esp)
	call	puts
	leave
	ret
	.size	funcao, .-funcao
	.ident	"GCC: (Ubuntu/Linaro 4.5.2-8ubuntu4) 4.5.2"
	.section	.note.GNU-stack,"",@progbits
