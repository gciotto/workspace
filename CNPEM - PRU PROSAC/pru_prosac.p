.origin 0
.entrypoint START

#define PRU0_R31_VEC_VALID 	32	/* allows notification of program completion */
#define PRU_EVTOUT_0 		3	/* the event number that is sent back */
#define PRU_EVTOUT_1		4   /* Event id indicating a received pulse */

START:
		
		/* Waits input pin to be set to 1 */
		WBS 	r31.t0
		
		/* Counter has received a pulse - sends an event back to PROSAC interrupter() thread */
		MOV 	r31.b0, PRU0_R31_VEC_VALID | PRU_EVTOUT_1
		
		/* Waits pulse to be reset to 0 */
		WBC 	r31.t0
		
		JMP	 	START		

END:
		MOV 	r31.b0, PRU0_R31_VEC_VALID | PRU_EVTOUT_0
		HALT
