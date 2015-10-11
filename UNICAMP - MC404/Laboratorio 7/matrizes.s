      .text
      .align 4
      .global	mm
      .type	mm, %function
mm:
      STR r9, [sp, #-4]!		@ Empilha CONTADOR_LINHAS_A
      STR r9, [sp, #-4]!		@ Empilha CONTADOR_COLUNAS_A
      STR r9, [sp, #-4]!		@ Empilha CONTADOR_COLUNAS_B
      STR r9, [sp, #-4]!		@ Empilha VALOR_AUXILIAR

for_linhas:
      EOR r9, r9, r9
      STR r9, [sp, #4]

for_colunas_B:
      EOR r9, r9, r9
      STR r9, [sp, #8]			@ STR r9, contador_colunas_A
      STR r9, [sp, #0]			@ STR r9, valor_auxiliar

for_colunas_A:

      LDR r7, [sp, #8]			@ LDR r7, contador_colunas_A
      LDR r8, [sp, #4]			@ LDR r8, contador_colunas_B
      LDR r4, [sp, #24]			@ r4 = n_colunasB
      MLA r9, r7, r4 ,r8		@ r9 = n_colunasB * contadorColunasA + contadorColunasB
      MOV r9, r9, LSL #2		@ r9 = 4*r9
      
      LDR r8, [sp, #12]			@ LDR r8, contador_linhas_A
      LDR r6, [sp, #16]			@ r6 = n_colunas_A
      MUL r7, r8, r6
      MOV r8, r7
      LDR r7, [sp, #8] 			@ LDR r7, contador_colunas_A | r7 = contador_colunas_A
      ADD r8, r8, r7			@ r8 = n_colunasA * contadorLinhasA + contador_ColunasA
      MOV r8, r8, LSL #2		@ r8 = 4 * r8
 
      LDR r8, [r1, r8]
      LDR r9, [r2, r9]

      MUL r7, r8, r9  			@ r7 = A[contadorLinhasA][contadorColunasA] X B[contadorColunasA][contadorColunasB]
      
      LDR r8, [sp]
      ADD r7, r7, r8

      STR r7, [sp]			@ guarda resultado parcial

      LDR r9, [sp, #8]
      ADD r9, r9, #1
      STR r9, [sp, #8]
      LDR r6, [sp, #16]			@ r6 = n_colunas_A
      CMP r9, r6
      BLT for_colunas_A      

@----
      LDR r8, [sp, #12]
      LDR r9, [sp, #4]
      MUL r7, r8, r3
      ADD r9, r7, r9			@ r9 = contadorLinhasA * n_colunasB + contadorcolunasB
      MOV r9, r9, LSL #2		@ r9 = 4 * r9

      LDR r8, [sp]

atualiza_valor:
      STR r8, [r0, r9]
@-----
    
      LDR r9, [sp, #4]
      ADD r9, r9, #1
      STR r9, [sp, #4]
      LDR r4, [sp, #24]
      CMP r9, r4
      BLT for_colunas_B

      LDR r9, [sp, #12]
      ADD r9, r9, #1
      STR r9, [sp, #12]
      CMP r9, r3
      BLT for_linhas

      ADD sp, sp, #16
      MOV pc, lr