		  .globl main
	main:	  ldr r0, entrada1
		  bl code
		  str r0, entrada1
	resultado1:
		  ldr r0, entrada2
		  bl decode
		  str r0, entrada1
		  str r1, entrada2
	resultado2:
		  mov r7, #1
	svc 0x0
	entrada1:	 .word 0x00
	entrada2:	 .word 0x00

	ponteiro:	 .word 0x00
	p1	:	 .word 0x00
	p2	:	 .word 0x00
	p3	:	 .word 0x00
	original:	 .word 0x00
	.align 4
	
	recupera:
		  MOV r6, r0, LSR #0x3	@ Recupera d1
		  
		  BIC r5, r0, #0x0B	@ Recupera d2
		  MOV r7, r5, LSR #0x2

		  BIC r5, r0, #0x0D	@ Recupera d3
		  MOV r8, r5, LSR #0x1

		  BIC r9, r0, #0x0E	@ Recupera d4
	    
		  MOV pc, lr

	calcula_paridade:

		  EOR r3, r6, r7	@ Calculo p1
		  EOR r2, r3, r9
		  STR r2, p1

		  EOR r4, r6, r8	@ Calculo p2
		  EOR r3, r4, r9
		  STR r3, p2

		  EOR r5, r7, r8	@ Calculo p3
		  EOR r4, r5, r9
		  STR r4, p3

		  MOV pc, lr

	codifica_numero:

		  MOV r1, #0

		  LDR r2, p1
		  ADD r0, r1, r2, LSL #6
		  MOV r1, r0

		  LDR r2, p2
		  ADD r0, r1, r2, LSL #5
		  MOV r1, r0
  
		  ADD r0, r1, r6, LSL #4
		  MOV r1, r0

		  LDR r2, p3
		  ADD r0, r1, r2, LSL #3
		  MOV r1, r0

		  ADD r0, r1, r7, LSL #2
		  MOV r1, r0

		  ADD r0, r1, r8, LSL #1
		  MOV r1, r0

		  ADD r0, r1, r9		  

		  MOV pc, lr

	code:
		  STR lr, ponteiro
		  BL recupera
		  BL calcula_paridade
		  BL codifica_numero

		  LDR pc, ponteiro

	recupera_paridade:
		  AND r2, r0, #0x40
		  MOV r3, r2, LSR #0x06
		  STR r3, p1

		  AND r2, r0, #0x20
		  MOV r3, r2, LSR #0x05
		  STR r3, p2

		  AND r2, r0, #0x08
		  MOV r3, r2, LSR #0x03
		  STR r3 , p3

		  MOV pc, lr
	
	recupera_original:
		  AND r3, r0, #0x07
		  MOV r4, r0, LSR #1
		  AND r5, r4, #0x08
		  ADD r7, r3, r5   
		  STR r7, original @ valor original

		  MOV pc, lr

	calcula_erro:
		  EOR r4, r3, r1
		  EOR r3, r4, r2
		  EOR r4, r3, r9
		
		  MOV pc, lr
	
	atualiza_parimento:
		  ORR r3, r5, r4
		  MOV r5, r3
		  
		  MOV pc, lr

	decode:
		  STR lr, ponteiro		  


		  BL recupera_paridade 
		  BL recupera_original
	      
		  LDR r0, original

		  BL recupera

		  MOV r5, #0x0

		  LDR r3, p1
		  MOV r1, r6
		  MOV r2, r7
		  BL calcula_erro
		  
		  BL atualiza_parimento
		    
  		  LDR r3, p2
		  MOV r2, r8
		  BL calcula_erro

		  BL atualiza_parimento

		  LDR r3, p3
		  MOV r1, r7
		  BL calcula_erro

		  BL atualiza_parimento

		  MOV r1, r5

		  LDR pc, ponteiro