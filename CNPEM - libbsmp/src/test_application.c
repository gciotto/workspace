#include "../include/server.h"
#include "../include/client.h"
#include "../include/bsmp.h"
#include "server_priv.h"
#include "bsmp_priv.h"

#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <stdio.h>

#include <pthread.h>

#define FULL_BUFFER 1
#define EMPTY_BUFFER 2
#define BUFFER_SIZE 256

struct bsmp_message
{
	uint8_t     code;
	uint16_t    payload_size;
	uint8_t     payload[BSMP_MAX_PAYLOAD];
};

uint8_t circular_buffer[BUFFER_SIZE];
uint8_t start = 0, end = 0, client_message_count = 0, server_message_count = 0, bytes_count = 0;

pthread_mutex_t lock_buffer;
bool isServerReady = false;

bool hasMessageFromClient();

void init_variables(struct bsmp_var** variables,  uint32_t* count);
void init_curves(struct bsmp_curve** curves,  uint32_t* count);

bool read_curve_example(struct bsmp_curve *curve, uint16_t block, uint8_t *data,
		uint16_t *len);

bool write_curve_example(struct bsmp_curve *curve, uint16_t block, uint8_t *data,
		uint16_t len);

int client_send_example (uint8_t* data, uint32_t *count);
int client_receive_example (uint8_t* data, uint32_t *count);

void* server_thread (void* args);
void* client_thread (void* args);

void server_read_raw_packet (struct bsmp_raw_packet *received_packet);
void server_write_raw_packet (struct bsmp_raw_packet *to_be_sent_packet);

void print_buffer();


int main(){


	pthread_mutex_init(&lock_buffer, NULL);

	// Threads
	pthread_t sv_thread, cl_thread;

	if (pthread_create(&sv_thread, NULL, server_thread, NULL)){
		printf ("Server thread creation process has failed.");
		return 1;
	}

	if (pthread_create(&cl_thread, NULL, client_thread, NULL)){
		printf ("Client thread creation process has failed.");
		return 1;
	}

	pthread_join(sv_thread, NULL);
	pthread_join(cl_thread, NULL);


	return 0;
}

bool hasMessageFromClient() {


	bool aux = (client_message_count > 0);

	return aux;

}

void init_curves(struct bsmp_curve **curves, uint32_t* count){

	printf ("How many curves? ");
	scanf ("%d", count);

	for (int j = 0; j < *count; j++) {

		printf("Building curve %d...\n", j + 1);
		curves[j] = (struct bsmp_curve*) malloc(sizeof(struct bsmp_curve));
		memset (curves[j], 0, sizeof (struct bsmp_curve));

		curves[j]->info.id = j;

		printf ("Is it writable? ");
		scanf("%d", &curves[j]->info.writable);

		printf ("How many bytes does each block contain? ");
		scanf("%d", &curves[j]->info.block_size);

		printf ("How many blocks does it contain? ");
		scanf("%d", &curves[j]->info.nblocks);

		curves[j]->read_block = &read_curve_example;
		curves[j]->write_block = &write_curve_example;

		printf("Type in all the 16 md5 digits: ");
		for (int i = 0; i < 16; i++) {
			printf("Type in %d-th digit: ", i + 1);
			scanf("%02hhX", &curves[j]->info.checksum[i]);
		}
	}

}

void init_variables(struct bsmp_var **variables, uint32_t* count){

	printf ("How many variables? ");
	scanf ("%d", count);

	//variables = (struct bsmp_var**) malloc(*count * sizeof(struct bsmp_var*));

	for (int j = 0; j < *count; j++) {

		printf("Building variable %d...\n", j+1);
		variables[j] = (struct bsmp_var*) malloc(sizeof(struct bsmp_var));
		memset (variables[j], 0, sizeof (struct bsmp_var));

		variables[j]->info.id = j;
		printf ("Is it writable? ");
		scanf("%d", &variables[j]->info.writable);

		printf ("How many bytes does it have? ");
		scanf("%d", &variables[j]->info.size);

		variables[j]->data = (uint8_t*) malloc (variables[j]->info.size * sizeof(uint8_t));
		memset (variables[j]->data, 0, variables[j]->info.size * sizeof(uint8_t));

		for (int i = 0; i < variables[j]->info.size; i++) {
			printf("Type in %d-th byte: ", i + 1);
			scanf("%02hhX", &variables[j]->data[i]);
		}

	}

}

bool read_curve_example(struct bsmp_curve *curve, uint16_t block, uint8_t *data,
		uint16_t *len) {


	return 0;
}

bool write_curve_example(struct bsmp_curve *curve, uint16_t block, uint8_t *data,
		uint16_t len){

	return 0;
}

int client_send_example (uint8_t* data, uint32_t *count) {

	printf("CLIENT send_example - lock mutex...\n");
	pthread_mutex_lock(&lock_buffer);
	printf("CLIENT send_example - lock mutex... ok\n");

	if (*count + 1 > (BUFFER_SIZE - bytes_count)) {

		pthread_mutex_unlock(&lock_buffer);
		return FULL_BUFFER;
	}

	circular_buffer[end] = *count;
	end = (end + 1) % BUFFER_SIZE;
	bytes_count++;

	for (uint32_t i = 0; i < *count; i++ ) {

		circular_buffer[end] = data[i];
		end = (end + 1) % BUFFER_SIZE;
		bytes_count++;
	}


	client_message_count++;

	print_buffer();

	printf("CLIENT send_example - unlock mutex...\n");
	pthread_mutex_unlock(&lock_buffer);
	printf("CLIENT send_example - unlock mutex... ok\n");
	return 0;
}

int client_receive_example (uint8_t* data, uint32_t *count) {

	while (!server_message_count);

	printf("CLIENT receive_example - lock mutex...\n");
	pthread_mutex_lock(&lock_buffer);
	printf("CLIENT receive_example - lock mutex... ok\n");

	print_buffer();

	*count = circular_buffer[start];
	start = (start + 1) % BUFFER_SIZE;
	bytes_count--;


	for (uint32_t i = 0; i < *count; i++ ){

		data[i] = circular_buffer[start];
		start = (start + 1) % BUFFER_SIZE;
		bytes_count--;
	}

	server_message_count--;

	print_buffer();

	printf("CLIENT receive_example - unlock mutex...\n");
	pthread_mutex_unlock(&lock_buffer);
	printf("CLIENT receive_example - unlock mutex... ok\n");
	return 0;
}

void* client_thread (void *arg){


	printf("Entrou thread client...\n");

	// Client instance
	bsmp_client_t *client;

	// Initialize a client instance
	client = (bsmp_client_t*) malloc(sizeof(bsmp_client_t));

	while (!isServerReady);

	printf("Server is ready...\n");

	bsmp_client_init(client, &client_send_example, &client_receive_example);

	printf("Client is ready...\n");

	for (;;) {

		int cmd;

		struct bsmp_var_info *var;
		uint8_t *value;

		// Prepare message to be sent
		struct bsmp_message response, request;

		enum bsmp_err err;

		printf ("Command to be sent to server: ");
		scanf ("%d", cmd);

		switch (cmd) {

		//		case CMD_VAR_QUERY_LIST:

		//			request.code = CMD_VAR_QUERY_LIST;
		//			request.payload_size = 0;
		//
		//			err = command (client, &request, &response);
		//
		//			printf ("Variable's info:\nid = %d");
		//
		//			if (response.code == CMD_VAR_LIST) {
		//
		//				printf ("TO DO");
		//
		//			} else printf ("Deu ruim\n");
		//
		//			break;

		}


	}

	pthread_exit(NULL);
}

void* server_thread (void *arg){

	printf("Entrou server thread...\n");

	// Server instance
	bsmp_server_t *server;

	// Server's variables and curvers
	struct bsmp_var* variables[BSMP_MAX_VARIABLES];
	uint32_t count_variables;

	struct bsmp_curve* curves[BSMP_MAX_VARIABLES];
	uint32_t count_curves;

	init_variables(variables, &count_variables);
	init_curves(curves, &count_curves);

	// Initialize a server instance
	server = (bsmp_server_t*) malloc(sizeof(bsmp_server_t));
	bsmp_server_init(server);

	printf("Server initialization... ok\n");

	enum bsmp_err err;
	for (int i = 0; i < count_variables; i++)
		if ((err = bsmp_register_variable(server, variables[i]))){
			printf("%s\n", bsmp_error_str(err));
			pthread_exit(NULL);
		}

	printf("Variables registration... ok\n");

	for (int i = 0; i < count_curves; i++)
		if ((err = bsmp_register_curve(server, curves[i]))){
			printf("%s\n",bsmp_error_str(err));
			pthread_exit(NULL);
		}

	printf("Curves registration... ok\n");

	isServerReady = true;

	for (;;) {

		if (hasMessageFromClient()) {

			struct bsmp_raw_packet received_packet, to_be_sent_packet;
			server_read_raw_packet (&received_packet);

			to_be_sent_packet.data = (uint8_t*) malloc (256 * sizeof(uint8_t));

			bsmp_process_packet(server, &received_packet, &to_be_sent_packet);

			server_write_raw_packet(&to_be_sent_packet);

		}
	}

	pthread_exit(NULL);
}

void server_read_raw_packet (struct bsmp_raw_packet *received_packet) {

	printf("SERVER read_raw_packet - lock mutex...\n");
	pthread_mutex_lock(&lock_buffer);
	printf("SERVER read_raw_packet - lock mutex... ok\n");

	print_buffer();

	received_packet->len = circular_buffer[start];
	start = (start + 1) % BUFFER_SIZE;
	bytes_count--;

	received_packet->data = (uint8_t*) malloc (received_packet->len * sizeof(uint8_t));

	for (uint16_t i = 0; i < received_packet->len; i++ ) {

		received_packet->data[i] =  circular_buffer[start];
		start = (start + 1) % BUFFER_SIZE;
		bytes_count--;
	}

	client_message_count--;

	printf("SERVER read_raw_packet - unlock mutex...\n");
	pthread_mutex_unlock(&lock_buffer);
	printf("SERVER read_raw_packet - unlock mutex... ok\n");

}


void server_write_raw_packet (struct bsmp_raw_packet *to_be_sent_packet) {

	printf("SERVER write_raw_packet - lock mutex...\n");
	pthread_mutex_lock(&lock_buffer);
	printf("SERVER write_raw_packet - lock mutex... ok\n");

	circular_buffer[end] = to_be_sent_packet->len;
	end = (end + 1) % BUFFER_SIZE;
	bytes_count++;

	for (uint32_t i = 0; i < to_be_sent_packet->len; i++ ) {

		circular_buffer[end] = to_be_sent_packet->data[i];
		end = (end + 1) % BUFFER_SIZE;
		bytes_count++;
	}


	print_buffer();

	server_message_count++;

	printf("SERVER write_raw_packet - unlock mutex...\n");
	pthread_mutex_unlock(&lock_buffer);
	printf("SERVER write_raw_packet - unlock mutex... ok\n");
}


void print_buffer() {

	uint8_t i_start = start;

	while (i_start != end) {

		printf("%02x ", circular_buffer[i_start]);

		i_start = (i_start + 1) % BUFFER_SIZE;
	}

	printf("\n");

}
