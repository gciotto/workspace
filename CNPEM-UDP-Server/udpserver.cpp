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

#define PORT_NUMBER 561
#define PATH_60 "/sys/class/gpio/gpio60/"
#define PATH_48 "/sys/class/gpio/gpio49/"


using namespace std;


int main () {

	int sockfd, clientsockfd, client_address_size, n_message_length, result, result_listen, counter = 0, on = 0;
	char message[256];
	struct sockaddr_in server_addr, client_address;

	printf("sdasd");

	FILE 	*LEDValue = fopen (PATH_60 "/value", "r+"),
			*LEDDirection = fopen (PATH_60 "/direction", "r+"),
			*LEDValue48 = fopen (PATH_48 "/value", "r+"),
			*LEDDirection48 = fopen (PATH_48 "/direction", "r+");

	fwrite("out", sizeof(char), 3, LEDDirection);
	fflush(LEDDirection);

	fwrite("0", sizeof(char), 1 , LEDValue);
	fflush(LEDValue);



	fwrite("out", sizeof(char), 3, LEDDirection48);
	fflush(LEDDirection48);

	fwrite("0", sizeof(char), 1 , LEDValue48);
	fflush(LEDValue48);



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
		return 1;
	}

	printf("bind %d \n", result);

	for (;;) {

		char number[5], change_state;

		bzero(message,256);

		n_message_length = read(sockfd, message, 255);

		if (n_message_length < 0)
			perror("ERROR reading from socket");


		printf("Here is the message: %s	\n",message);

		change_state = message[4];



		counter++;
		strncpy(number, message, 4);
		number[4] = '\0';

		if (counter == atoi(number) ) {
			if (counter % 2) {
				fwrite("0", sizeof(char), 1 , LEDValue);
				fflush(LEDValue);
			}
			else {
				fwrite("1", sizeof(char), 1 , LEDValue);
				fflush(LEDValue);
			}
		}

		if (change_state == '1') {
			char state[1];
			on = !on;
			sprintf(state, "%d", on);
			fwrite(state, sizeof(char), 1 , LEDValue48);
			fflush(LEDValue48);
		}
	}

	close(sockfd);

	return 0;

}

