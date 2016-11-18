/*
 * Required function prototypes to run a PROSAC client.
 *
 * prosac.h
 *
 *  Created on: May 5, 2016
 *  Author: Gustavo Ciotto Pinton
 */

#ifndef INTERFACE_H_
#define INTERFACE_H_

#include <string.h>

#include "stm32746g_discovery_lcd.h"
#include "stm32746g_discovery_ts.h"
#include "stm32746g_discovery.h"
#include "globals.h"

/* Interface is basically composed of two screens:
 *   - HOME SCREEN or BOARD_SELECTION: allows to connect/disconnect to PROSAC host and
 *   to select one of the 4 available boards.
 *   - BOARD_SELECTED: displays information about the board and allows sending commands to it. */

/* Dimensions and coordinates of all GUI elements */
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

/* maximum number of connected boards  */
#define NUMBER_OF_BOARDS	8

/* Currently available command buttons */
enum button_index {
	BUTTON_E0 = 0,
	BUTTON_E1,
	BUTTON_01,
	BUTTON_05,
	BUTTON_C8,
	BUTTON_C9,
	BUTTON_CB,
	BUTTON_CC,
	BUTTON_CONNECT,

	BUTTON_COUNT
};

/* prototype for a button event handler. The parameter source is a pointer
 * to the button which fired the event */
typedef void (*button_handler_t) (void *source);
/* prototype for a board refreshing function. To be implemented according to the details
 * of each board */
typedef void (*board_refresh_screen_t) (void* board);

/* a struct describing a button */
typedef struct {

	uint16_t x_pos, y_pos;
	/* hasBeenAcknowledged holds if event has been already acknowledged or not */
	uint8_t isPushed, hasBeenAcknowledged, isEnabled;
	char title[6];

	button_handler_t buttonEventHandler;

} button_t;

/* GUI representation of a board_t struct */
typedef struct {

	board_t board;
	uint16_t x, y;
	uint8_t isEnabled, isPushed, order;

	board_refresh_screen_t refresh_function, draw_initial_screen;

} pboard_t;

/* GUI-related global variables  */
button_t buttons[BUTTON_COUNT], buttons_ramp_cycle[8], button_home;
pboard_t *selected_board;

uint8_t pboards_c;
pboard_t pboards [NUMBER_OF_BOARDS];

/* Initialization prototypes */
void initializeGUI(void);
void drawHomeScreen();
void backToHomeScreen (void* button);

/* Prototypes of board-specific functions */
void refreshLOCON(void* board);
void drawLOCON(void* board);

/* Prototypes of field setting functions */
void setTitle(char *title);
void setStatus(enum connection_state isConnected);
void setConnectedState(uint8_t isConnected);
void setVersion (uint8_t *version);
void setReceivedData();

/* Painting prototypes */
void paintPushedButton (button_t button, uint16_t width, uint16_t heigth);
void unpaintPushedButton (button_t button, uint16_t width, uint16_t heigth);
void paintPushedBoard(pboard_t *board);
void unpaintPushedBoard(pboard_t *board);
void repaintButtons();

/* Positioning prototypes */
void placeBoard(pboard_t *board, uint32_t color_back, uint32_t color_text);
void placeButton(button_t button, uint32_t color_back, uint32_t color_text, uint16_t width, uint16_t heigth);

/* Prototypes of functions which are called to refresh an element state */
void refreshBoardState (TS_StateTypeDef ts_event,  pboard_t *board);
void refreshButtonState (TS_StateTypeDef ts_event,  button_t *button, uint16_t width, uint16_t heigth);
void refreshConnectState(TS_StateTypeDef ts_event);
void refreshBoards();

/* Event handlers prototypes */
void buttonConnectPressed();

/* Prototypes of checking functions */
uint8_t isTouchedBoard(TS_StateTypeDef ts_event,  pboard_t *board);
uint8_t isTouched(TS_StateTypeDef ts_event,  button_t *button, uint16_t width, uint16_t heigth);

/* Prototypes of printing new IDs */
void printNewCycleID();
void printNewRampID();
void printNewAnalogInput();
void printNewDigitalInput();

/* Miscellaneous */
void addBoard(module_t module, int id);
void resetBoards();

/* Processing commands prototypes */
void processCommand(enum command command, uint8_t *buffer, uint16_t length);
void prepareCommand(enum command command, uint8_t** buffer, uint16_t *length);
void executeCommand(enum command command);
void receiveCommand(uint8_t *buffer, uint16_t *length);
void sendCommand(uint8_t* buffer, uint16_t length);

/* Event handlers */
void eventHandlerE0 (void* button);
void eventHandlerE1 (void* button);
void eventHandlerConfirm (void* button);
void eventHandlerAdjust (void* button);
void eventHandlerC8 (void* button);
void eventHandlerC9 (void* button);
void eventHandlerCB (void* button);
void eventHandlerCC (void* button);
void increaseRampID (void* button);
void increaseCycleID (void* button);
void decreaseRampID (void* button);
void decreaseCycleID (void* button);
void increaseAnalogInput (void* button);
void decreaseAnalogInput (void* button);
void decreaseDigitalInput (void* button);
void increaseDigitalInput (void* button);
#endif /* INTERFACE_H_ */
