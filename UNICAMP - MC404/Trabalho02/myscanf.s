      .text
      .align 4
      .global	myscanf
      .type	myscanf, %function

myscanf:
    STMFD sp!, {r3}
    STMFD sp!, {r2}
    STMFD sp!, {r1}
    LDR r3, =parametros
    STR sp, [r3]			/* Empilha topo da pilha */
    MOV r11, sp

    STMFD sp!, {r1-r11, lr} 		/* Guarda todos antigos registradores */
    
while_myscanf:
    BL read_char			/* Le caracter da cadeia de string */
    CMP r4, #0
    BEQ end_scanf

    CMP r4, #'%'
    BEQ lenght

just_restart:
    BL increment_counter
    B while_myscanf

lenght:
    BL increment_counter		/* Le tamanho */
    BL read_char

    MOV r3, #4				/* R3 = LENGHT = 32bits = 4bytes */
    MOV r5, #1
    MOV r8, #4

while_lenght:
    
    CMP r4, #'h'			/* Compara com 'h' */
    MOVEQ r3,r3, LSR #1
    BEQ lenght_continue

    CMP r4, #'l'			/* Compara com 'l' */
    MULEQ r3, r5, r8
    ADDEQ r5, r5, #1
    BEQ lenght_continue
    
    BNE mask

lenght_continue:
    BL increment_counter
    BL read_char

    B while_lenght

mask:					/* Verifica mascara */
    CMP r4, #'i'

    CMP r4, #'s'
    BEQ read_string			/* Le STRING */

    CMP r4, #'c'
    BEQ read_char_param			/* Le CHAR */

    CMP r4, #'o'
    BEQ read_octa			/* Le inteiro OCTAL */

    CMP r4, #'d'
    BEQ read_decimal			/* Le inteiro DECIMAL */

    CMP r4, #'x'
    BEQ read_hexa			/* Le inteiro HEXA */

read_decimal:
    STMFD sp!, {r0-r11, lr}

    BL recupera_parametro		/* Recupera enderecos de memoria */

    LDR r6, =auxiliar
    MOV r7, #10
    EOR r8, r8, r8			
  
    MOV r10, #10
  
    BL read				/* Le numero */

    MOV r5, #0	

    LDRB r7, [r6]
    CMP r7, #'-'			/* Verifica se foi digitado um sinal de menos */
    MOVEQ r9, #1
    ADDEQ r5, r5, #1    

while_decimal:

    LDRB r7, [r6, r5]

    CMP r7, #'0'
    BLLT error
    BLT read_end_decimal		/*Verifica se o digito lido esta no intervalo correto  */
    CMP r7, #'9'
    BLGT error
    BGT read_end_decimal

    BL increment_success

    SUB r7, r7, #'0'   

    MOV r2, r8				/* Controi o numero atraves de multiplicacoes sucessivas */
    MUL r8, r2 ,r10

    ADD r8, r8, r7

    ADD r5, r5, #1			/* Tamanho maximo de um decimal em 32 bits */
    CMP r5, r10
    BGE  read_end_decimal
    B while_decimal
    
read_end_decimal:
    CMP r9, #1
    MVNEQ r8,r8
    ADDEQ r8, r8, #1

    STR r8, [r4]
    LDMFD sp!, {r0-r11, lr}
    B just_restart


/************************************************
    entrada	: lenght em r3
    saida	: -
************************************************/
read_octa:
    STMFD sp!, {r0-r11, lr}

    BL recupera_parametro		/* Recupera endereco de memoria */

    LDR r6, =auxiliar

    CMP r3, #4				/* Verifica a quantidade maxima de bytes que podem ser lidos */
    MOVEQ r7, #11				/* 4bytes o maximo em octal sao 11 digitos */

    CMP r3, #8
    MOVEQ r7, #22				/* 8bytes o maximo em octal sao 22 digitos */

    CMP r3, #2
    MOVEQ r7, #6				/* 2bytes o maximo em octal sao 6 digitos */

    CMP r3, #1
    MOVEQ r7, #3				/* 4bytes o maximo em octal sao 3 digitos */

    EOR r8, r8, r8			/* Resultado parcial */
    EOR r1, r1, r1			/* Segundo registrador caso necessarios - 8bytes */

    BL read

    MOV r5, #0				/* Contador que percorrera a string lida */
    MOV r9, #1

while_octa:

    LDRB r7, [r6, r5]			/* Le um byte */

    CMP r7, #'0'			/* Verifica se o digito eh valido 0 - 7 */
    BLLT error
    BLT read_end_octa

    CMP r7, #'7'
    BLGT error
    BGT read_end_octa

    BL increment_success

    SUB r7, r7, #'0'			/* Obtem valor do digito */   

thimps:
    CMP r5, #10				/* Verifica se um soh registrador sera capaz de representar o numero */
    ANDGE r2, r8, #0x20000000		/* Verifica se eh necessario somar o 3 digito mais significativo */
    MOVGE r2, r2, LSR #29
    
    ANDGE r11,r8, #0xC0000000		/* Caso nao, obtem-se os dois digitos mais significativos */
    MOVGE r11, r11, LSR #29		/* Desloca os digitos para os bits menos significativos */
    MOVGE r1, r1, LSL #3		/* Desloca registrador extra em 3 posicoes */
    ADDGE r1, r1, r11			/* Soma com os outros dois digitos  */    
    ADDGE r1, r1, r2

    ADD r5, r5, #1
    MOV r8, r8, LSL #3			/* Soma digito lido */
    ADD r8, r8, r7

    B while_octa
    
read_end_octa:
    CMP r3, #2
    STREQH r8, [r4]			/* Salva meia palavra */
    BEQ octa_push

    CMP r3, #1
    STREQB r8, [r4]			/* Salva um byte */
    BEQ octa_push
    
    STR r8, [r4]
    CMP r3, #8
    STRGE r1, [r4, #4]			/* Little endian */
    
octa_push:
    LDMFD sp!, {r0-r11, lr}
    B just_restart

/************************************************
    entrada	: lenght em r3
    saida	: -
************************************************/
read_hexa:
    STMFD sp!, {r0-r11, lr}

    BL recupera_parametro

    LDR r6, =auxiliar
    
    CMP r3, #4                          /* Verifica a quantidade maxima de bytes que podem ser lidos */               
    MOVEQ r7, #8                          /* 4bytes o maximo em octal sao 8 digitos */               

    CMP r3, #8
    MOVEQ r7, #16                         /* 8bytes o maximo em octal sao 16 digitos */               

    CMP r3, #2
    MOVEQ r7, #4                          /* 2bytes o maximo em octal sao 4 digitos */               

    CMP r3, #1
    MOVEQ r7, #2                          /* 4bytes o maximo em octal sao 2 digitos */               

    EOR r8, r8, r8                      /* Resultado parcial */               
    EOR r1, r1, r1                      /* Segundo registrador caso necessario - 8bytes */               

    BL read

    MOV r5, #0

while_hexa:

    LDRB r7, [r6, r5]			/* Le caracter */

    CMP r7, #'0'
    BLLT error
    BLT hexa_push

    CMP r7, #'f'
    BLGT error
    BGT hexa_push

    BL increment_success

    CMP r7, #'a'			/* Verifica se esta no intervalo correto */
    SUBGE r7, r7, #87
    SUBLT r7, r7, #'0'
ivan:
    CMP r5, #8				/* Verifica se eh necessario outro registrador */
    ANDGE r11,r8, #0xF0000000		/* Recupera 4 bits mais significativos */
    MOVGE r11, r11, LSR #28
    MOVGE r1, r1, LSL #4		/* Move registrador para a esquerda */
    ADDGE r1, r1, r11			/* Salva no novo registrador os bits mais significativos */

    ADD r5, r5, #1
    MOV r8, r8, LSL #4			/* Salva em r8 o digito atualmente lido */

    ADD r8, r8, r7

    B while_hexa
    
read_end_hexa:
    CMP r3, #2				/* Salva metade de palavra */
    STREQH r8, [r4]
    BEQ hexa_push

    CMP r3, #1				/* Salva um byte */
    STREQB r8, [r4]
    BEQ hexa_push

    STR r8, [r4]

    CMP r3, #8
    STREQ r1, [r4, #4]			/* Little Endian */

hexa_push:
    LDMFD sp!, {r0-r11, lr}

    B just_restart

/************************************************
    entrada	: lenght em r3
    saida	: -
************************************************/
read_string:
    STMFD sp!, {r0-r11, lr}

    BL recupera_parametro 		/* Recupera parametro  */

    MOV r6, r4

    BL recupera_parametro		/* Recupera parametro subsequente */
    
    SUB r7, r4, r6			/* Calcula a diferenca entre os enderecos de memoria */

    EOR r5, r5, r5
while_string:
    CMP r5, r7
    BGE continue_string

    BL increment_success
    B while_string

continue_string:
    LDR r10, =contador_parametros
    LDR r9, [r10]
    SUB r9, r9, #4
    STR r9, [r10]			/* Na chamada de recupera_parametro, incrementamos o contador. Por isso, devemos volta-lo na posicao correta */

    BL read				/* Le string */

    LDMFD sp!, {r0-r11, lr}
    B just_restart

/************************************************
    entrada	: lenght em r3
    saida	: -
************************************************/
read_char_param:
    STMFD sp!, {r0-r11, lr}
    BL recupera_parametro

    MOV r6, r4
    MOV r7, #1				/* r7 eh o numero de bits a serem lidos */
    BL read				/* Le apena um digito */
    BL increment_success
    LDMFD sp!, {r0-r11, lr}
    B just_restart

end_scanf:
    LDR r0, =retorno
    LDR r0, [r0]

    LDMFD sp!, {r1-r11, lr}		/* Desempilha registradores */
    LDMFD sp!, {r1}
    LDMFD sp!, {r2}
    LDMFD sp!, {r3}
    MOV pc, lr

/************************************************
    saida	: r4 - Caracter lido
************************************************/
read_char:
      STMFD sp!, {r5, lr}
	
      BL return_counter
      
      LDRB r4, [r0, r5]			/* Recupera caracter da cadeia */

      LDMFD sp!, {r5, lr}
      MOV pc, lr

/************************************************
    entrada	: lenght em r2
    saida	: r4 e r5 (caso necessario)
************************************************/
recupera_parametro:
    STMFD sp!, {r6-r11, lr}  

    LDR r7, =parametros			/* REcupera topo da pilha de parametros */
    LDR r7, [r7]
    BL return_counter_parameters
    
    LDR r4, [r7, r5]			/* Armazena em r4 o endereco de uma variavel a ser lida */

    BL increment_counter_parameters
    
    LDMFD sp!, {r6-r11, lr}
    MOV pc, lr

/************************************************
    saida	: r5 - Contador de parametros
************************************************/
return_counter_parameters:
      STMFD sp!, {r8, lr}

      LDR r8, =contador_parametros	/* Le contador de parametros da pilha */
      LDR r5, [r8]
      
      LDMFD sp!, {r8, lr}      
      MOV pc, lr

/************************************************
    saida	: r5 - Contador da string
************************************************/
return_counter:
      STMFD sp!, {r8, lr}

      LDR r8, =contador			/* Le contador  da string passada como parametro */
      LDR r5, [r8]
      
      LDMFD sp!, {r8, lr}      
      MOV pc, lr

/************************************************
      entrada	: -
      saida	: -
************************************************/
increment_counter:
      STMFD sp!, {r4, r5, lr}
  
      BL return_counter

      LDR r4, =contador
      ADD r5, r5, #1			/* Incrementa contador que percorre a string passada como parametro em myscanf */
      STR r5, [r4]

      LDMFD sp!, {r4, r5, lr}
      MOV pc, lr

/************************************************
entrada	: r6 - Tamanho da mascara (em bytes)
saida	: -
************************************************/
increment_counter_parameters:
      STMFD sp!, {r4, r5, lr}
  
      BL return_counter_parameters

      LDR r4, =contador_parametros	/* Incrementa contador que aponta os parametros na pilha */

      ADD r5, r5, #4			/* Cada apontador tem 4bytes */

      STR r5, [r4]

      LDMFD sp!, {r4, r5, lr}
      MOV pc, lr


/************************************************
    entrada	: r6 - Buffer de caracteres
		  r7 - numero de bytes/digitos
************************************************/
read:
      STMFD sp!, {r0-r11, lr}
      
      MOV r0, #0 			/* Valor de fildes em r0! */
      MOV r1, r6			/* Endereço de buf em r1! */
      MOV r2, r7 			/* Valor de nbyte em r2! */
      MOV r7, #3			/* Número da chamada de sistema em r7! */
      SVC #0				/* Instrução de chamada de sistema */

      LDMFD sp!, {r0-r11, lr}
      MOV pc, lr

/************************************************
      entrada	: -
      saida	: -
************************************************/
increment_success:
      STMFD sp!, {r4, r5, lr}

      LDR r4, =retorno
      LDR r5, [r4]
      ADD r5, r5, #1			
      STR r5, [r4]

      LDMFD sp!, {r4, r5, lr}
      MOV pc, lr

error:
      STMFD sp!, {r4, r5, lr}
 
      LDR r4, =retorno
      MOV r5, #-1			
      STR r5, [r4]

      LDMFD sp!, {r4, r5, lr}
      MOV pc, lr

.data
      .align 4      
      contador: .word 0			/* Contador que percorrerá a cadeia em r0 */
      contador_parametros:.word 0
      parametros: .word
      auxiliar: .fill 32, 4, 0x0
      retorno: .word 0x0
