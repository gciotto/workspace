.origin 0
.entrypoint START

#define PRU0_R31_VEC_VALID 	32	// allows notification of program completion
#define PRU_EVTOUT_0 		3	// the event number that is sent back
#define PRU_EVTOUT_1		4

#define		IEP_BASE			0x0002E000
#define 	GLB_CFG_OFFSET		0x0
#define 	GLB_STATUS_OFFSET	0x4
#define 	COMPEN_OFFSET		0x8
#define		COUNT_OFFSET		0xC
#define		CMP_CFG_OFFSET		0x40
#define		CMP_STATUS_OFFSET	0x44
#define		CMP0_OFFSET			0x48

#define 	COUNT_VALUE_1MS		0x7A120  /* Contar 1ms */
#define 	COUNT_VALUE_1S		0x1DCD6500 /* Contar 1s */
#define		CLEAR_COUNT 		0xFFFFFFFF

START:
		
		CLR		r30.t5
		
		CLR		r1.t0

		/* Habilitar IEP Timer */
		
		/* Inicializacao */
		LBCO 	r0, c26, GLB_CFG_OFFSET, 4
		CLR 	r0.t0
		SET		r0.t4
		SBCO 	r0, c26, GLB_CFG_OFFSET, 4
		
		MOV 	r0, CLEAR_COUNT
		SBCO 	r0, c26, COUNT_OFFSET, 4
		
		LBCO 	r0, c26, GLB_STATUS_OFFSET, 4
		SET 	r0.t0
		SBCO 	r0, c26, GLB_STATUS_OFFSET, 4
		
		MOV 	r0, 0xFF
		SBCO 	r0, c26, CMP_STATUS_OFFSET, 4
		
		/* Configuracao */
		MOV		r0, COUNT_VALUE_1S
		SBCO	r0, c26, CMP0_OFFSET, 4
		
		LBCO 	r0, c26, CMP_CFG_OFFSET, 4
		SET		r0.t0
		SET		r0.t1		
		SBCO	r0, c26, CMP_CFG_OFFSET, 4
		
		MOV		r0, 0
		SBCO	r0, c26, COMPEN_OFFSET, 4
		
		
		/* Habilitar timer */
		LBCO 	r0, c26, GLB_CFG_OFFSET, 4
		SET		r0.t0
		SBCO 	r0, c26, GLB_CFG_OFFSET, 4


WAIT_1_MS:

		QBBC	NOT_R31_T3, r31.t3
		
		QBBS	WAIT_1_MS_CONDITION, r1.t0
		
		SET		r1.t0
				
		MOV 	r31.b0, PRU0_R31_VEC_VALID | PRU_EVTOUT_1
	
NOT_R31_T3:
		
		CLR		r1.t0
		
WAIT_1_MS_CONDITION:	

		LBCO	r0, c26, CMP_STATUS_OFFSET, 4
		QBBC	WAIT_1_MS, r0.t0

		/* Limpa flag */
		SET		r0.t0
		SBCO 	r0, c26, CMP_STATUS_OFFSET, 4
				
		QBBS	TURN_LED_OFF, r30.t5

TURN_LED_ON:

		SET		r30.t5 
		
		JMP 	WAIT_1_MS
		
TURN_LED_OFF:

		CLR		r30.t5
		
		JMP 	WAIT_1_MS
			
END:
		MOV 	r31.b0, PRU0_R31_VEC_VALID | PRU_EVTOUT_0
		HALT

