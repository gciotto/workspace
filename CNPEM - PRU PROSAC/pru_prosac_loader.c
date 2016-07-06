#include <stdio.h>
#include <prussdrv.h>
#include <pruss_intc_mapping.h>
#include <strings.h>

#define PRU_NUM 0
#define PATH "/sys/class/gpio/gpio112"

int main(){

	FILE 	*output_pin = fopen (PATH "/value", "r+");

	tpruss_intc_initdata pruss_intc_initdata = PRUSS_INTC_INITDATA;

	prussdrv_init ();

	prussdrv_open (PRU_EVTOUT_0);
	prussdrv_open (PRU_EVTOUT_1);

	prussdrv_pruintc_init(&pruss_intc_initdata);

	prussdrv_exec_program (PRU_NUM, "./pru_prosac.bin");

	printf("Executando programa....\n");

	for (;;) {

		printf ("Esperando PRU_EVTOUT_1...\n");

		/* Waits for gpio events from PRU */
		prussdrv_pru_wait_event(PRU_EVTOUT_1);

		/* Clears events */
		prussdrv_pru_clear_event (PRU_EVTOUT_1, PRU1_ARM_INTERRUPT);

		printf ("Recebeu evento PRU_EVTOUT_1...\n");

		/* Generates a pulse for debugging-purposes */
		fwrite("1", sizeof(char), 1, output_pin);
		fflush(output_pin);

		fwrite("0", sizeof(char), 1, output_pin);
		fflush(output_pin);

	}

	prussdrv_pru_wait_event(PRU_EVTOUT_0);

	prussdrv_pru_disable(PRU_NUM);

	prussdrv_exit ();

	return 0;
}
