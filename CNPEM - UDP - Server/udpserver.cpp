#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdexcept>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <iostream>
#include <sched.h>
#include <pthread.h>

#define PORT_NUMBER 561
#define PATH_60 "/sys/class/gpio/gpio60"
#define PATH_67 "/sys/class/gpio/gpio67"
#define PATH_49 "/sys/class/gpio/gpio49"
#define EXPORT_PATH "/sys/class/gpio/export"
#define MAX_TICKS 1
#define MESSAGE_LENGTH 6

using namespace std;

void* run_server(void* args);

int main () {

//	int create_result;
//	pthread_t thread1;
//
//	//set attributes
//	pthread_attr_t attr1;
//	struct sched_param parm1;
//
//	pthread_attr_getschedparam(&attr1, &parm1);
//
//	parm1.sched_priority = sched_get_priority_max(SCHED_FIFO);
//	pthread_attr_setschedpolicy(&attr1, SCHED_FIFO);
//
//	pthread_attr_setschedparam(&attr1, &parm1);
//
//	if ((create_result = pthread_create(&thread1, &attr1, run_server, NULL))) {
//
//		printf("Erro %d\n", create_result);
//
//		return 1;
//
//	}
//
//	pthread_setschedparam(thread1, SCHED_FIFO, &parm1);
//
//	pthread_join(thread1, NULL);
//
	run_server(NULL);
	return 0;

}

void* run_server(void* args) {

	int sockfd, result, counter = 0, on = 0, onOsc = 0,
			tick_pressed = 0, counter_received, change_cmd_received = 0, tick_counter = 0;
	char message[MESSAGE_LENGTH];
	struct sockaddr_in server_addr, client_address;

	FILE 	*exportFile = fopen(EXPORT_PATH, "w");

	fwrite ("60", sizeof(char), 2, exportFile);
	fflush(exportFile);
	fwrite ("67", sizeof(char), 2, exportFile);
	fflush(exportFile);
	fwrite ("49", sizeof(char), 2, exportFile);

	fclose(exportFile);

	FILE 	*LEDValue = fopen (PATH_60 "/value", "w"),
			*LEDDirection = fopen (PATH_60 "/direction", "w"),
			*LEDValue67 = fopen (PATH_67 "/value", "w"),
			*LEDDirection67 = fopen (PATH_67 "/direction", "w"),
			*LEDValue49 = fopen (PATH_49 "/value", "w"),
			*LEDDirection49 = fopen (PATH_49 "/direction", "w");

	cout << "ok" << endl;

	fwrite("out", sizeof(char), 3, LEDDirection);
	fflush(LEDDirection);


	fwrite("0", sizeof(char), 1 , LEDValue);
	fflush(LEDValue);


	fwrite("out", sizeof(char), 3, LEDDirection67);
	fflush(LEDDirection67);

	fwrite("0", sizeof(char), 1 , LEDValue67);
	fflush(LEDValue67);


	fwrite("out", sizeof(char), 3, LEDDirection49);
	fflush(LEDDirection49);

	fwrite("0", sizeof(char), 1 , LEDValue49);
	fflush(LEDValue49);

	sockfd = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);

	printf("%d \n", sockfd);

	bzero((char *) &server_addr, sizeof(server_addr));
	bzero((char *) &client_address, sizeof(client_address));

	server_addr.sin_family = AF_INET;
	server_addr.sin_port = htons(PORT_NUMBER);
	//server_addr.sin_addr.s_addr = INADDR_ANY; /* Endereco do servidor (propria maquina) */
	server_addr.sin_addr.s_addr = inet_addr("192.168.2.22"); /* Endereco do servidor (propria maquina) */

	if ((result = bind(sockfd, (struct sockaddr *) &server_addr, sizeof(server_addr))) < 0) {
		perror("ERROR on binding");
		return NULL;
	}

	printf("bind %d \n", result);

	for (;;) {

		int change_state, n_message_length;
		bzero(message, MESSAGE_LENGTH);

		n_message_length = read(sockfd, message, MESSAGE_LENGTH);

		if (n_message_length < 0) perror("ERROR reading from socket");


		change_state = message[4] - '0';

		message[4] = '\0';

		counter_received = atoi(message);

		if (change_state) {
			char state[1];

			sprintf(state, "%d", on);

			fwrite(state, sizeof(char), 1 , LEDValue49);
			fflush(LEDValue49);

			onOsc = !onOsc;

			sprintf(state, "%d", onOsc);
			fwrite(state, sizeof(char), 1 , LEDValue67);
			fflush(LEDValue67);

			tick_pressed = 1;
			on = !on;
			change_state = 0;

			change_cmd_received++;
		}

		if (tick_pressed)
			tick_counter++;

		if (tick_counter >= MAX_TICKS && tick_pressed) {
			char state[1];

			//printf("state changed at tick = %d.\n", counter_received);

			onOsc = !onOsc;

			sprintf(state, "%d", onOsc);
			fwrite(state, sizeof(char), 1 , LEDValue67);
			fflush(LEDValue67);


			tick_pressed = 0;
			tick_counter = 0;

		}

		if (counter_received != counter) {
			counter = counter_received;
		}

		if (counter_received % 2) {
			fwrite("0", sizeof(char), 1 , LEDValue);
			fflush(LEDValue);
		}
		else {
			fwrite("1", sizeof(char), 1 , LEDValue);
			fflush(LEDValue);
		}

		counter++;
		if (counter > 1000)
			counter = 0;
	}

	close(sockfd);

	return NULL;

}

