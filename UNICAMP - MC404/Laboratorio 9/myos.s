.org 	0x0
      B inicializa			@ RESET 

.org	0x8
      B tratador_syscall

.org	0x18
      B tratador_interrupcao		@ Tratador de interrupcoes 


.org 0x100	
inicializa:

      @ Constantes para os endereços do TZIC	
      @ (não são instruções, são diretivas do montador!)
.set TZIC_BASE, 0x0FFFC000
.set TZIC_INTCTRL, 0x0
.set TZIC_INTSEC1, 0x84 
.set TZIC_ENSET1, 0x104
.set TZIC_PRIOMASK, 0xC
.set TZIC_PRIORITY9, 0x424

.set GPT_CR, 0x53FA0000 
.set GPT_PR, 0x53FA0004
.set GPT_0CR1, 0x53FA0010
.set GPT_IR,0x53FA000C

      MSR     CPSR_c, #0x13       @ SUPERVISOR mode, IRQ/FIQ enabled

      LDR r2, =0xD000
      MOV r3, #0
      LDR r3, [r2]			@ Zera posicao de memoria 0xD000 

      @ Configura GPT conforme enunciado 
      LDR r0, =GPT_CR
      MOV r1,#0x00000041
      STR r1, [r0]

      LDR r0, =GPT_PR
      MOV r1,#0x0
      STR r1, [r0]

      LDR r0, =GPT_0CR1
      MOV r1,#1
      STR r1, [r0]

      LDR r0, =GPT_IR
      MOV r1,#100
      STR r1, [r0]

      @ Liga o controlador de interrupções
      @ R1 <= TZIC_BASE
      ldr	r1, =TZIC_BASE
      @ Configura interrupção 39 do GPT como não segura
      mov	r0, #(1 << 7)
      str	r0, [r1, #TZIC_INTSEC1]
      @ Habilita interrupção 39 (GPT)
      @ reg1 bit 7 (gpt)
      mov	r0, #(1 << 7)
      str	r0, [r1, #TZIC_ENSET1]
      @ Configure interrupt39 priority as 1
      @ reg9, byte 3
      ldr r0, [r1, #TZIC_PRIORITY9]
      bic r0, r0, #0xFF000000
      mov r2, #1
      orr r0, r0, r2, lsl #24
      str r0, [r1, #TZIC_PRIORITY9]
      @ Configure PRIOMASK as 0
      eor r0, r0, r0
      str r0, [r1, #TZIC_PRIOMASK]
      @ Habilita o controlador de interrupções
      mov r0, #1
      str r0, [r1, #TZIC_INTCTRL]

@ Configurable STACK values for each ARM core operation mode
.set SVC_STACK, 0x7800
.set UND_STACK, 0x7900
.set ABT_STACK, 0x7A00
.set IRQ_STACK, 0x7B00
.set FIQ_STACK, 0x7C00
.set USR_STACK, 0x7D00

      @ First configure stacks for all modes
      mov sp, #SVC_STACK 
      msr CPSR_c, #0xDF	@ Enter system mode, FIQ/IRQ disabled
      mov sp, #USR_STACK
      msr CPSR_c, #0xD1	@ Enter FIQ mode, FIQ/IRQ disabled
      mov sp, #FIQ_STACK
      msr CPSR_c, #0xD2	@ Enter IRQ mode, FIQ/IRQ disabled
      mov sp, #IRQ_STACK
      msr CPSR_c, #0xD7	@ Enter abort mode, FIQ/IRQ disabled
      mov sp, #ABT_STACK
      msr CPSR_c, #0xDB	@ Enter undefined mode, FIQ/IRQ disabled
      mov sp, #UND_STACK
      msr CPSR_c, #0x1F	@ Enter system mode, IRQ/FIQ enabled

.set UCR1, 0x53fbc080
.set UCR2, 0x53fbc084
.set UCR3, 0x53fbc088
.set UCR4, 0x53fbc08c
.set UFCR, 0x53fbc090
.set UBIR, 0x53fbc0a4
.set UBMR, 0x53fbc0a8
.set UTXD, 0x53fbc040
.set USR1, 0x53fbc094

      LDR r3, =constantes

      LDR r2, [r3]
      LDR r1, =UCR1
      STR r2, [r1]

      LDR r2, [r3, #4]
      LDR r1, =UCR2
      STR r2, [r1]

      LDR r2, [r3, #8]
      LDR r1, =UCR3
      STR r2, [r1]

      LDR r2, [r3, #12]
      LDR r1, =UCR4
      STR r2, [r1]

      LDR r2, [r3, #16]
      LDR r1, =UFCR
      STR r2, [r1]

      LDR r2, [r3, #20]
      LDR r1, =UBIR
      STR r2, [r1]

      LDR r2, [r3, #24]
      LDR r1, =UBMR
      STR r2, [r1]

@ Enable interruptions, set ARM mode to USR, and jump to 0x8000
      msr	CPSR_c, #0x10
      LDR r4, =0x8000
      BX r4

tratador_interrupcao:

      LDR R0, =0xD000			@ Carrega o endereco de memoria 
      LDR R1, [R0]			@ Carrega valor corntido em 0xD000 
      ADD R1, R1, #1			@ Soma um 

      STR R1, [R0]			@ Armazena valor em 0xD000

      SUB R14, R14, #4			@ LR_irq = PC + 8 -- temos que subtrair 4 
    
      MOVS pc, R14

tratador_syscall:
      
      CMP r7, #4
      BLEQ tratador_write

      B tratador_exit      

tratador_write:
      STMFD sp!, {r4-r11,lr}

      LDR r4, =UTXD
      LDR r5, =USR1
      EOR r6, r6, r6

while:
      CMP r6, r2
      BGE end_while
 
UTXD_wait:
      LDR r7, [r5]
      AND r7, r7, #0x1000
      MOV r7, r7, LSR #12
      CMP r7, #
      BEQ UTXD_wait

      LDRB r8, [r1, r6]
      STRB r8, [r4] 

      ADD r6, r6, #1
      B while

end_while:
      MOV r0, r6
      LDMFD sp!, {r4-r11,lr}
      MOVS pc, lr

tratador_exit:
      B tratador_exit

.data 
constantes:
    .word 0x0001
    .word 0x2127
    .word 0x0704
    .word 0x7C00
    .word 0x089E
    .word 0x08FF
    .word 0x0C34
