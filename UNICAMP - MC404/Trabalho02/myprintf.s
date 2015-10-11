      .text
      .align 4
      .global	myprintf 	
      .type	myprintf, %function

myprintf:
      
      STMFD sp!, {r3}			/* Empilha primeiros parametros */
      STMFD sp!, {r2}
      STMFD sp!, {r1}
  
      LDR r3, =parametros		/* Guarda endereço do primeiro elemento da pilha */
      STR sp, [r3]
      STMFD sp!, {r4-r11, lr} 		/* Guarda todos antigos registradores */

while:
      BL read_char			/* Le um char da cadeia */
      
      CMP r4, #0			/* Compara caracter com % */
      BEQ end

      CMP r4, #'%'			/* Compara caracter com % */
      BNE write_and_restart

flags:
      BL increment_counter		/* Encontrou um '%', lemos um novo elemento */
      BL read_char
    
      MOV r2, r4			/* r2 = FLAG , se r3 != #0 */
      EOR r3, r3, r3

      CMP r2, #'0'			/* Compara com 0 */      
      MOVEQ r3, #1			/* Indica que ha presenca de uma flag*/
      BEQ continue

      CMP r2, #'-'			/* Compara com - */
      MOVEQ r3, #1
      BEQ continue

      CMP r2, #'+'			/* Compara com + */
      MOVEQ r3, #1

continue:

    EOR r1, r1, r1			/* Tamanho width */
    CMP r3, #1				/* Verifica se ha necessario ler mais um caracter: */
    BLEQ increment_counter				/* é necessario ler quando R3 == 1 */
    BLEQ read_char
    
    STR r2, [sp, #-4]!			/* Usaremos o registrador R2. Portanto temos que guarda-lo */
width:   

    CMP r4, #'0'			/* Neste caso, soh eh possivel caracteres entre 0 e 9 */
    BLT lenght				/* Se encontrarmos algum caracter fora desse intervalo, nao ha largura */

    CMP r4, #'9'
    BGT lenght

    SUB r4, r4, #'0'			/* Obtem numero do caracter */

    MOV r2, #10				/* Multiplica resultado parcial por 10 */
    MOV r6, r1						/* e soma com o digito lido */
    MUL r1, r6, r2

    ADD r1, r1, r4			/* R1 = width */

    BL increment_counter		/* Le proximo caracter */
    BL read_char

    B width

lenght:
    LDR r2, [sp], #4
    MOV r6, #4				/* R6 = LENGHT = 32bits = 4bytes (valor inicial) */
    MOV r5, #1				/* R5 eh a quantidade de l presentes */
    MOV r8, #4				/* R8 eh soh um valor auxiliar, necessario na multiplicacao */
    
while_lenght:
    CMP r4, #'h'			/* Compara com 'h' */
    MOVEQ r6,r6, LSR #1
    BEQ lenght_continue

    CMP r4, #'l'			/* Compara com 'l' */
    MULEQ r6, r5, r8
    ADDEQ r5, r5, #1
    BEQ lenght_continue
    
    BNE mask

lenght_continue:
    BL increment_counter
    BL read_char

    B while_lenght

mask:					/* Verifica qual eh o tipo de impressao */
    CMP r4, #'i'
    BEQ write_decimal

    CMP r4, #'u'
    BEQ write_decimal

    CMP r4, #'s'			/* imprime STRING */
    BEQ write_string

    CMP r4, #'o'			/* imprime inteiro OCTAL */
    BEQ write_octal

    CMP r4, #'d'			/* imprime inteiro DECIMAL */
    BEQ write_decimal

    CMP r4, #'x'			/* imprime inteiro HEXA */
    BEQ write_hexa

    /* So pode ser char */

    /* imprime CHAR */
    CMP r6, #4			
    BLT write_decimal			/* Caso em que foi detectado hh antes de c */
    BEQ write_char_param

    BL error

end:
    
    LDMFD sp!, {r4-r11, lr}		/* Desempilha registradores */
    LDMFD sp!, {r1}
    LDMFD sp!, {r2}
    LDMFD sp!, {r3}

    LDR r0, =retorno
    LDR r0, [r0]

    MOV pc, lr

/************************************************
    entrada	: lenght em r6
    saida	: r4 e r5 (caso necessario)
************************************************/
write_hexa:
    STMFD sp!, {r4-r11, lr}

    MOV r10, #0
    
    BL recupera_parametro		/* Recupera parametro em r4 */

    MOV r7, #16				
    BL additional_space			/* Calcula a quantidade de digitos que necessitam */
					    /* ainda ser impressos, considerando a largura. */
    CMP r2, #'-'
    BEQ start_write_hexa		/* Verifica o alinhamento */

    CMP r2, #'0'			/* Caso haja largura, verifica se temos que imprimir '0' ou ' ' */
    MOVEQ r8, #'0'
    MOVNE r8, #' '
    BL print_zeros_spaces		/* Imprime ' ' ou '0' necessarios */

start_write_hexa:
    
    CMP r6, #8
    MOVGE r6, r6, LSR #1

    CMP r6, #4				/* Mascara para 4bytes */
    MOVGE r7, #0xF0000000 

    CMP r6, #2
    MOVEQ r7, #0xF000			/* Mascara para 2bytes */ 

    CMP r6, #1
    MOVEQ r7, #0xF0			/* Mascara para 1byte */ 
   
    MOV r8, #8				/* Calcula a quantidade de bits a serem deslocados em cada iteraçao */
    MUL r11, r6, r8
    SUB r11, r11, #4
 
while_hexa:    
    
    AND r8, r4, r7			/* Aplica a mascara */
    MOV r8, r8, LSR r11			/* Desloca a quantidade correta para que o valor estja no digito menos significativo */

    CMP r8, #0				/* Verifica se nao vamos imprimir '0' desnecessarios */
    MOVNE r10, #1			/* Caso haja um digito que nao seja '0', temos guardar que, caso haja outros zeros */
						      /* eles serao impressos */

    CMP r10, #0				/* Nao encontramos nenhum caracter valido ainda */
    BEQ continue_hexa

    CMP r8, #9				/* Verificamos se temos que subtrair '0' do digito lido ou 'a' */
    ADDGT r8, r8, #87
    ADDLS r8, r8, #'0'

    STRB r6, [sp, #-4]!			/* Escrevemos um digito */
    STRB r8, [sp, #-4]!			/* Escrevemos um digito */
    MOV r6, sp
    BL write_char
    LDRB r8, [sp], #4
    LDRB r6, [sp], #4
    
continue_hexa:
    SUB r11, r11, #4			/* Deslocamos mascara e quantidade de bits que vao  */
    MOV r7, r7, LSR #4				/* necessarios para deslocar o resultado */

    CMP r7, #0				/* Se a mascara for nula, terminamos */
    BNE while_hexa

fim_hexa:
    CMP r5, #0
    MOVNE r4, r5
    MOVNE r5, #0
    BNE start_write_hexa

    CMP r2, #'-'			/* Verificamos se o alinhamento eh a direita e */
    MOVEQ r8, #' '				/* imprimimos caso necessario */
    BLEQ print_zeros_spaces

    LDMFD sp!, {r4-r11, lr}		/* Desempilha tudo */

    B just_restart

/************************************************
    entrada	: lenght em r6
    saida	: -
************************************************/
write_octal:
    STMFD sp!, {r4-r11, lr}
    
    MOV r10, #0

    BL recupera_parametro		/* Recupera parametro em r4 */

    MOV r7, #8
    BL additional_space    		/* Calcula a quantidade de digitos que necessitam */
						/* ainda ser impressos, considerando a largura. */
    CMP r2, #'-'
    BEQ start_write_octa  		/* Verifica o alinhamento */

    CMP r2, #'0'           		/* Caso haja largura, verifica se temos que imprimir '0' ou ' ' */
    MOVEQ r8, #'0'
    MOVNE r8, #' '
    BL print_zeros_spaces  		/* Imprime ' ' ou '0' necessarios */

start_write_octa:

    CMP r6, #16				/* Tratando o segundo registrador de um numero de 64bits */
    MOVEQ r7, #0x38000000			/* Adequa as mascaras */
    MOVEQ r11, #27
    BEQ __start

    CMP r6, #8				/* Primeiro registrador de um numero de 64bits */
    MOVEQ r6, r6, LSL #2
    MOVEQ r7, #0x80000000		/* O resto de 64/3 eh 1, portanto, comecamos com a mascara somente recuperando o digito mais significativo */
    MOVEQ r11, #31
    BEQ __start

    CMP r6, #4		        	/* Mascara para 4bytes */
    MOVEQ r7, #0xC0000000		
                           
    CMP r6, #2				/* Mascara para 2bytes */
    MOVEQ r7, #0xC000
  
    CMP r6, #1				/* Mascara para 1byte */
    MOVEQ r7, #0xC0

    MOV r8, #8
    MUL r11, r6, r8
                           
    SUB r11, r11, #2         		/* Calcula a quantidade de bits a serem deslocados em cada iteracao */

__start:
    STR r9, [sp, #-4]!     
    MOV r9, #1            

while_octa:    

    CMP r7, #1
    BNE __continue

    STMFD sp!, {r5, r7}
    CMP r6, #32
    ANDEQ r5, r5, #0xC0000000		/* primeira vez no segundo registrador do numero de 64bits */
    MOVEQ r5, r5, LSL #30				/* Recupera os dois bits mais significativos do segundo registrador */
    ANDEQ r7, r4, #0x01					/* e 'junta' com o menos significativo da parte alta.  */
    MOVEQ r7, r7, LSL #2
    ADDEQ r8, r7, r5
    MOVEQ r6, r6, LSR #1
    LDMFD sp!, {r5, r7}
                    
    B __check

__continue:                           
    
    AND r8, r4, r7			/* Aplica a mascara */
					/* Desloca a quantidade correta para que o valor estja no digito no lugar correto */
    MOV r8, r8, LSR r11			

__check:
    CMP r8, #0				/* Verifica se nao vamos imprimir '0' desnecessarios */
    MOVNE r10, #1

    CMP r10, #0            
    BEQ skip_write_octal		/* Nao encontramos nenhum caracter valido ainda */

    CMP r8, #9
    ADDLS r8, r8, #'0'			/* Subtrai do digo o valor '0' */

    STRB r6, [sp, #-4]!
    STRB r8, [sp, #-4]!    		/* Escrevemos um digito */
    MOV r6, sp             	
    BL write_char
    LDRB r8, [sp], #4 
    LDRB r6, [sp], #4
    
skip_write_octal:
    MOV r7, r7, LSR #3			/* Deslocamos mascara e quantidade de bits que vao  */
    SUB r11, r11, #3	         		/* necessarios para deslocar o resultado */
                       	
    CMP r9, #1				/* Vericamos se precisamos adequar a mascara. Isso eh necessario somente na primeira vez */
    BNE continue_octa
                
    STR r5, [sp, #-4]!
    CMP r6, #32
    MOVEQ r5, #6			/* Adequamos a mascara somente a primeira vez. Nas demais, soh deslocamos 3 bits para a direita */
    MOVEQ r5, r5, LSL #28
    ADDEQ r7, r7, r5
    LDR r5, [sp], #4


    CMP r6, #4				/* Soma com um fator de forma que agora 3 digitos estejam setados na mascara (4bytes) */
    ADDEQ r7, r7, #0x20000000 

    CMP r6, #2
    ADDEQ r7, r7, #0x2000 		/* Soma com um fator de forma que agora 3 digitos estejam setados na mascara (2bytes) */

    CMP r6, #1
    ADDEQ r7, r7, #0x20 		/* Soma com um fator de forma que agora 3 digitos estejam setados na mascara (1byte) */

    EOR r9, r9, r9			/* Guardamos que nao precisamos fazer esse procedimento de novo */

continue_octa:
    CMP r7, #0				/* Se a mascara for nula, terminamos */
    BNE while_octa

fim_octa:
    LDR r9, [sp], #4

    CMP r5, #0
    MOVNE r4, r5
    MOVNE r5, #0
    BNE start_write_octa

    CMP r2, #'-'			/* Verificamos se o alinhamento eh a direita e */
    MOVEQ r8, #' '				/* imprimimos caso necessario */
    BLEQ print_zeros_spaces

    LDMFD sp!, {r4-r11, lr}		/* Desempilha tudo */
    B just_restart

/************************************************
    entrada	: r7 - width
    saida	: -
************************************************/
write_decimal:
    STMFD sp!, {r0,r3-r11, lr}
    
    MOV r3, r4
    
    BL recupera_parametro		/* Parametro em r4 */

    EOR r0, r0, r0

    MOV r8, #' '			
    
    EOR r10, r10, r10
    
    CMP r3, #'u'			/* Unsigned: nao precisa fazer verificacoes de sinal */
    BEQ unsigned

    CMP r6, #2				/* Verifica se um numero de 2 bytes eh negativo */
    ANDEQ r10, r4, #0x8000
    BNE __1byte

    CMP r10, #0				/* SE for acrescentamos os bits que faltam para completar 32 bits */
    ADDGT r4, r4, #0xff000000
    ADDGT r4, r4, #0x00ff0000
    
__1byte:
    CMP r6, #1				/* Verifica se um numero de 1 byte eh negativo */
    ANDEQ r10, r4, #0x80
    BNE ___4bytes

    CMP r10, #0
    ADDGT r4, r4, #0xff000000		/* SE for acrescentamos os bits que faltam para completar 32 bits */
    ADDGT r4, r4, #0x00ff0000
    ADDGT r4, r4, #0x0000ff00
  
___4bytes:
    CMP r6, #4
    ANDEQ r10, r4, #0x80000000

    CMP r10, #0				/* Numero Negativo: temos que usar o sistema de complemento de dois */
    MOVGT r10, #1			/* Caso o numero seja negativo, negamos todos os digitos e somamos 1. */
    MVNGT r5, r5
    ADDGT r5, r5, #1
    MVNGT r4, r4					
    ADDGT r4, r4, #1
    
unsigned:
    MOV r7, #10	
    BL additional_space			/* Calcula quantidade de digtos necessarios ser escritos por causa da largura */

    CMP r10, #1
    SUBEQ r9, r9, #1			/* Verifica se precisamos imprimir '-'. Nesse caso, subtraimos 1 da quantidade de digitos que serao impressos */

    CMP r2, #'-'
    BEQ start_decimal
    CMP r2, #'0'			/* Imprime '0' ou ' ', caso necessario */
    MOVEQ r8, #'0'
    MOVNE r8, #' '
    BL print_zeros_spaces
    
    CMP r10, #1
    BNE start_decimal			/* Checamos se eh necessario imprimir um sinal negativo '-' */

    LDR r7, =char_auxiliar
    MOV r8, #'-'			/* Imprimimos o sinal negativo '-' */
    STRB r8, [r7]				/* usamos um buffer auxiliar */
    STR r6, [sp, #-4]!
    MOV r6, r7
    BL write_char
    LDR r6, [sp], #4

start_decimal:

    CMP r6, #4
    LDREQ r9, =limite_superior_32		/* Faremos sucessivas divisoes e comecaremos com a maior potencia de 10 em 4 bytes */

    CMP r6, #2
    LDREQ r9, =limite_superior_16		/* Faremos sucessivas divisoes e comecaremos com a maior potencia de 10 em 2 bytes */

    CMP r6, #1
    LDREQ r9, =limite_superior_8		/* Faremos sucessivas divisoes e comecaremos com a maior potencia de 10 em 1 byte */

    LDR r9, [r9]

while_decimal:
    
    BL divide				/* Divide o numero por uma potencia de 10 */

    STMFD sp!, {r1, r4, r9}
    CMP r6, #8
    BNE continue_while_decimal

    LDR r9, =limite_superior_32
    STR r9, [r9]
    MOV r4, r8
    BL divide				/* Anda com o contador: multiplica por 10 */
    MOV r9, #10
    MOV r8, r4
    BL divide

continue_while_decimal:
    LDMFD sp!, {r1, r4, r9}

    CMP r8, #0				/* se o numero for divido e o quociente foi zero, nao imprimimos nada */
    MOVNE r0, #1

    CMP r0, #1
    BNE continue_decimal

    ADD r8, r8, #'0'			/* imprime o digito correto na tela */
    LDR r11, =char_auxiliar
    STRB r8, [r11]
    STR r6, [sp, #-4]!
    MOV r6, r11
    BL write_char
    LDR r6, [sp], #4

    SUB r8, r8, #'0'			/* Subtrai do numero original, digito * potencia de 10 */
    MUL r7, r8, r9
    SUB r4, r4, r7

continue_decimal:
    MOV r11, r4
    MOV r4, r9
    MOV r9, #10
    BL divide				/* Divide contador */

    MOV r9, r8

    MOV r4, r11
    CMP r9, #0				/* Checa se o contador nao eh zero. Sai da iteracao se for zero. */
    BNE while_decimal

end_decimal:    
    CMP r6, #8
    MOVEQ r4, r5
    MOVEQ r5, #0
    MOVEQ r6, r6, LSR #1
    BEQ start_decimal

    CMP r2, #'-'
    MOVNE r8, #' '
    BLEQ print_zeros_spaces		/* Imprime caso padding seja a direita */
    

    LDMFD sp!, {r0,r3-r11, lr}		/* Desempilha tudo */
    B just_restart

/************************************************
    entrada	: r4 - string
    saida	: r9 - count
************************************************/
str_count:
    STMFD sp!, {r4-r8, r10-r11, lr}
    EOR r9, r9, r9

while_str_count:
 
    LDRB r8, [r4, r9]			/* Percorre a string ateh encontrar /0 */
    CMP r8, #0
    BEQ end_str_count

    ADD r9, r9, #1

    B while_str_count    		/* Checa novamente com proximo caracter */


end_str_count:
    LDMFD sp!, {r4-r8, r10-r11, lr}
    MOV pc, lr

/************************************************
    entrada	: r7 - width
    saida	: -
************************************************/
write_string:
    STMFD sp!, {r4-r11, lr}
    
    BL recupera_parametro		/* Parametro em r4 */

    BL str_count

    SUB r9, r1, r9

    CMP r2, #'-'			/* Verifica se eh necessario escrever '0' ou ' ' */
    MOVNE r8, #' '
    BLNE print_zeros_spaces

    EOR r5, r5, r5

while_str:
    ADD r6, r4, r5			/* Percorre string, escrevendo na tela */
    BL write_char
    LDRB r7, [r4, r5]

    ADD r5, r5, #1
    CMP r7, #0
    BNE while_str			/* Continua ate achar /0 */

    CMP r2, #'-'
    MOVEQ r8, #' '
    BLEQ print_zeros_spaces		/* Caso o alinhamento seja a esquerda, imprime-se espaços necessarios */

    LDMFD sp!, {r4-r11, lr}
    B just_restart

/************************************************
    entrada	: lenght em r6
    saida	: r4 e r5 (caso necessario)
************************************************/
recupera_parametro:
    STMFD sp!, {r6-r11, lr}  

    LDR r7, =parametros			/* Recupera valor da primeira posicao da pilha  */
    LDR r7, [r7]
    BL return_counter_parameters	/* Recupera indice */
    
					/* R6 = LENGHT : Numero de bytes de leitura */
    CMP r6, #4
    LDREQ r4, [r7,r5]			/* Dependendo do tamanho da mascara lê a quantidade correta de bytes */
    MOVEQ r5, #0				/* inicializa o valor do segundo registrador */

    CMP r6, #2				
    LDREQH r4, [r7,r5]
    MOVEQ r5, #0				/* inicializa o valor do segundo registrador */
    
    CMP r6, #1
    LDREQB r4, [r7,r5]
    MOVEQ r5, #0				/* inicializa o valor do segundo registrador */

    CMP r6, #8				/* Caso o numero seja de 64 bits sao necessarios dois registradores */
    MOVEQ r11, r5
    LDREQ r5, [r7,r5]
    ADDEQ r11, r11, #4
    LDREQ r4, [r7, r11]
    
    BL increment_counter_parameters	/* incrementa contador de parametros */
    
    LDMFD sp!, {r6-r11, lr}		/* Desempilha tudo */
    MOV pc, lr
    
/************************************************
    entrada	: lenght em r6
    saida	: r4 e r5 (caso necessario)
************************************************/
write_char_param:
    STMFD sp!, {r4-r11, lr}

    BL recupera_parametro		/* Parametro em r4 */

    SUB r9, r1, #1

    CMP r2, #'-'			/* Como nos outros casos, imprime-se os espaços ou zeros necessarios */
    MOVNE r8, #' '
    BLNE print_zeros_spaces

    LDR r7, =char_auxiliar		
    STRB r4, [r7]
    
    STR r6, [sp, #-4]!
    MOV r6, r7
    BL write_char			/* imprime caracter */
    STR r7, [sp], #-4

    CMP r2, #'-'
    MOVEQ r8, #' '
    BLEQ print_zeros_spaces		/* alinhamento a esquerda: imprime-se espços a direita do valor */

    LDMFD sp!, {r4-r11, lr}		/* desempilha tudo */
    B just_restart

/************************************************
    entrada	: r6 - Buffer de caracteres
************************************************/
write_char:
      STMFD sp!, {r0, r1, r2, r7, lr}
      
      MOV r0, #0 			/* Valor de fildes em r0! */
      MOV r1, r6			/* Endereço de buf em r1! */
      MOV r2, #1 			/* Valor de nbyte em r2! */
      MOV r7, #4			/* Número da chamada de sistema em r7! */
      SVC #0				/* Instrução de chamada de sistema */

      BL increment_success
      LDMFD sp!, {r0, r1, r2, r7, lr}
      MOV pc, lr

/************************************************
    saida	: r4 - Caracter lido
************************************************/
read_char:
      STMFD sp!, {r5, lr}		/* Retorna o valor de um caracter da string de formatos */
	
      BL return_counter
      
      LDRB r4, [r0, r5]			/* Recupera caracter da cadeia */
      
      LDMFD sp!, {r5, lr}
      MOV pc, lr

/************************************************
    saida	: r5 - Contador de parametros
************************************************/
return_counter_parameters:
      STMFD sp!, {r8, lr}		/* Retorna contador de parametros */

      LDR r8, =contador_parametros	/* Le contador */
      LDR r5, [r8]
      
      LDMFD sp!, {r8, lr}      
      MOV pc, lr

/************************************************
    saida	: r5 - Contador da string
************************************************/
return_counter:
      STMFD sp!, {r8, lr}		/* Retorna contador da string */

      LDR r8, =contador			/* Le contador */
      LDR r5, [r8]
      
      LDMFD sp!, {r8, lr}      
      MOV pc, lr

/************************************************
      entrada	: -
      saida	: -
************************************************/
increment_counter:
      STMFD sp!, {r4, r5, lr}		/* Incrementa contador, isto eh, anda na string */
  
      BL return_counter

      LDR r4, =contador
      ADD r5, r5, #1
      STR r5, [r4]

      LDMFD sp!, {r4, r5, lr}
      MOV pc, lr

/************************************************
      entrada	: r8: caracter
		  r9: width - lenght
      saida	: -
************************************************/
print_zeros_spaces:
      STMFD sp!, {r1-r9, lr}
      
      CMP r9, #0			/* Verifica se a quantidade de ' ' ou '0' eh valido */
      BLE end_print_zeros_spaces
    
      LDR r6, =char_auxiliar
      
      STRB r8, [r6]			/* Buffer a ser impresso */

      MOV r2, #0

while_print_zeros_spaces:

      CMP r2, r9
      BGE end_print_zeros_spaces	/* Verifica se jah imprimiu a quantidade correta */

      BL write_char			/* imprime char corretamente */

      ADD r2, r2, #1			/* anda com contador */

      B while_print_zeros_spaces

end_print_zeros_spaces:
      LDMFD sp!, {r1-r9, lr}
      MOV pc, lr

/************************************************
      entrada	: r1: width 
		  r4, r5: number
		  r6, lenght (bytes)
		  r7: base
      saida	: r9: width - lenght
************************************************/
negative_case:

__8bytes:				/* Se for negativo, basta sabermos o tamanho e a base, porque sabemos  */
      CMP r6, #8				/* que o primeiro bit esta setado */
      BNE __4bytes

      CMP r7, #16
      MOVEQ r9, #16
      BEQ end_additional

      CMP r7, #8
      MOVEQ r9, #22
      BEQ end_additional

__4bytes:
      CMP r6, #4
      BNE continue_negative

      CMP r7, #16
      MOVEQ r9, #8
      BEQ end_additional

      CMP r7, #8
      MOVEQ r9, #11
      BEQ end_additional

continue_negative:

      B end_additional

additional_space:
      STMFD sp!, {r1-r8, r10, r11, lr}
    
      CMP r3, #'u'
      MOVEQ r9, #0
      BEQ start_additional_space

      CMP r4, #0
      MOVEQ r9, #0			/* Quantidade de digitos */
      MOVGT r9, #1
      BLT negative_case			/* Verifica se o numero nao eh negativo */

start_additional_space:    
      
      MOV r2, #1			/* Potencias da base passada como parametro */

while_additional:

      MUL r11, r2, r7			/* Verifica maior numero possivel para a quantidade atual de digitos (r9) */
      MOV r2, r11
      SUB r11, r11, #1

      CMP r4, r11
      BLS end_additional		/* Se o numero for menor jah precisamos parar a interacao. Ja achamos teto */

      ADD r11, r11, #1

      LDR r10, =limite_superior_32
      LDR r10, [r10]
      CMP r11, r10
      BEQ end_additional

      ADD r9, r9, #1

      B while_additional   

end_additional:
      CMP r5, #0
      BEQ end_add
    
      CMP r9, #0			/* Caso o numero seja de 64bits verificamos a outra parte (32bits restantes) */
      MOVEQ r4, r5
      ADDEQ r9, r9, #1
      MOVEQ r5, #0
      BEQ start_additional_space

      CMP r7, #16
      ADDEQ r9, r9, #8

      CMP r7, #8
      ADDEQ r9, r9, #11

end_add:
      SUB r9, r1, r9			/* Subtrai da largura o valor de digitos encontrados */
      LDMFD sp!, {r1-r8, r10, r11, lr}
      MOV pc, lr

/************************************************
entrada	: r6 - Tamanho da mascara (em bytes)
saida	: -
************************************************/
increment_counter_parameters:
      STMFD sp!, {r4, r5, lr}		/* Incrementa contador de parametros */
  
      BL return_counter_parameters	/* Recupera o valor atual */

      LDR r4, =contador_parametros

					/* LENGHT : Numero de bytes de leitura */
      CMP r6, #4
      ADDEQ r5, r5, #4			/* Verifica a quantidade correta para se incrementar  */
						/* dependendo do tamanho do valor */
      CMP r6, #2
      ADDEQ r5, r5, #4

      CMP r6, #1
      ADDEQ r5, r5, #4

      CMP r6, #8
      ADDEQ r5, r5, #8

      STR r5, [r4]			/* Guarda novo valor */

      LDMFD sp!, {r4, r5, lr}
      MOV pc, lr

write_and_restart:
      
      BL return_counter			/* Escreve e reinicia a iteracao */
      ADD r6, r0, r5

      BL write_char

just_restart:
      BL increment_counter		/* Reinicia iteracao principal */

      B while


/************************************************
    entrada 	:- r4 e r9
    saida  	:- r8 = r4/r9
************************************************/
divide:
	STMFD sp!, {r4, r6, r7, r9-r11, lr}      
	LDR r10, =limite_divisao
	LDR r10, [r10]

      	CMP r9, #0
	BEQ divide_end
					@check for divide by zero!
      
	MOV r8,#0	     		@ clear R0 to accumulate result
	MOV r3,#1			@set bit 0 in R3, which will be
					@shifted left then right
start:
	CMP r9, r10
	BEQ next

	CMP r9,r4
	MOVLS r9,r9,LSL#1
	MOVLS r3,r3,LSL#1
	BLS      start
					@shift R2 left until it is about to
					@be bigger than R1
					@shift R3 left in parallel in order
					@to flag how far we have to go

next:
	CMP r4,r9	      		@carry set if R1>R2 (don't ask why)
	SUBCS r4,r4,r9  	 	@subtract R2 from R1 if this would
					@give a positive answer
	ADDCS r8,r8,r3 		  	@and add the current bit in R3 to
					@the accumulating answer in R0

	MOVS r3,r3,LSR#1   	  	@Shift R3 right into carry flag
	MOVCC r9,r9,LSR#1 		@and if bit 0 of R3 was zero, also
					@shift R2 right
	BCC next			@If carry not clear, R3 has shifted
					@back to where it started, and we
					@can end
divide_end:
	LDMFD sp!, {r4, r6, r7, r9-r11, lr}
	MOV pc, lr

/************************************************
      entrada	: -
      saida	: -
************************************************/
increment_success:
      STMFD sp!, {r4, r5, lr}

      LDR r4, =retorno
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
      contador: .word 0		/* Contador que percorrerá a cadeia em r0 */
      contador_parametros:.word 0
      parametros: .word 0
      limite_divisao: .word 0xEE6B2800
      limite_superior_32: .word 0x3B9ACA00
      limite_superior_16: .word 0x2710
      limite_superior_8: .word 0x64
      char_auxiliar: .byte 48
      retorno: .word 0x0
