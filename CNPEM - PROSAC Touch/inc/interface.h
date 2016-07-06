/*
 * interface.h
 *
 *  Created on: May 5, 2016
 *      Author: gciotto
 */

#ifndef INTERFACE_H_
#define INTERFACE_H_

#include "stm32746g_discovery_lcd.h"
#include "stm32746g_discovery_ts.h"
#include "stm32746g_discovery.h"
#include "lwip/sockets.h"
#include "cmsis_os.h"

#define BOARD_WIDTH			190
#define BOARD_HEIGTH		70

#define BUTTON_INCREASE_W	35
#define BUTTON_INCREASE_H	25

#define BUTTON_WIDTH		90
#define BUTTON_HEIGTH		30
#define E0_BUTTON_X			25
#define E0_BUTTON_Y			90
#define E1_BUTTON_X			25
#define E1_BUTTON_Y			135
#define C8_BUTTON_X			135
#define C8_BUTTON_Y			90
#define C9_BUTTON_X			135
#define C9_BUTTON_Y			135
#define CB_BUTTON_X			135
#define CB_BUTTON_Y			180
#define CC_BUTTON_X			135
#define CC_BUTTON_Y			225
#define O1_BUTTON_X			25
#define O1_BUTTON_Y			180
#define O5_BUTTON_X			25
#define O5_BUTTON_Y			225
#define CONNECT_BUTTON_X	340
#define CONNECT_BUTTON_Y	27

#define SOCK_TARGET_HOST  	"10.128.47.1"
#define SOCK_TARGET_PORT 	4000

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

enum connection_state {
	DISCONNECTED,
	CONNECTED,
	CONNECTING
};

enum button_index {
	BUTTON_E0 = 0,
	BUTTON_E1,
	BUTTON_01,
	BUTTON_05,
	BUTTON_C8,
	BUTTON_C9,
	BUTTON_CB,
	BUTTON_CC,
	BUTTON_CONNECT
};

enum screen_s {
	BOARD_SELECTION,
	BOARD_SELECTED
};

enum screen_s aScreen;

typedef void (*button_handler_t) (void *source);

typedef struct {

	uint16_t x_pos, y_pos;
	uint8_t isPushed, hasBeenAcknowledged, isEnabled;
	char title[6];
	button_handler_t buttonPressedHandler;

} button_t;

typedef struct {

	uint8_t type;
	char name[9];
	int readBytesCount, writeBytesCount;

	uint32_t background_color, pushed_background_color;

} module_t;

typedef struct {

	uint8_t *readBytes, *writeBytes;
	uint8_t id;

	module_t module;

} board_t;

typedef void (*board_refresh_screen_t) (void* board);

typedef struct {

	board_t board;
	uint16_t x, y;

	uint8_t isEnabled, isPressed, order;

	board_refresh_screen_t refresh_function, draw_initial_screen;

} pboard_t;

#define NUMBER_OF_BUTTONS 	9
#define NUMBER_OF_BOARDS	8

button_t buttons[NUMBER_OF_BUTTONS], buttons_ramp_cycle[8], button_home;

uint8_t pboards_c;
pboard_t pboards [NUMBER_OF_BOARDS];

uint8_t isConnected, isReady, version[6], rampSelected, cycleSelected;
uint16_t ramps[3][1021], digitalIn, analogIn;

pboard_t *selected_board;

SemaphoreHandle_t mutex_handler, mutex_drawer;

int socket_in;
void refresh_LOCON(void* board);
void draw_LOCON(void* board);
void refreshBoardState (TS_StateTypeDef ts_event,  pboard_t *board);
uint8_t isTouchedBoard(TS_StateTypeDef ts_event,  pboard_t *board);
void init_interface(void);
void setTitle(char *title);
void placeButton(button_t button, uint32_t color_back, uint32_t color_text, uint16_t width, uint16_t heigth);
void paintPushedButton (button_t button, uint16_t width, uint16_t heigth);
void unpaintPushedButton (button_t button, uint16_t width, uint16_t heigth);
uint8_t isTouched(TS_StateTypeDef ts_event,  button_t *button, uint16_t width, uint16_t heigth);
void refreshButtonState (TS_StateTypeDef ts_event,  button_t *button, uint16_t width, uint16_t heigth);
void refreshConnectState(TS_StateTypeDef ts_event);
void buttonConnectPressed();
void setStatus(enum connection_state isConnected);
void setConnectedState(uint8_t isConnected);
void setVersion (uint8_t *version);
void executeCommand(enum command command);
void print_new_cycle();
void print_new_ramp();
void generate_ramps();

#endif /* INTERFACE_H_ */
