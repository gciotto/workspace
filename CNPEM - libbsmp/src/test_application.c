#include "../include/server.h"
#include "../include/client.h"
#include "../include/bsmp.h"
#include "server_priv.h"
#include "bsmp_priv.h"

#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <stdio.h>
#include <math.h>
#include <pthread.h>

#define FULL_BUFFER 1
#define EMPTY_BUFFER 2
#define BUFFER_SIZE 256
#define BLOCK_SIZE 8
#define EXIT 0xff

enum print_origin_event {

	CLIENT_SEND,
	CLIENT_RECEIVE,
	SERVER_SEND,
	SERVER_RECEIVE

};

uint8_t circular_buffer[BUFFER_SIZE];
uint8_t start = 0, end = 0, client_message_count = 0, server_message_count = 0, bytes_count = 0;

pthread_mutex_t lock_buffer;

bool isServerReady = false;
bool client_finished = false;

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

void print_buffer(enum print_origin_event origin_event);

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

	return (client_message_count > 0);
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

		// printf ("How many bytes does each block contain? ");
		// scanf("%d", &curves[j]->info.block_size);

		curves[j]->info.block_size = BLOCK_SIZE; // each block is a double

		printf ("How many blocks does it contain? ");
		scanf("%d", &curves[j]->info.nblocks);

		curves[j]->read_block = &read_curve_example;
		curves[j]->write_block = &write_curve_example;

		/*printf("Type in all the 16 md5 digits.... \n");
		for (int i = 0; i < 16; i++) {
			printf("Type in %do. digit:.... ", i + 1);
			scanf("%02hhX", &curves[j]->info.checksum[i]);
		}*/

	}

}

void init_variables(struct bsmp_var **variables, uint32_t* count){

	printf ("How many variables?..... ");
	scanf ("%d", count);

	//variables = (struct bsmp_var**) malloc(*count * sizeof(struct bsmp_var*));

	for (int j = 0; j < *count; j++) {

		printf("Building variable %d...\n", j+1);
		variables[j] = (struct bsmp_var*) malloc(sizeof(struct bsmp_var));
		memset (variables[j], 0, sizeof (struct bsmp_var));

		variables[j]->info.id = j;
		printf ("Writable? ..... ");
		scanf("%d", &variables[j]->info.writable);

		printf ("How many bytes?..... ");
		scanf("%d", &variables[j]->info.size);

		variables[j]->data = (uint8_t*) malloc (variables[j]->info.size * sizeof(uint8_t));
		memset (variables[j]->data, 0, variables[j]->info.size * sizeof(uint8_t));

		for (int i = 0; i < variables[j]->info.size; i++) {
			printf("Type in %do. byte: ", i + 1);
			scanf("%02hhX", &variables[j]->data[i]);
		}

	}

}

bool read_curve_example(struct bsmp_curve *curve, uint16_t block, uint8_t *data,
		uint16_t *len) {

	*len = curve->info.block_size;

	union {
		double sin_value;
		uint8_t sin_bytes[8];
	} block_aux;


	block_aux.sin_value = sin(2*M_PI*((double)block/(double)curve->info.nblocks));

	printf ("read_curve_example:.... %.6f\n", block_aux.sin_value);

	for (int i = 0; i < BLOCK_SIZE; i++)
		data[i] = block_aux.sin_bytes[i];

	return 1;
}

bool write_curve_example(struct bsmp_curve *curve, uint16_t block, uint8_t *data,
		uint16_t len){

	union {
		double cos_value;
		uint8_t cos_bytes[8];
	} block_aux;

	for (int i = 0; i < BLOCK_SIZE; i++)
		block_aux.cos_bytes[i] = data[i];

	printf("write_curve_example wrote successfully (%.4f)....\n", block_aux.cos_value);

	return 1;
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

	print_buffer(CLIENT_SEND);

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

	print_buffer(CLIENT_RECEIVE);

	*count = circular_buffer[start];
	start = (start + 1) % BUFFER_SIZE;
	bytes_count--;


	for (uint32_t i = 0; i < *count; i++ ){

		data[i] = circular_buffer[start];
		start = (start + 1) % BUFFER_SIZE;
		bytes_count--;
	}

	server_message_count--;

	printf("CLIENT receive_example - unlock mutex...\n");
	pthread_mutex_unlock(&lock_buffer);
	printf("CLIENT receive_example - unlock mutex... ok\n");
	return 0;
}

void* client_thread (void *arg){


	printf("\nCLIENT thread initialized... ok\n");

	// Client instance
	bsmp_client_t *client;

	// Initialize a client instance
	client = (bsmp_client_t*) malloc(sizeof(bsmp_client_t));

	while (!isServerReady);

	printf("SERVER initialized... ok\n");

	bsmp_client_init(client, &client_send_example, &client_receive_example);

	printf("CLIENT initialized... ok\n");

	for (;;) {

		int cmd, id;
		uint8_t *value;

		struct bsmp_group_list *list_query;
		struct bsmp_curve_info curve;
		uint16_t curve_offset;
		uint32_t len;

		struct bsmp_var_info **list;

		union {
			double val;
			uint8_t array[8];
		} curve_block;

		enum bsmp_err err;

		printf ("Command to be sent to server:..... ");
		scanf ("%x", &cmd);

		switch (cmd) {

		case CMD_VAR_READ:

			printf ("Variable's ID:...");
			scanf("%d", &id);

			value = (uint8_t*) malloc (client->vars.list[id].size * sizeof(uint8_t));

			if ((err = bsmp_read_var(client, &client->vars.list[id], value)))
				printf("%s\n", bsmp_error_str(err));
			else {
				printf ("Variable (ID %d) value.....: ", id);
				for (uint8_t i = 0; i < client->vars.list[id].size; i++)
					printf ("%02x ", value[i]);
				printf("\n");
			}

			break;

		case CMD_VAR_WRITE:

			printf ("Variable's ID:...");
			scanf("%d", &id);

			value = (uint8_t*) malloc (client->vars.list[id].size * sizeof(uint8_t));

			for (uint8_t i = 0; i < client->vars.list[id].size; i++) {
				printf ("Type in %io. byte.....: ", i+1);
				scanf ("%02hhX", &value[i]);
			}

			if ((err = bsmp_write_var(client, &client->vars.list[id], value)))
				printf("%s\n", bsmp_error_str(err));
			else printf ("Variable (ID %d) has been changed.....\n", id);


			break;

		case CMD_GROUP_QUERY_LIST:

			list_query = (struct bsmp_group_list*) malloc (sizeof(struct bsmp_group_list));

			if ((err = bsmp_get_groups_list(client, &list_query)))
				printf("%s\n", bsmp_error_str(err));

			else
				for (int i = 0; i < list_query->count; i++){
					printf ("Group (ID %d) writable (%d) contains %d byte (s).....\n",
							list_query->list[i].id, list_query->list[i].writable, list_query->list[i].size);
				}

			break;

		case CMD_GROUP_QUERY:

			printf ("Group's ID:...");
			scanf("%d", &id);

			value = (uint8_t*) malloc (client->groups.list[id].size * sizeof(uint8_t));

			if (err = bsmp_read_group (client, &client->groups.list[id], value))
				printf("%s\n", bsmp_error_str(err));

			else
				for (int i = 0, j = 0; i < client->groups.list[id].vars.count; i++) {

					struct bsmp_var_info *var_info = client->groups.list[id].vars.list[i];
					printf ("Variable (ID %d): ....", var_info->id);

					for (int k = 0; k < var_info->size ;j++, k++)
						printf ("%02x ", value[j]);
					printf("\n");
				}

			break;

		case CMD_GROUP_CREATE:

			printf ("# of variables of the new group:.....");
			scanf ("%d", &len);

			list = (struct bsmp_var_info**)
							malloc (len * sizeof(struct bsmp_var_info*));

			for (int i = 0; i < len; i++) {
				printf("Variable ID:....");
				scanf ("%d", &id);
				list[i] = &client->vars.list[id];
			}


			if ((err = bsmp_create_group (client, list)))
				printf("%s\n", bsmp_error_str(err));

			break;

		case CMD_CURVE_BLOCK_REQUEST:

			printf ("Curve's ID:...");
			scanf("%d", &id);

			printf ("Offset....: ");
			scanf("%d", &curve_offset);

			value = (uint8_t*) malloc (client->curves.list[id].block_size * sizeof(uint8_t));

			if ((err = bsmp_request_curve_block (client, &client->curves.list[id],
					curve_offset, &curve_block.array, &len)))
				printf("%s\n", bsmp_error_str(err));

			else {

				printf("Curve (ID %d) block n. %d:..... ", id, curve_offset);

				for (uint16_t i = 0; i < len; i++)
					printf ("%02x ", curve_block.array[i]);
				printf("(%.4f)\n", curve_block.val);
			}

			break;

		case CMD_CURVE_BLOCK:

			printf ("Curve's ID:...");
			scanf("%d", &id);

			printf ("Offset....: ");
			scanf("%d", &curve_offset);

			curve_block.val = cos (2*M_PI*curve_offset/client->curves.list[id].nblocks);

			if  ((err = bsmp_send_curve_block (client, &client->curves.list[id], curve_offset,
					&curve_block.array, BLOCK_SIZE)))
				printf("%s\n", bsmp_error_str(err));
			else printf("write_curve_example wrote successfully (%.4f)....\n", curve_block.val);

			break;


		case EXIT:
			printf("CLIENT has ended.... ok\n");
			client_finished = true;
			pthread_exit(NULL);

		}


	}

	pthread_exit(NULL);
}

void* server_thread (void *arg){

	printf("\nSERVER thread initialized... ok\n");

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

	for (;!client_finished;) {

		if (hasMessageFromClient()) {

			struct bsmp_raw_packet received_packet, to_be_sent_packet;
			server_read_raw_packet (&received_packet);

			to_be_sent_packet.data = (uint8_t*) malloc (256 * sizeof(uint8_t));

			bsmp_process_packet(server, &received_packet, &to_be_sent_packet);

			server_write_raw_packet(&to_be_sent_packet);

		}
	}

	printf("SERVER has ended.... ok\n");
	pthread_exit(NULL);
}

void server_read_raw_packet (struct bsmp_raw_packet *received_packet) {

	printf("SERVER read_raw_packet - lock mutex...\n");
	pthread_mutex_lock(&lock_buffer);
	printf("SERVER read_raw_packet - lock mutex... ok\n");

	print_buffer(SERVER_RECEIVE);

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

	print_buffer(SERVER_SEND);

	server_message_count++;

	printf("SERVER write_raw_packet - unlock mutex...\n");
	pthread_mutex_unlock(&lock_buffer);
	printf("SERVER write_raw_packet - unlock mutex... ok\n");
}


void print_buffer(enum print_origin_event origin_event) {

	switch (origin_event) {
	case CLIENT_RECEIVE: printf ("RECEIVED by CLIENT - "); break;
	case CLIENT_SEND: printf ("SENT by CLIENT - "); break;
	case SERVER_RECEIVE: printf ("RECEIVED by SERVER - "); break;
	case SERVER_SEND: printf ("SENT by SERVER - "); break;
	}

	uint8_t i_start = start;

	while (i_start != end) {

		printf("%02x ", circular_buffer[i_start]);

		i_start = (i_start + 1) % BUFFER_SIZE;
	}

	printf("\n");
}
