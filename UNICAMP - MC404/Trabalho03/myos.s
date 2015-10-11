/*		I	F	T    |>    		PROC. MODE	<|
Bits  8		7	6	5	4	3	2	1	0
					1	0	0	1	1	supervisor mode
					1	1	1	1	1	system mode
*/

/* Enderecos dos registradores de harware */
/* GPT */
.set GPT_CR, 	0x53FA0000	/* GPT Control Register */
.set GPT_OCR1, 	0x53FA0010 	/* GPT Output Compare Register 1 */
.set GPT_IR, 	0x53FA000C 	/* GPT Interrupt Register */
.set GPT_SR,	0x53FA0008
.set GPT_PR, 	0x53FA0004

/* TZIC */
.set TZIC_BASE, 	0x0FFFC000
.set TZIC_INTCTRL, 	0x0
.set TZIC_INTSEC1, 	0x84 
.set TZIC_ENSET1, 	0x104
.set TZIC_PRIOMASK, 	0xC
.set TZIC_PRIORITY9, 	0x424

/* Configurable STACK values for each ARM core operation mode */
.set SVC_STACK, 0x10800
.set UND_STACK, 0x7900
.set ABT_STACK, 0x7A00
.set IRQ_STACK, 0x7B00
.set FIQ_STACK, 0x7C00
.set USR_STACK, 0x11000

/* UART */
.set UCR1, 0x53fbc080
.set UCR2, 0x53fbc084
.set UCR3, 0x53fbc088
.set UCR4, 0x53fbc08c
.set UFCR, 0x53fbc090
.set UBIR, 0x53fbc0a4
.set UBMR, 0x53fbc0a8
.set UTXD, 0x53fbc040
.set USR1, 0x53fbc094

/* Constantes */
.set COUNT, 20

.org 0x00
    B reset

.org 0x08
    B SVC_interruption

/* Interrupcao IRQ */
.org 0x018
    B IRQ_interruption

.org 0x30

reset:
    /* Inicializar GPT - 100 milisegundos -- Interrupcao */
    MSR CPSR_c, #0x13 		/* SUPERVISOR mode, IRQ/FIQ enabled */

    /* Configuracao GPT */
    /* 1 -	Você deve escrever no registrador GPT_CR (control register) o valor 0x00000041 que irá habilitá-lo e configurar o clock_src para periférico. 
		Isto significa que o contador irá contar a cada ciclo do relógio dos periféricos do sistema. Note que o relógio (clock) do processador é muito mais alto (~1GHz) do 
		que o relógio dos periféricos (~200MHz).
    */
    LDR r0, =GPT_CR
    MOV r1, #0x00000041
    STR r1, [r0]

    LDR r0, =GPT_PR
    MOV r1,#0x0
    STR r1, [r0]

    /* 2 - 	Mantenha o prescaler zerado e coloque em GPT_OCR1 o valor que você deseja contar. Quando o comparador do GPT determinar que a contagem 
		se igualou ao conteúdo de GPT_OCR1, uma interrupção to tipo Output Compare Channel 1 será gerada.
    */
    LDR r0, =GPT_OCR1
    MOV r1, #100
    STR r1, [r0]

    /* 3 - 	Para demonstrar interesse nessa interrupção específica do GPT, grave 1 no registrador GPT_IR. Isto irá habilitar a 
		interrupção Output Compare Channel 1, que inicia-se desligada.
    */
    LDR r0, =GPT_IR
    MOV r1, #1
    STR r1, [r0]

    /* Configuracao TZIC */

    /* Liga o controlador de interrupções */
    /* R1 <= TZIC_BASE */
    LDR	r1, =TZIC_BASE

    /* Configura interrupção 39 do GPT como não segura */
    MOV	r0, #(1 << 7)
    STR	r0, [r1, #TZIC_INTSEC1]

    /* Habilita interrupção 39 (GPT) */
    /* reg1 bit 7 (gpt) */
    MOV	r0, #(1 << 7)
    STR	r0, [r1, #TZIC_ENSET1]

    /* Configure interrupt39 priority as 1 */
    /* reg9, byte 3 */
    LDR r0, [r1, #TZIC_PRIORITY9]
    BIC r0, r0, #0xFF000000
    MOV r2, #1
    ORR r0, r0, r2, lsl #24
    STR r0, [r1, #TZIC_PRIORITY9]

    /* Configure PRIOMASK as 0 */
    EOR r0, r0, r0
    STR r0, [r1, #TZIC_PRIOMASK]

    /* Habilita o controlador de interrupções */
    MOV	r0, #1
    STR	r0, [r1, #TZIC_INTCTRL]

    @ First configure stacks for all modes
    MOV sp, #SVC_STACK 
    MSR CPSR_c, #0xDF	@ Enter system mode, FIQ/IRQ disabled
    MOV sp, #USR_STACK
    MSR CPSR_c, #0xD1	@ Enter FIQ mode, FIQ/IRQ disabled
    MOV sp, #FIQ_STACK
    MSR CPSR_c, #0xD2	@ Enter IRQ mode, FIQ/IRQ disabled
    MOV sp, #IRQ_STACK
    MSR CPSR_c, #0xD7	@ Enter abort mode, FIQ/IRQ disabled
    MOV sp, #ABT_STACK
    MSR CPSR_c, #0xDB	@ Enter undefined mode, FIQ/IRQ disabled
    MOV sp, #UND_STACK
    MSR CPSR_c, #0x1F	@ Enter system mode, IRQ/FIQ enabled

    /* Configuracao UART - Passos do Datasheet */

    LDR r3, =constraints         
                                 
    LDR r2, [r3]                       /* 1. 	UCR1 = 0x0001 */
    LDR r1, =UCR1                      /* 	Enable the UART. */
    STR r2, [r1]                       
                                       
    LDR r2, [r3, #4]                   /* 2. 	UCR2 = 0x2127 */
    LDR r1, =UCR2                      /* 	Set hardware flow control, data format and enable transmitter and receiver. */
    STR r2, [r1]                       
                                       
    LDR r2, [r3, #8]                   /* 3. 	UCR3 = 0x0704 */
    LDR r1, =UCR3                      /*	Set UCR3[RXDMUXSEL] = 1. */
    STR r2, [r1]                       
                                       
    LDR r2, [r3, #12]                  /* 4. 	UCR4 = 0x7C00 */
    LDR r1, =UCR4                      /*	Set CTS trigger level to 31, */
    STR r2, [r1]                       
                                       
    LDR r2, [r3, #16]                  /* 5. 	UFCR = 0x089E */
    LDR r1, =UFCR                      /*	Set internal clock divider = 5 (divide input uart clock by 5). So the reference clock is */
    STR r2, [r1]                       /*	100MHz/5 = 20MHz. */
                                       /*	Set TXTL = 2 and RXTL = 30. */
    LDR r2, [r3, #20]                  /* 6. 	UBIR = 0x08FF */
    LDR r1, =UBIR                      /* 7. 	UBMR = 0x0C34 */
    STR r2, [r1]                       /*	In the above two steps, set baud rate to 921.6Kbps based on the 20MHz reference */
                                       /*	clock. */
    LDR r2, [r3, #24]
    LDR r1, =UBMR
    STR r2, [r1]


    MSR CPSR_c, #0b10010

    BL refresh_stacks_addresses

    MSR CPSR_c, #0b10000

    LDR r0, =0x850c
    BX r0

change_pid_process:
    STMFD sp!, {r4-r11, lr}

    LDR r4, =current_pid
    LDR r4, [r4]
    
    MOV r5, r4			/* Parent process */
    
    STMFD sp!, {r0, r1}

__while_change_pid_process:
    ADD r4, r4, #1   		/* Checa novo pid */

    CMP r4, #9			/* Checa se eh maior que 8. */
    MOVGE r4, #1

    MOV r1, r4
    BL get_pid_status		/* Checa o status do pid. Verifica se o numero esta livre */

    CMP r0, #1			/* Processo esta livre */
    BEQ __end_while_change_pid_process
    
    B __while_change_pid_process

__end_while_change_pid_process:

    LDR r5, =current_pid
    STR r4, [r5]
    
    LDMFD sp!, {r0, r1}

    LDMFD sp!, {r4-r11, lr}	/* Retorna estado antes da chamada */
    MOV pc, lr

IRQ_interruption:

    BL disable_gpt

    STR r5, [sp, #-4]!
    STR r6, [sp, #-4]!

    LDR r5, =GPT_SR
    MOV r6, #1
    STR r6, [r5]

    LDR r6, [sp], #4
    LDR r5, [sp], #4

    STR r4, [sp, #-4]!
    LDR r4, =lr_irq		/* lr = pc + 8 */
    STR lr, [r4]
    LDR r4, [sp], #4

    BL disable_gpt

    BL save_context_of_process

    BL change_pid_process
    
    BL refresh_stacks_addresses    

    BL load_context_of_process

    BL enable_gpt

    SUB lr, r8, #4
    MOVS pc,lr

refresh_stacks_addresses:
    STMFD sp!, {r4-r11, lr}

    MSR CPSR_c,  #0b11111	/* Enter system mode */

    LDR r4, =current_pid
    LDR r4, [r4]
    SUB r4, r4, #1

    MOV r5, #0x1000
    MUL r6, r5, r4

    LDR r7, =stack_base
    LDR r7, [r7]
    SUB r8, r7, r6

    MOV sp, r8

    MSR CPSR_c, #0b10011 	/* Enter svc mode */

    SUB r8, r8, #0x800

    MOV sp, r8

    MSR CPSR_c,  #0b10010	/* Enter IRQ mode */

    LDMFD sp!, {r4-r11, lr}
    MOV pc, lr

SVC_interruption:
    MOV r8, lr

    STR r4, [sp, #-4]!
    STR r5, [sp, #-4]!

    LDR r4, =lr_irq		/* lr = pc + 4 */
    ADD r5, lr, #4		/* lr = pc + 8 */
    STR r5, [r4]

    LDR r5, [sp], #4
    LDR r4, [sp], #4
  
    CMP r7, #4			/* Syscall WRITE */
    BLEQ syscall_write

    CMP r7, #1			/* Syscall EXIT */
    BLEQ syscall_exit

    CMP r7, #2			/* Syscall FORK */
    BLEQ syscall_fork

    CMP r7, #3			/* Syscall READ */
    BLEQ syscall_read

    CMP r7, #20			/* Syscall READ */
    BLEQ get_pid

    MOV lr, r8
    MOVS pc, lr			/* Retorna para syscalls.s */

enable_gpt:
    STMFD sp!, {r4-r11, lr}

    LDR r8, =GPT_CR
    LDR r9, [r8]		/* Habilita interrupcoes do GPT */
    ORR r9, r9, #1
    STR r9, [r8]

    LDMFD sp!, {r4-r11, lr}
    MOV pc, lr

disable_gpt:
    STMFD sp!, {r4-r11, lr}

    LDR r8, =GPT_CR
    LDR r9, [r8]		/* Desabilita interrupcoes do GPT */
    LDR r7, =0xFFFFFFFE
    AND r9, r9, r7
    STR r9, [r8]

    LDMFD sp!, {r4-r11, lr}
    MOV pc, lr

syscall_write:
    STMFD sp!, {r4-r11,lr}

    BL disable_gpt

    LDR r4, =UTXD		/* Recupera registradores da UART */
    LDR r5, =USR1
    EOR r6, r6, r6		/* Zera contador */

__while_syscall_write:
    CMP r6, r2
    BGE __end_syscall_write

__UTXD_wait:
    LDR r7, [r5]		/* Recupera estado da UART */
    AND r7, r7, #0x1000		/* Checa 13o byte para verificar se podemos escrever */
    MOV r7, r7, LSR #12
    CMP r7, #1
    BEQ __UTXD_wait		/* Repete ate nao estiver disponivel */

    LDRB r8, [r1, r6]		/* Escreve */
    STRB r8, [r4] 

    ADD r6, r6, #1		/* Aumenta contador */
    B __while_syscall_write

__end_syscall_write:
    
    BL enable_gpt
    MOV r0, r6
        
    LDMFD sp!, {r4-r11,lr}	/* Retorna estado antes da chamada */
    MOV pc, lr

syscall_exit:
    STMFD sp!, {r4-r11,lr}

    LDR r4, =current_pid
    LDR r4, [r4]

    STMFD sp!, {r0, r1}
    
    MOV r0, r4
    BL set_pid_status_stopped

    BL reset_data

    LDMFD sp!, {r0, r1}

    SUB r4, r4, #1
    LDR r6, =stack_base
    MOV r8, #0x1000
    MUL r9, r8, r4
    SUB r6, r6, r9
    SUB r7, r6, #0x800
    MOV r0, r7    

    LDMFD sp!, {r4-r11,lr}	/* Retorna estado antes da chamada */
    MOV sp, r0
    MOV pc, lr    

reset_data:
    STMFD sp!, {r4-r11,lr}

    LDR r4, =current_pid
    LDR r4, [r4]

    SUB r4, r4, #1

    LDR r5, =context_array
    MOV r8, #80
    MUL r9, r8, r4
    ADD r5, r5, r9
    
    LDR r6, =stack_base
    MOV r8, #0x1000
    MUL r9, r8, r4
    SUB r6, r6, r9
    SUB r1, r6, #0x800

    LDR r7, =stacks_svc_tops
    LDR r8, =stacks_user_tops
    STR r1, [r7, r4, LSL #2]
    STR r6, [r8, r4, LSL #2]

    MOV r8, #0

    STR r8, [r5]		/*r0*/
    STR r8, [r5, #4]		/*r1*/
    STR r8, [r5, #8]		/*r2*/
    STR r8, [r5, #12]		/*r3*/
    STR r8, [r5, #16]		/*r4*/
    STR r8, [r5, #20]		/*r5*/
    STR r8, [r5, #24]		/*r6*/
    STR r8, [r5, #28]		/*r7*/
    STR r8, [r5, #32]		/*r8*/
    STR r8, [r5, #36]		/*r9*/
    STR r8, [r5, #40]		/*r10*/
    STR r8, [r5, #44]		/*r11*/
    STR r8, [r5, #48]		/*r12*/
    STR r6, [r5, #52]		/*sp*/
    STR r8, [r5, #56]		/* lr */
    STR r8, [r5, #60]		/* nada */
    STR r8, [r5, #66]		/* nada */

    MOV r8, #0b10000		/* USer mode */
    STR r8, [r5, #64]		/* CPSR */

    LDMFD sp!, {r4-r11,lr}	/* Retorna estado antes da chamada */
    MOV pc, lr

load_context_of_process:
  
    STMFD sp!, {r4-r11, lr}

    LDR r0, =current_pid
    LDR r0, [r0]
    SUB r0, r0, #1

    LDR r1, =stacks_svc_tops
    MSR CPSR_c, #0b10011
    STR sp, [r1, r0, LSL #2]
    MSR CPSR_c, #0b10010

    MOV r1, #COUNT
    MUL r2, r0, r1

    LDR r0, =context_array
    ADD r0, r0, r2

    LDR r3, [r0, #12]
    LDR r4, [r0, #16]
    LDR r5, [r0, #20]
    LDR r6, [r0, #24]
    LDR r7, [r0, #28]
    LDR r8, [r0, #32]
    LDR r9, [r0, #36]
    LDR r10, [r0, #40]
    LDR r11, [r0, #44]
    LDR r12, [r0, #48]

    /* Enter system mode */
    MSR CPSR_c, #0x1F		@ Enter system mode

    LDR r13, [r0, #52]
    LDR r14, [r0, #56]

    MSR CPSR_c, #0x12		/* IRQ mode */

    LDR r2, [r0, #64]
    MSR SPSR, r2

    STMFD sp!, {r4-r11}
    
    LDR r1, =current_pid
    LDR r1, [r1]
    SUB r1, r1 ,#1
    
    MSR CPSR, #0b11111		/* System mode */
    LDR r2, =stacks_user_tops
    LDR sp, [r2, r1, LSL #2]
    MSR CPSR, #0b10010

    LDR r0, =context_array
    MOV r2, #80
    MUL r4, r1, r2

    ADD r4, r0, r4

    LDR r0, [r4]
    LDR r1, [r4, #4]
    LDR r2, [r4, #8]

    LDMFD sp!, {r4-r11}
    
    LDMFD sp!, {r4-r11, lr}
    MOV pc, lr  

/* Saves the context of current process */
save_context_of_process:
    STMFD sp!, {r4-r11,lr}	/* Guarda registradores callee-save */

    STMFD sp!, {r0, r1, r2}	/* Guarda parametros momentaneamente */
  
    LDR r1, =current_pid	/* Recupera o processo atual */
    LDR r0, [r1]

    SUB r0, r0, #1
    MOV r1, #80
    MUL r2, r0, r1

    LDR r0, =context_array	/* End. Contexto =  End. Contexto_Base + CURRENT_PID*80 */
    ADD r0, r0, r2		/* Recupera endereco do vetor de contexto */

    STR r3, [r0, #12]
    STR r4, [r0, #16]
    STR r5, [r0, #20]
    STR r6, [r0, #24]		/* Salva registradores comuns: */
    STR r7, [r0, #28]				/* r3 - r12 */
    STR r8, [r0, #32]
    STR r9, [r0, #36]
    STR r10, [r0, #40]
    STR r11, [r0, #44]
    STR r12, [r0, #48]

    MRS r2, SPSR		/* Salva CPSR do USUARIO */
    STR r2, [r0, #64]

    MRS r6, CPSR		/* Guarda momentaneamente o estado do MODO ATUAL */

    LDR r1, =lr_irq		/* lr_irq possui o valor de lr = pc + 8 */
    LDR r1, [r1]

    SUB r1, r1, #4

    STR r1, [r0, #56] 		/* Salva lr do USUARIO */

    MSR CPSR, #0b10011
    STR lr, [r0, #68]		/* Salva lr do MODO SUPERVISOR */
    
    MSR CPSR, r6		/* Volta para o MODO ATUAL */

    LDMFD sp!, {r0, r1, r2}

    LDR r3, =current_pid	/* Novamente recupera PID ATUAL */
    LDR r3, [r3]

    SUB r3, r3, #1
    MOV r4, #80
    MUL r5, r3, r4		/* r5 = (CURRENT_PID-1)*80 */

    LDR r3, =context_array
    ADD r3, r3, r5		/* r3(endereco do vetor de contexto) = Vetor_base + (CURRENT_PID-1)*80 */

    /* Enter system mode */
    MSR CPSR_c, #0x1F		@ Enter SYSTEM mode, FIQ/IRQ disabled

    STR r13, [r3, #52]		/* Guarda SP do modo USUARIO */

    LDR r4, =current_pid
    LDR r4, [r4]
    SUB r4, r4, #1

    LDR r5, =stacks_user_tops
    STR sp, [r5, r4, LSL #2]	/* Guarda SP do modo USUARIO na MEMORIA */

    STR r0, [r3]		/*Salva r0-r2 do USUARIO */
    STR r1, [r3, #4]
    STR r2, [r3, #8]

    AND r6, r6, #0x1F		/* Verifica MODO ATUAL */

    CMP r6, #0b10011
    MSREQ CPSR_c, #0b10011 	/* SVC mode */

    CMP r6, #0b10010
    MSREQ CPSR_c, #0b10010	@ Enter IRQ mode, FIQ/IRQ disabled

    LDMFD sp!, {r4-r11,lr}	/* Retorna estado antes da chamada */
    MOV pc, lr    

syscall_read:
    STMFD sp!, {r4-r11,lr}
    /* FAZ NADA - Enunciado */
    LDMFD sp!, {r4-r11,lr}	/* Retorna estado antes da chamada */
    MOV pc, lr    

/* PID number :- r0 */
set_pid_status_stopped:

    STMFD sp!, {r4-r11,lr}

    LDR r8, =status_pid
    LDR r5, [r8]

    SUB r6, r0, #1

    MOV r7, #1

    MOV r7, r7, LSL r6
    MVN r7, r7

    AND r5, r5, r7

    STR r5, [r8]

    LDMFD sp!, {r4-r11,lr}	/* Retorna estado antes da chamada */
    MOV pc, lr

/* PID number :- r0 */
set_pid_status_running:

    STMFD sp!, {r4-r11,lr}

    LDR r8, =status_pid
    LDR r5, [r8]

    SUB r6, r0, #1

    MOV r7, #1

    MOV r7, r7, LSL r6

    ORR r5, r5, r7

    STR r5, [r8]

    LDMFD sp!, {r4-r11,lr}	/* Retorna estado antes da chamada */
    MOV pc, lr

/* PID number :- r1 */
/* Exit :- r0 */
get_pid_status:
    STMFD sp!, {r4-r11,lr}

    LDR r5, =status_pid
    LDR r5, [r5]

    SUB r6, r1, #1

    MOV r7, r5, LSR r6

    AND r7, r7, #0x0001

    MOV r0, r7

    LDMFD sp!, {r4-r11,lr}	/* Retorna estado antes da chamada */
    MOV pc, lr

syscall_fork:
    STMFD sp!, {r4-r11,lr}

    STMFD sp!, {r0-r3}

    BL save_context_of_process

    LDMFD sp!, {r0-r3}

    LDR r4, =current_pid
    LDR r4, [r4]

    MOV r6, r4

    MOV r5, r4			/* Parent process */    

__while_syscall_fork:
    ADD r4, r4, #1   		/* Checa novo pid */

    CMP r4, #9			/* Checa se eh maior que 8. */
    MOVGE r4, #1

    CMP r4, r6
    BEQ __err_while_syscall_fork

    MOV r1, r4
    BL get_pid_status		/* Checa o status do pid. Verifica se o numero esta livre */

    CMP r0, #0			/* Processo esta livre */
    BEQ __end_while_syscall_fork
    
    B __while_syscall_fork

__end_while_syscall_fork:

    MOV r0, r4
    BL set_pid_status_running	/* Seta novo processo para RUNNING */    
    
    MOV r0, r5			/* FROM */
    MOV r1, r4			/* TO */
    STMFD sp!, {r0, r1}
    BL copy_context_array_from_to
    LDMFD sp!, {r0, r1}

    STMFD sp!, {r0, r1}
    BL copy_stack_from_to	/* r0 : from r1 : to */
    LDMFD sp!, {r0, r1}

    MOV r0, r4
    MOV r1, #0
    LDMFD sp!, {r4-r11,lr}	/* Retorna estado antes da chamada */
    MOV pc, lr

__err_while_syscall_fork:
    LDR r0, =0xFFFFFFFF
    MOV r1, #0
    LDMFD sp!, {r4-r11,lr}	/* Retorna estado antes da chamada */
    MOV pc, lr

/* FROM : r0
    TO  : r1 */
copy_context_array_from_to:
    STMFD sp!, {r4-r11, lr}

    EOR r4, r4, r4
    MOV r8, #80

    SUB r0, r0, #1
    MUL r9, r0, r8
    MOV r0, r9

    SUB r1, r1, #1
    MUL r9, r1, r8
    MOV r1, r9

    LDR r7, =context_array
    ADD r5, r7, r0		/* FROM */
    ADD r6, r7, r1		/* TO */

__while_copy:
    CMP r4, #COUNT
    BEQ __end_copy

    LDR r8, [r5, r4, LSL #2]
    STR r8, [r6, r4, LSL #2]

    ADD r4, r4, #1
    B __while_copy
   
__end_copy:

    LDMFD sp!, {r4-r11, lr}	/* Retorna estado antes da chamada */
    MOV pc, lr

save_tops:
    STMFD sp!, {r4-r11, lr}
    
    LDR r4, =stacks_user_tops
    LDR r6, =stacks_user_tops
    SUB r0, r0, #1
    SUB r1, r1, #1

    MSR CPSR_c, #0b10000	/* User mode */
    MOV r5, sp
    MSR CPSR_c, #0b10011	/* Supervisor mode */

    STR r5, [r4, r0, LSL #2]
    STR r5, [r4, r1, LSL #2]

    STR sp, [r6, r0, LSL #2]
    STR sp, [r6, r1, LSL #2]

    LDMFD sp!, {r4-r11, lr}	/* Retorna estado antes da chamada */
    MOV pc, lr

/* FROM : r0
    TO  : r1 */
copy_stack_from_to:
    STMFD sp!, {r4-r11, lr}

    STMFD sp!, {r0, r1}
    BL save_tops
    LDMFD sp!, {r0, r1}

    LDR r7, =stack_base
    LDR r7, [r7]

    STR r0, [sp, #-4]!
    MUL r9, r0, r8
    MOV r0, r9
    SUB r10, r7, r0
    LDR r0, [sp], #4

    SUB r0, r0, #1
    MUL r9, r0, r8
    MOV r0, r9

    SUB r1, r1, #1
    MUL r9, r1, r8
    MOV r1, r9

    SUB r5, r7, r0		/* FROM */
    SUB r6, r5, #0x800
    SUB r8, r7, r1		/* TO */
    SUB r9, r8, #0x800		

__while_copy_stack_user:
    CMP r5, r6
    BLO __while_copy_stack_svc

    LDR r4, [r5], #-4
    STR r4, [r8], #-4

    B __while_copy_stack_user

__while_copy_stack_svc:

    CMP r6, r10
    BLO __end_copy_stack

    LDR r4, [r6], #-4
    STR r4, [r9], #-4

    B __while_copy_stack_svc
   
__end_copy_stack:

    LDMFD sp!, {r4-r11, lr}	/* Retorna estado antes da chamada */
    MOV pc, lr
    
/* Retorna o PID do processo atual em r0 */
get_pid:
    STMFD sp!, {r4-r11, lr}

    LDR r4, =current_pid
    LDR r0, [r4]
    
    LDMFD sp!, {r4-r11, lr}	/* Retorna estado antes da chamada */
    MOV pc, lr

constraints:
	    .word 0x0001
	    .word 0x2127
	    .word 0x0704
	    .word 0x7C00
	    .word 0x089E
	    .word 0x08FF
	    .word 0x0C34

status_pid: .word 0x0000001

stack_base: .word 0x11000

stacks_svc_tops:
	    .word 0x10800
	    .word 0xF800
	    .word 0xE800
	    .word 0xD800
	    .word 0xC800
	    .word 0xB800
	    .word 0xA800
	    .word 0x9800

stacks_user_tops:
	    .word 0x10100
	    .word 0x10000
	    .word 0xF000
	    .word 0xE000
	    .word 0xD000
	    .word 0xC000
	    .word 0xB000
	    .word 0xA000

context_array:
	    .fill COUNT, 4, 0x0
	    .fill COUNT, 4, 0x0
	    .fill COUNT, 4, 0x0
	    .fill COUNT, 4, 0x0
	    .fill COUNT, 4, 0x0
	    .fill COUNT, 4, 0x0
	    .fill COUNT, 4, 0x0
	    .fill COUNT, 4, 0x0	
lr_irq:	
	    .word 0x0

current_pid:.word 0x1
