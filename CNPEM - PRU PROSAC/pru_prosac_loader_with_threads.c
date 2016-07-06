#include <stdio.h>
#include <prussdrv.h>
#include <pruss_intc_mapping.h>
#include <strings.h>
#include <pthread.h>

#define PRU_NUM 0
#define PATH "/sys/class/gpio/gpio112"
#define NUMBER_OF_CONCURRENT_THREADS 80

void* thread_pru_start(void *args) {

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

}

void* thread_concurrent_start(void *args) {

	int id = (int) args;

	for (;;) {

		printf("CONCURRENT THREAD #%d...\n", id);
	}

}

int main(){

	struct sched_param p_thread_pru, p_thread_concurrent;
	int i;

	pthread_t thread_pru, thread_concurrent[NUMBER_OF_CONCURRENT_THREADS];

	pthread_create(&thread_pru, NULL, thread_pru_start, NULL);
	p_thread_pru.sched_priority = 50;
	pthread_setschedparam(thread_pru, SCHED_FIFO, &p_thread_pru);

	p_thread_concurrent.sched_priority = 25;
	for (i = 0; i < NUMBER_OF_CONCURRENT_THREADS; i++) {
		pthread_create(&thread_concurrent[i], NULL, thread_concurrent_start, (void*) i);
		pthread_setschedparam(thread_concurrent[i], SCHED_RR, &p_thread_concurrent);
	}

	pthread_join(thread_pru, NULL);

	for (i = 0; i < NUMBER_OF_CONCURRENT_THREADS; i++)
		pthread_join(thread_concurrent[i], NULL);

	return 0;
}
