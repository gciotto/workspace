.global write
.global exit

write:
      STMFD sp!, {r7, lr}
      MOV r7, #4

      SVC #0

      LDMFD sp!, {r7, lr}
      MOVS pc, lr 

exit:
 
      STMFD sp!, {r7, lr}
      MOV r7, #1

      SVC #0

      LDMFD sp!, {r7, lr}
      MOVS pc, lr
