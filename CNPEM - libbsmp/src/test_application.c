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

uint8_t circular_buffer[BUFFER_SIZE];
uint8_t start = 0, end = 0, client_message_count = 0, server_message_count = 0;

pthread_mutex_t lock_buffer, lock_buffer_start_end;

bool isBufferEmpty();

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


int main(){


	pthread_mutex_init(&lock_buffer, NULL);

	// Threads
	pthread_t sv_thread, cl_thread;

	if (pthread_create(&sv_thread, NULL, server_thread, NULL)){
		printf ("Server thread creation process has failed.");
		return 1;
	}
	pthread_join(sv_thread, NULL);

	if (pthread_create(&cl_thread, NULL, client_thread, NULL)){
		printf ("Client thread creation process has failed.");
		return 1;
	}

	pthread_join(cl_thread, NULL);


	return 0;
}

bool isBufferEmpty() {
	return (start == end);

}

void init_curves(struct bsmp_curve **curves, uint32_t* count){

	printf ("How many curves? ");
	scanf ("%d", count);

	curves = (struct bsmp_curve**) malloc(sizeof(struct bsmp_curve*));

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
			printf("Type in %d-th byte: ", i + 1);
			scanf("%02hhX", &curves[j]->info.checksum[i]);
		}
	}

}

void init_variables(struct bsmp_var **variables, uint32_t* count){

	printf ("How many variables? ");
	scanf ("%d", count);

	variables = (struct bsmp_var**) malloc(sizeof(struct bsmp_var*));

	for (int j = 0; j < *count; j++) {

		printf("Building variable %d...\n", j);
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

	pthread_mutex_lock(&lock_buffer);

	for (uint32_t i = 0; i < *count; i++ ) {
		pthread_mutex_lock(&lock_buffer_start_end);

		if ((end + 1)%BUFFER_SIZE != start) {
			circular_buffer[end] = data[i];
			end = (end + 1) % BUFFER_SIZE;

			pthread_mutex_unlock(&lock_buffer_start_end);
		}
		else{

			pthread_mutex_unlock(&lock_buffer_start_end);
			pthread_mutex_unlock(&lock_buffer);
			return FULL_BUFFER;
		}
	}


	pthread_mutex_unlock(&lock_buffer);
	return 0;
}

int client_receive_example (uint8_t* data, uint32_t *count) {

	pthread_mutex_lock(&lock_buffer);

	for (uint32_t i = 0; i < *count; i++ ) {


		if (start != end) {
			data[i] = circular_buffer[start];
			start = (start + 1) % BUFFER_SIZE;
		}

		else {
			pthread_mutex_unlock(&lock_buffer);
			return EMPTY_BUFFER;
		}
	}

	pthread_mutex_unlock(&lock_buffer);
	return 0;
}

void* client_thread (void *arg){

	// Client instance
	bsmp_client_t *client;

	// Initialize a client instance
	client = (bsmp_client_t*) malloc(sizeof(bsmp_client_t));
	bsmp_client_init(client, &client_send_example, &client_receive_example);

	for (;;) {

	}

	pthread_exit(NULL);
}

void* server_thread (void *arg){

	// Server instance
	bsmp_server_t *server;

	// Server's variables and curvers
	struct bsmp_var** variables;
	uint32_t count_variables;

	struct bsmp_curve** curves;
	uint32_t count_curves;

	init_variables(variables, &count_variables);
	init_curves(curves, &count_curves);

	// Initialize a server instance
	server = (bsmp_server_t*) malloc(sizeof(bsmp_server_t));
	bsmp_server_init(server);

	enum bsmp_err err;
	for (int i = 0; i < count_variables; i++)
		if ((err = bsmp_register_variable(server, variables[i]))){
			printf("%s\n", bsmp_error_str(err));
			pthread_exit(NULL);
		}

	for (int i = 0; i < count_curves; i++)
		if ((err = bsmp_register_curve(server, curves[i]))){
			printf("%s\n",bsmp_error_str(err));
			pthread_exit(NULL);
		}

	for (;;) {

		if (!isBufferEmpty()) {



		}
	}

	pthread_exit(NULL);
}
