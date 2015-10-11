.global write
.global fork
.global exit
.global read

fork:
      STMFD sp!, {r7, lr}
      MOV r7, #2		/* Configura parametro - # FORK */

      SVC #0			/* Gera interrupcao */

      LDMFD sp!, {r7, lr}
      MOV pc, lr 

read:
      STMFD sp!, {r7, lr}
      MOV r7, #3		/* Configura parametro - # READ */

      SVC #0			/* Gera interrupcao */

      LDMFD sp!, {r7, lr}
      MOV pc, lr 

write:
      STMFD sp!, {r7, lr}
      MOV r7, #4		/* Configura parametro - #4 WRITE */

      SVC #0			/* Gera interrupcao */

      LDMFD sp!, {r7, lr}
      MOV pc, lr 

exit:
 
      STMFD sp!, {r7, lr}
      MOV r7, #1		/* Configura parametro - #1 EXIT */

      SVC #0			/* Gera interrupcao */

      LDMFD sp!, {r7, lr}
      MOV pc, lr
