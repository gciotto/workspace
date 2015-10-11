      .text
      .align 4
      .global	mm
      .type	mm, %function
mm:

for_linhas:
      MOV r9, #0
      STR r9, contador_colunas_B

for_colunas_B:
      MOV r9, #0
      STR r9, contador_colunas_A
      STR r9, valor_auxiliar

for_colunas_A:

      LDR r7, contador_colunas_A
      LDR r8, contador_colunas_B
      MLA r9, r7, r4 ,r8		@ r9 = n_colunasB * contadorColunasA + contadorColunasB
      MOV r9, r9, LSL #2		@ r9 = 4*r9
      
      LDR r8, contador_linhas_A
      MUL r7, r8, r6
      MOV r8, r7
      LDR r7, contador_colunas_A	@ r7 = contador_colunas_A
      ADD r8, r8, r7			@ r8 = n_colunasA * contadorLinhasA + contador_ColunasA
      MOV r8, r8, LSL #2		@ r8 = 4 * r8
 
      LDR r8, [r1, r8]
      LDR r9, [r2, r9]

      MUL r7, r8, r9  			@ r7 = A[contadorLinhasA][contadorColunasA] X B[contadorColunasA][contadorColunasB]
      
      LDR r8, valor_auxiliar
      ADD r7, r7, r8

      STR r7, valor_auxiliar		@ guarda resultado parcial

      LDR r9, contador_colunas_A
      ADD r9, r9, #1
      STR r9, contador_colunas_A
      CMP r9, r6
      BLT for_colunas_A      

@----
      LDR r8, contador_linhas_A
      LDR r9, contador_colunas_B	
      MUL r7, r8, r4
      ADD r9, r7, r9			@ r9 = contadorLinhasA * n_colunasB + contadorcolunasB
      MOV r9, r9, LSL #2		@ r9 = 4 * r9

      LDR r8, valor_auxiliar

atualiza_valor:
      STR r8, [r0, r9]
@-----
    
      LDR r9, contador_colunas_B
      ADD r9, r9, #1
      STR r9, contador_colunas_B
      CMP r9, r4
      BLT for_colunas_B

      LDR r9, contador_linhas_A
      ADD r9, r9, #1
      STR r9, contador_linhas_A
      CMP r9, r3
      BLT for_linhas

      MOV pc, lr

valor_auxiliar: .word 0x0 
contador_linhas_A: .word 0x0
contador_colunas_A: .word 0x0
contador_colunas_B: .word 0x0
