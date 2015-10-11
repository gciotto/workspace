.org 	0x0
      B inicializa			/* RESET */

.org	0x18
      B tratador_interrupcao		/* Tratador de interrupcoes */


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
      LDR r3, [r2]			/* Zera posicao de memoria 0xD000 */

      /* Configura GPT conforme enunciado */
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
      MOV r1,#1
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

infinito:
      B infinito			/* Loop infinito */

tratador_interrupcao:

      LDR R0, =0xD000			/* Carrega o endereco de memoria */
      LDR R1, [R0]			/* Carrega valor contido em 0xD000 */
      ADD R1, R1, #1			/* Soma um */

      STR R1, [R0]			/* Armazena valor em 0xD000*/

      SUB R14, R14, #4			/* LR_irq = PC + 8 -- temos que subtrair 4 */
    
      MOVS pc, R14