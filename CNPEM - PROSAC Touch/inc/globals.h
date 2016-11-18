/*
 *  globals.h
 *
 *	Global definitions.
 *  Created on: 17 de nov de 2016
 *  Author: Gustavo Ciotto Pinton
 */

#ifndef GLOBALS_H_
#define GLOBALS_H_

#include "cmsis_os.h"
#include "lwip/sockets.h"


/* - mutex_handler prevents two execute commands from being processed at the same time.
 * - mutex_drawer prevents a screen refresh operation from being interrupted before its end. */
SemaphoreHandle_t mutex_handler, mutex_drawer;

/* IP address and port configured in PROSAC host */
#define SOCK_TARGET_HOST  	"10.128.47.5"
#define SOCK_TARGET_PORT 	4000

/* PROSAC supported commands */
enum command {
	NORMAL 				= 0x00,
	ADJUST,
	IDENT,
	END_IDENT,
	BOOT,
	MSG_CONFIRM,
	RAMP_INIT 			= 0xC8,
	RAMP_BLOCK,
	RAMP_ASK_BLOCK,
	RAMP_ENABLE,
	RAMP_ABORT,
	RAMP_ABORTED,
	RAMP_COMPLETED,
	ENABLE_READINGS,
	RAMP_ENABLE_CYCLIC,
	CYCLE_ENABLE 		= 0xE0,
	CYCLE_ABORT,
	CYCLE_ABORTED,
	CYCLE_COMPLETED
};

/* enumeration type representing all possible states of a connection */
enum connection_state {
	DISCONNECTED,
	CONNECTED,
	CONNECTING
};

/* application is either on BOARD_SELECTION screen or BOARD_SELECTED screen */
enum screen_s {
	BOARD_SELECTION,
	BOARD_SELECTED
};

/* a struct describing a board module to be drawn in home screen */
typedef struct {

	uint8_t type;
	char name[9];
	int readBytesCount, writeBytesCount;

	uint32_t background_color, pushed_background_color;

} module_t;

/* board_t struct contains the current data of a detected board  */
typedef struct {

	uint8_t *readBytes, *writeBytes;
	uint8_t id;

	module_t module;

} board_t;

extern const module_t NONE, LCN12BMP, LCN12BB, LCN16BMP , LCN16BBP, MUX16BBP;

/* global variable with current screen selection */
enum screen_s aScreen;

/* 3 default ramp curves are provided */
uint16_t ramps[3][1021], digitalIn, analogIn;

/* Execution control variables */
uint8_t isConnected, isReady, version[6], rampSelected, cycleSelected;

/* TCP socket descriptor of PROSAC connection */
int socket_in;

/* Prototypes of provided functions  */
void generate_ramps();
module_t findModule(uint8_t code);

#endif /* GLOBALS_H_ */
