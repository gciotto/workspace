#include <stdio.h>
#include <prussdrv.h>
#include <pruss_intc_mapping.h>

#define PRU_NUM 0

int main(){

	char c;
	tpruss_intc_initdata pruss_intc_initdata = PRUSS_INTC_INITDATA;

	prussdrv_init ();

	prussdrv_open (PRU_EVTOUT_0);
	prussdrv_open (PRU_EVTOUT_1);

	prussdrv_pruintc_init(&pruss_intc_initdata);

	prussdrv_exec_program (PRU_NUM, "./pru_timer_server.bin");

	int count = 0;

	for (;;) {


		printf ("Esperando PRU_EVTOUT_1...\n");

		prussdrv_pru_wait_event(PRU_EVTOUT_1);

		/* Processa pacote */

		printf ("Recebeu evento PRU_EVTOUT_1...\n");

		/* Fim do processo do pacote */

		prussdrv_pru_send_event(ARM_PRU0_INTERRUPT);

		printf ("Send event ARM_PRU0_INTERRUPT...\n");

		prussdrv_pru_clear_event(PRU0, ARM_PRU0_INTERRUPT);

		printf ("Clear event ARM_PRU0_INTERRUPT...\n");

		prussdrv_pru_clear_event (PRU_EVTOUT_1, PRU1_ARM_INTERRUPT);

		printf("Count #%d...\n", count++);

	}

	prussdrv_pru_wait_event(PRU_EVTOUT_0);

	prussdrv_pru_disable(PRU_NUM);

	prussdrv_exit ();

	return 0;
}

