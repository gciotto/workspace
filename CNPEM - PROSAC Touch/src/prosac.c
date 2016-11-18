/*
 * Source files providing functions to handle GUI events and run a PROSAC client.
 *
 * Author: Gustavo Ciotto Pinton
 */
#include "prosac.h"

void initializeGUI(void) {

	BSP_LCD_Init();

	BSP_LCD_LayerDefaultInit(0, LCD_FB_START_ADDRESS);

	BSP_LCD_DisplayOn();

	BSP_LCD_SelectLayer(0);

	BSP_LCD_Clear(LCD_COLOR_WHITE);

	BSP_LCD_SetTransparency(0, 200);

	BSP_LCD_SetFont(&Font20);

	button_home.x_pos = 5;
	button_home.y_pos = 5;
	button_home.hasBeenAcknowledged = 0;
	button_home.isEnabled = 1;
	button_home.isPushed = 0;
	memcpy(button_home.title, "<Home", 6);
	button_home.buttonEventHandler = backToHomeScreen;

	buttons[BUTTON_E0].x_pos = E0_BUTTON_X;
	buttons[BUTTON_E0].y_pos = E0_BUTTON_Y;
	buttons[BUTTON_E0].isPushed = 0;
	buttons[BUTTON_E0].hasBeenAcknowledged = 0;
	buttons[BUTTON_E0].isEnabled = 0;
	buttons[BUTTON_E0].buttonEventHandler = eventHandlerE0;
	memcpy(buttons[BUTTON_E0].title, "0xE0\0", 5);

	buttons[BUTTON_E1].x_pos = E1_BUTTON_X;
	buttons[BUTTON_E1].y_pos = E1_BUTTON_Y;
	buttons[BUTTON_E1].isPushed = 0;
	buttons[BUTTON_E1].hasBeenAcknowledged = 0;
	buttons[BUTTON_E1].buttonEventHandler = eventHandlerE1;
	memcpy(buttons[BUTTON_E1].title, "0xE1\0", 5);
	buttons[BUTTON_E1].isEnabled = 0;

	buttons[BUTTON_01].x_pos = O1_BUTTON_X;
	buttons[BUTTON_01].y_pos = O1_BUTTON_Y;
	buttons[BUTTON_01].isPushed = 0;
	buttons[BUTTON_01].isEnabled = 0;
	buttons[BUTTON_01].hasBeenAcknowledged = 0;
	buttons[BUTTON_01].buttonEventHandler = eventHandlerAdjust;
	memcpy(buttons[BUTTON_01].title, "0x01\0", 5);

	buttons[BUTTON_05].x_pos = O5_BUTTON_X;
	buttons[BUTTON_05].y_pos = O5_BUTTON_Y;
	buttons[BUTTON_05].isPushed = 0;
	buttons[BUTTON_05].hasBeenAcknowledged = 0;
	buttons[BUTTON_05].isEnabled = 0;
	buttons[BUTTON_05].buttonEventHandler = eventHandlerConfirm;
	memcpy(buttons[BUTTON_05].title, "0x05\0", 5);

	buttons[BUTTON_C8].x_pos = C8_BUTTON_X;
	buttons[BUTTON_C8].y_pos = C8_BUTTON_Y;
	buttons[BUTTON_C8].isPushed = 0;
	buttons[BUTTON_C8].hasBeenAcknowledged = 0;
	buttons[BUTTON_C8].isEnabled = 0;
	buttons[BUTTON_C8].buttonEventHandler = eventHandlerC8;
	memcpy(buttons[BUTTON_C8].title, "0xC8\0", 5);

	buttons[BUTTON_C9].x_pos = C9_BUTTON_X;
	buttons[BUTTON_C9].y_pos = C9_BUTTON_Y;
	buttons[BUTTON_C9].isPushed = 0;
	buttons[BUTTON_C9].hasBeenAcknowledged = 0;
	buttons[BUTTON_C9].isEnabled = 0;
	buttons[BUTTON_C9].buttonEventHandler = eventHandlerC9;
	memcpy(buttons[BUTTON_C9].title, "0xC9\0", 5);

	buttons[BUTTON_CB].x_pos = CB_BUTTON_X;
	buttons[BUTTON_CB].y_pos = CB_BUTTON_Y;
	buttons[BUTTON_CB].isPushed = 0;
	buttons[BUTTON_CB].hasBeenAcknowledged = 0;
	buttons[BUTTON_CB].isEnabled = 0;
	buttons[BUTTON_CB].buttonEventHandler = eventHandlerCB;
	memcpy(buttons[BUTTON_CB].title, "0xCB\0", 5);

	buttons[BUTTON_CC].x_pos = CC_BUTTON_X;
	buttons[BUTTON_CC].y_pos = CC_BUTTON_Y;
	buttons[BUTTON_CC].isPushed = 0;
	buttons[BUTTON_CC].hasBeenAcknowledged = 0;
	buttons[BUTTON_CC].isEnabled = 0;
	buttons[BUTTON_CC].buttonEventHandler = eventHandlerCC;
	memcpy(buttons[BUTTON_CC].title, "0xCC\0", 5);

	buttons[BUTTON_CONNECT].x_pos = CONNECT_BUTTON_X;
	buttons[BUTTON_CONNECT].y_pos = CONNECT_BUTTON_Y;
	buttons[BUTTON_CONNECT].isPushed = 0;
	buttons[BUTTON_CONNECT].hasBeenAcknowledged = 0;
	buttons[BUTTON_CONNECT].buttonEventHandler = buttonConnectPressed;
	buttons[BUTTON_CONNECT].isEnabled = 1;
	memcpy(buttons[BUTTON_CONNECT].title, "Conn.\0", 6);

	buttons_ramp_cycle[0].x_pos = 400;
	buttons_ramp_cycle[0].y_pos = 205;
	buttons_ramp_cycle[0].isPushed = 0;
	buttons_ramp_cycle[0].hasBeenAcknowledged= 0;
	buttons_ramp_cycle[0].isEnabled = 0;
	buttons_ramp_cycle[0].buttonEventHandler = decreaseRampID;
	memcpy(buttons_ramp_cycle[0].title, "<<", 2);

	buttons_ramp_cycle[1].x_pos = 440;
	buttons_ramp_cycle[1].y_pos = 205;
	buttons_ramp_cycle[1].isPushed = 0;
	buttons_ramp_cycle[1].isEnabled = 0;
	buttons_ramp_cycle[1].hasBeenAcknowledged= 0;
	buttons_ramp_cycle[1].buttonEventHandler = increaseRampID;
	memcpy(buttons_ramp_cycle[1].title, ">>", 2);

	buttons_ramp_cycle[2].x_pos = 400;
	buttons_ramp_cycle[2].y_pos = 235;
	buttons_ramp_cycle[2].isPushed = 0;
	buttons_ramp_cycle[2].hasBeenAcknowledged= 0;
	buttons_ramp_cycle[2].buttonEventHandler = decreaseCycleID;
	buttons_ramp_cycle[2].isEnabled = 0;
	memcpy(buttons_ramp_cycle[2].title, "<<", 2);

	buttons_ramp_cycle[3].x_pos = 440;
	buttons_ramp_cycle[3].y_pos = 235;
	buttons_ramp_cycle[3].isPushed = 0;
	buttons_ramp_cycle[3].isEnabled = 0;
	buttons_ramp_cycle[3].hasBeenAcknowledged= 0;
	buttons_ramp_cycle[3].buttonEventHandler = increaseCycleID;
	memcpy(buttons_ramp_cycle[3].title, ">>", 2);

	buttons_ramp_cycle[4].x_pos = 400;
	buttons_ramp_cycle[4].y_pos = 145;
	buttons_ramp_cycle[4].isPushed = 0;
	buttons_ramp_cycle[4].hasBeenAcknowledged = 0;
	buttons_ramp_cycle[4].buttonEventHandler = decreaseAnalogInput;
	buttons_ramp_cycle[4].isEnabled = 0;
	memcpy(buttons_ramp_cycle[4].title, "<<", 2);

	buttons_ramp_cycle[5].x_pos = 440;
	buttons_ramp_cycle[5].y_pos = 145;
	buttons_ramp_cycle[5].isPushed = 0;
	buttons_ramp_cycle[5].isEnabled = 0;
	buttons_ramp_cycle[5].hasBeenAcknowledged= 0;
	buttons_ramp_cycle[5].buttonEventHandler = increaseAnalogInput;
	memcpy(buttons_ramp_cycle[5].title, ">>", 2);

	buttons_ramp_cycle[6].x_pos = 400;
	buttons_ramp_cycle[6].y_pos = 175;
	buttons_ramp_cycle[6].isPushed = 0;
	buttons_ramp_cycle[6].hasBeenAcknowledged = 0;
	buttons_ramp_cycle[6].buttonEventHandler = decreaseDigitalInput;
	buttons_ramp_cycle[6].isEnabled = 0;
	memcpy(buttons_ramp_cycle[6].title, "<<", 2);

	buttons_ramp_cycle[7].x_pos = 440;
	buttons_ramp_cycle[7].y_pos = 175;
	buttons_ramp_cycle[7].isPushed = 0;
	buttons_ramp_cycle[7].isEnabled = 0;
	buttons_ramp_cycle[7].hasBeenAcknowledged= 0;
	buttons_ramp_cycle[7].buttonEventHandler = increaseDigitalInput;
	memcpy(buttons_ramp_cycle[7].title, ">>", 2);

	pboards_c = 0;
	for (int i = 0; i < NUMBER_OF_BOARDS; i++){
		pboards[i].board.module = NONE;

		pboards[i].isEnabled = 0;
		pboards[i].isPushed = 0;
		pboards[i].order = (uint8_t) i;
	}

	pboards[0].x = 25;
	pboards[0].y = 90;

	pboards[1].x = 25;
	pboards[1].y = 180;

	pboards[2].x = 240;
	pboards[2].y = 90;

	pboards[3].x = 240;
	pboards[3].y = 180;

	BSP_TS_Init(BSP_LCD_GetXSize(), BSP_LCD_GetYSize());

	aScreen = BOARD_SELECTION;

	drawHomeScreen();

	isReady = 0;
}

void drawHomeScreen(){

	setTitle("Cliente PROSAC");

	setStatus(isConnected);

	uint32_t color_connect;
	if (isConnected)
		color_connect = LCD_COLOR_LIGHTRED;
	else color_connect = LCD_COLOR_LIGHTGREEN;

	placeButton(buttons[BUTTON_CONNECT], color_connect, LCD_COLOR_BLACK, BUTTON_WIDTH, BUTTON_HEIGTH);

	placeBoard(&pboards[0], pboards[0].board.module.background_color, LCD_COLOR_BLACK);
	placeBoard(&pboards[1], pboards[1].board.module.background_color, LCD_COLOR_BLACK);
	placeBoard(&pboards[2], pboards[2].board.module.background_color, LCD_COLOR_BLACK);
	placeBoard(&pboards[3], pboards[3].board.module.background_color, LCD_COLOR_BLACK);

}

void backToHomeScreen (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	aScreen = BOARD_SELECTION;

	selected_board->board.writeBytes[1] = 128;
	cycleSelected = 128;
	rampSelected = 128;

	BSP_LCD_Clear(LCD_COLOR_WHITE);

	setVersion(version);

	drawHomeScreen();
}

void drawLOCON(void* board) {

	pboard_t *aBoard = (pboard_t*) board;

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_Clear(LCD_COLOR_WHITE);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);

	BSP_LCD_SetFont(&Font24);

	BSP_LCD_SetTextColor(LCD_COLOR_BLUE);

	char buffer[3];
	memset(buffer, 0, 3);
	sprintf (buffer,"%d", aBoard->board.id);

	BSP_LCD_DisplayStringAt(30, 45, (uint8_t*) buffer, LEFT_MODE);

	BSP_LCD_SetTextColor(aBoard->board.module.background_color);
	BSP_LCD_DisplayStringAt(55, 45, (uint8_t*) aBoard->board.module.name, LEFT_MODE);

	BSP_LCD_SetFont(&Font20);

	BSP_LCD_SetTextColor(LCD_COLOR_RED);

	BSP_LCD_DisplayStringAt(250, 30, (uint8_t*) "SAIDAS ", LEFT_MODE);

	BSP_LCD_SetTextColor(LCD_COLOR_BLACK);

	BSP_LCD_DisplayStringAt(250, 60, (uint8_t*) "Analog. : ", LEFT_MODE);
	BSP_LCD_DisplayStringAt(250, 90, (uint8_t*) "Digital : ", LEFT_MODE);

	BSP_LCD_SetTextColor(LCD_COLOR_BLUE);

	BSP_LCD_DisplayStringAt(250, 120, (uint8_t*) "ENTRADAS ", LEFT_MODE);

	BSP_LCD_SetTextColor(LCD_COLOR_BLACK);

	BSP_LCD_DisplayStringAt(250, 150, (uint8_t*) "An : 2000 ", LEFT_MODE);
	BSP_LCD_DisplayStringAt(250, 180, (uint8_t*) "D  : 56 ", LEFT_MODE);

	xSemaphoreGive(mutex_drawer);

	buttons_ramp_cycle[4].isEnabled = 1;
	buttons_ramp_cycle[5].isEnabled = 1;
	placeButton(buttons_ramp_cycle[4], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W,BUTTON_INCREASE_H);
	placeButton(buttons_ramp_cycle[5], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W,BUTTON_INCREASE_H);

	buttons_ramp_cycle[6].isEnabled = 1;
	buttons_ramp_cycle[7].isEnabled = 1;
	placeButton(buttons_ramp_cycle[6], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W,BUTTON_INCREASE_H);
	placeButton(buttons_ramp_cycle[7], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W,BUTTON_INCREASE_H);

	digitalIn = 56;
	analogIn  = 2000;

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);
	BSP_LCD_SetTextColor(LCD_COLOR_GREEN);

	rampSelected = 128;
	BSP_LCD_DisplayStringAt(250, 210, (uint8_t*) "RAMPA   : ", LEFT_MODE);

	BSP_LCD_SetTextColor(LCD_COLOR_RED);
	BSP_LCD_DisplayStringAt(380, 210, (uint8_t*) "N ", LEFT_MODE);

	xSemaphoreGive(mutex_drawer);

	buttons_ramp_cycle[0].isEnabled = 0;
	buttons_ramp_cycle[1].isEnabled = 1;
	placeButton(buttons_ramp_cycle[0], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W,BUTTON_INCREASE_H);
	placeButton(buttons_ramp_cycle[1], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W,BUTTON_INCREASE_H);

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);
	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);
	BSP_LCD_SetTextColor(LCD_COLOR_ORANGE);

	BSP_LCD_DisplayStringAt(250, 240, (uint8_t*) "CICLAGEM: ", LEFT_MODE);

	cycleSelected = 128;
	BSP_LCD_SetTextColor(LCD_COLOR_RED);
	BSP_LCD_DisplayStringAt(380, 240, (uint8_t*) "N ", LEFT_MODE);

	xSemaphoreGive(mutex_drawer);

	buttons_ramp_cycle[2].isEnabled = 0;
	buttons_ramp_cycle[3].isEnabled = 2;
	placeButton(buttons_ramp_cycle[2], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W,BUTTON_INCREASE_H);
	placeButton(buttons_ramp_cycle[3], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W,BUTTON_INCREASE_H);

	buttons[BUTTON_E0].isEnabled = 1;
	buttons[BUTTON_01].isEnabled = 1;
	buttons[BUTTON_05].isEnabled = 1;
	buttons[BUTTON_C8].isEnabled = 1;

	placeButton(buttons[BUTTON_E0], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_E1], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_01], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_05], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_C8], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_C9], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_CB], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_CC], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);

	placeButton(button_home, LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);

}

void refreshLOCON(void* board) {

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	pboard_t *aBoard = (pboard_t*) board;

	BSP_LCD_SetTextColor(LCD_COLOR_WHITE);
	BSP_LCD_FillRect(390, 60, 80, 20);

	BSP_LCD_FillRect(390, 90, 80, 20);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);
	BSP_LCD_SetTextColor(LCD_COLOR_BLACK);

	char buffer[8];

	int t = aBoard->board.readBytes[2] << 8, t2 = aBoard->board.readBytes[3];

	memset (buffer, 0, 8);
	sprintf(buffer, "%d", t + t2);
	BSP_LCD_DisplayStringAt(390, 60, (uint8_t*) buffer, LEFT_MODE);

	memset (buffer, 0, 8);
	sprintf(buffer, "%d", aBoard->board.readBytes[4]);

	BSP_LCD_DisplayStringAt(390, 90, (uint8_t*) buffer, LEFT_MODE);

	aBoard->board.writeBytes[0] = 0;
	aBoard->board.writeBytes[1] = cycleSelected;

	/* Teste */
	aBoard->board.writeBytes[2] = (analogIn >> 8) & 0xFF;
	aBoard->board.writeBytes[3] = analogIn & 0xFF;
	aBoard->board.writeBytes[4] = digitalIn;

	xSemaphoreGive(mutex_drawer);

}

void setTitle(char *title) {

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);

	BSP_LCD_SetTextColor(LCD_COLOR_WHITE);

	BSP_LCD_FillRect(0, 10, BSP_LCD_GetXSize(), 20);

	BSP_LCD_SetTextColor(LCD_COLOR_BLACK);

	BSP_LCD_DisplayStringAt(0, 5, (uint8_t*) title, CENTER_MODE);

	xSemaphoreGive(mutex_drawer);
}

void setStatus(enum connection_state isConnected){

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);

	BSP_LCD_SetTextColor(LCD_COLOR_WHITE);

	BSP_LCD_FillRect(0, 30, 320, 20);

	BSP_LCD_SetTextColor(LCD_COLOR_BLACK);

	BSP_LCD_DisplayStringAt(30, 35, (uint8_t*) "Status: ", LEFT_MODE);

	if (isConnected == CONNECTED) {
		BSP_LCD_SetTextColor(LCD_COLOR_GREEN);
		BSP_LCD_DisplayStringAt(140, 35, (uint8_t*) " CONNECTED.  ", LEFT_MODE);
	}
	else if (isConnected == DISCONNECTED){
		BSP_LCD_SetTextColor(LCD_COLOR_RED);
		BSP_LCD_DisplayStringAt(140, 35, (uint8_t*) "DISCONNECTED.", LEFT_MODE);
	}
	else if (isConnected == CONNECTING){
		BSP_LCD_SetTextColor(LCD_COLOR_YELLOW);
		BSP_LCD_DisplayStringAt(140, 35, (uint8_t*) "CONNECTING...", LEFT_MODE);
	}

	xSemaphoreGive(mutex_drawer);
}

void setConnectedState(uint8_t isConnected) {

	buttons[BUTTON_E0].isEnabled = isConnected;
	buttons[BUTTON_E1].isEnabled = isConnected;
	buttons[BUTTON_01].isEnabled = isConnected;
	buttons[BUTTON_05].isEnabled = isConnected;
	buttons[BUTTON_C8].isEnabled = isConnected;
	buttons[BUTTON_C9].isEnabled = isConnected;
	buttons[BUTTON_CB].isEnabled = isConnected;
	buttons[BUTTON_CC].isEnabled = isConnected;

	placeButton(buttons[BUTTON_E0], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_E1], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_01], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_05], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_C8], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_C9], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_CB], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_CC], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);

}

void setVersion (uint8_t *version) {

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);

	BSP_LCD_SetTextColor(LCD_COLOR_WHITE);

	BSP_LCD_FillRect(0, 65, BSP_LCD_GetXSize(), 20);

	if (version != NULL) {

		BSP_LCD_SetTextColor(LCD_COLOR_BLACK);

		BSP_LCD_DisplayStringAt(30, 65, (uint8_t*) "Version: ", LEFT_MODE);

		BSP_LCD_SetTextColor(LCD_COLOR_BLUE);

		char buffer[32];
		memset(buffer, 0, 32);

		sprintf(buffer, "%c %d.%02d", (char) version[0], version[1], version[2]);

		BSP_LCD_DisplayStringAt(150, 65, (uint8_t*) buffer, LEFT_MODE);

		BSP_LCD_SetTextColor(LCD_COLOR_BLACK);

		BSP_LCD_DisplayStringAt(240, 65, (uint8_t*) "Data: ", LEFT_MODE);

		memset(buffer, 0, 32);

		sprintf(buffer, "%02d/%02d/%02d", version[3], version[4], version[5]);

		BSP_LCD_SetTextColor(LCD_COLOR_ORANGE);

		BSP_LCD_DisplayStringAt(320, 65, (uint8_t*) buffer, LEFT_MODE);

	}

	xSemaphoreGive(mutex_drawer);
}

void setReceivedData() {

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);

	BSP_LCD_SetTextColor(LCD_COLOR_WHITE);

	BSP_LCD_FillRect(245, 95, BSP_LCD_GetXSize() - 245, 20);

	BSP_LCD_SetTextColor(LCD_COLOR_BLACK);

	BSP_LCD_DisplayStringAt(240, 95, (uint8_t*) "Received packet: ", LEFT_MODE);

	xSemaphoreGive(mutex_drawer);

}

void paintPushedButton (button_t button, uint16_t width, uint16_t heigth) {
	placeButton(button, LCD_COLOR_LIGHTBLUE, LCD_COLOR_BLACK, width, heigth);
}

void unpaintPushedButton (button_t button, uint16_t width, uint16_t heigth) {
	placeButton(button, LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, width, heigth);
}

void paintPushedBoard(pboard_t *board) {

	placeBoard(board, board->board.module.pushed_background_color, LCD_COLOR_WHITE);
}

void unpaintPushedBoard(pboard_t *board) {
	placeBoard(board, board->board.module.background_color, LCD_COLOR_BLACK);
}

void repaintButtons() {

	placeButton(buttons[BUTTON_E0], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_E1], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_01], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_05], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_C8], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_C9], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_CB], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_CC], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
}

void placeBoard(pboard_t *board, uint32_t color_back, uint32_t color_text) {

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	char buffer[32] = "Board #";

	buffer[7] = '0' + board->order + 1;

	BSP_LCD_SetTextColor(color_back);

	if (board->isEnabled) {

		BSP_LCD_FillRect(board->x, board->y, BOARD_WIDTH, BOARD_HEIGTH);
		BSP_LCD_SetTextColor(color_text);
		BSP_LCD_SetBackColor(color_back);
	}
	else {

		BSP_LCD_SetTextColor(LCD_COLOR_WHITE);
		BSP_LCD_FillRect(board->x, board->y, BOARD_WIDTH, BOARD_HEIGTH);

		BSP_LCD_SetTextColor(LCD_COLOR_LIGHTGRAY);
		BSP_LCD_DrawRect(board->x, board->y, BOARD_WIDTH, BOARD_HEIGTH);
		BSP_LCD_SetTextColor(LCD_COLOR_LIGHTGRAY);
		BSP_LCD_SetBackColor(LCD_COLOR_WHITE);
	}

	BSP_LCD_DisplayStringAt(board->x + 10, board->y + 15, (uint8_t*) buffer, LEFT_MODE);

	BSP_LCD_DisplayStringAt(board->x + 10, board->y + 35, (uint8_t*) board->board.module.name, LEFT_MODE);

	xSemaphoreGive(mutex_drawer);
}

void placeButton(button_t button, uint32_t color_back, uint32_t color_text, uint16_t width, uint16_t heigth) {

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_SetTextColor(color_back);

	if (button.isEnabled) {
		BSP_LCD_FillRect(button.x_pos, button.y_pos, width, heigth);
		BSP_LCD_SetTextColor(color_text);
		BSP_LCD_SetBackColor(color_back);

	}
	else {

		BSP_LCD_SetTextColor(LCD_COLOR_WHITE);
		BSP_LCD_FillRect(button.x_pos, button.y_pos, width, heigth);

		BSP_LCD_SetTextColor(LCD_COLOR_LIGHTGRAY);
		BSP_LCD_DrawRect(button.x_pos, button.y_pos, width, heigth);
		BSP_LCD_SetTextColor(LCD_COLOR_LIGHTGRAY);
		BSP_LCD_SetBackColor(LCD_COLOR_WHITE);
	}

	BSP_LCD_DisplayStringAt(button.x_pos + width/8, button.y_pos + heigth/6, (uint8_t*) button.title, LEFT_MODE);

	xSemaphoreGive(mutex_drawer);
}

void refreshBoardState (TS_StateTypeDef ts_event,  pboard_t *board) {

	if (isTouchedBoard(ts_event, board)) {

		if (!board->isPushed)
			paintPushedBoard(board);

		aScreen = BOARD_SELECTED;
		selected_board = board;

		board->draw_initial_screen((void*) board);
		board->refresh_function((void*) board);

		board->isPushed = 1;
	}
	else {

		if (board->isPushed)
			unpaintPushedBoard(board);

		board->isPushed = 0;
	}

}

void refreshButtonState (TS_StateTypeDef ts_event,  button_t *button, uint16_t width, uint16_t heigth) {

	if (isTouched(ts_event, button, width, heigth)) {

		if (!button->isPushed)
			paintPushedButton(*button, width, heigth);

		if (!button->hasBeenAcknowledged)
			button->buttonEventHandler((void*) button);

		button->isPushed = 1;
	}
	else {

		if (button->isPushed)
			unpaintPushedButton(*button, width, heigth);

		button->hasBeenAcknowledged = 0;
		button->isPushed = 0;
	}

}

void refreshConnectState(TS_StateTypeDef ts_event) {

	uint32_t color_back_pushed, color_back_normal;

	if (!isConnected){
		memcpy(buttons[BUTTON_CONNECT].title, "Conn.\0", 6);
		color_back_pushed = LCD_COLOR_DARKGREEN;
		color_back_normal = LCD_COLOR_LIGHTGREEN;
	}
	else {
		memcpy(buttons[BUTTON_CONNECT].title, "Disc.\0", 6);
		color_back_pushed = LCD_COLOR_DARKRED;
		color_back_normal = LCD_COLOR_LIGHTRED;
	}

	if (isTouched(ts_event, &buttons[BUTTON_CONNECT], BUTTON_WIDTH, BUTTON_HEIGTH)) {

		if (!buttons[BUTTON_CONNECT].hasBeenAcknowledged)
			buttons[BUTTON_CONNECT].buttonEventHandler((void*) &buttons[BUTTON_CONNECT]);

		if (!buttons[BUTTON_CONNECT].isPushed)
			placeButton(buttons[BUTTON_CONNECT], color_back_pushed, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
		buttons[BUTTON_CONNECT].isPushed = 1;
	}
	else {

		if (buttons[BUTTON_CONNECT].isPushed)
			placeButton(buttons[BUTTON_CONNECT], color_back_normal, LCD_COLOR_BLACK, BUTTON_WIDTH, BUTTON_HEIGTH);

		buttons[BUTTON_CONNECT].isPushed = 0;
		buttons[BUTTON_CONNECT].hasBeenAcknowledged = 0;
	}
}

void refreshBoards() {

	for (int i = 0; i < 4; i++)
		placeBoard(&pboards[i], pboards[i].board.module.background_color, LCD_COLOR_BLACK);
}

void buttonConnectPressed(void* source){

	struct sockaddr_in addr;
	int ret;

	((button_t*)source)->hasBeenAcknowledged = 1;

	setStatus(CONNECTING);

	if (!isConnected) {

		memset(&addr, 0, sizeof(addr));
		addr.sin_len = sizeof(addr);
		addr.sin_family = AF_INET;
		addr.sin_port = PP_HTONS(SOCK_TARGET_PORT);
		addr.sin_addr.s_addr = inet_addr(SOCK_TARGET_HOST);

		socket_in = lwip_socket(AF_INET, SOCK_STREAM, 0);

		if ((ret = lwip_connect(socket_in, (struct sockaddr*)&addr, sizeof(addr))) == 0) {
			isConnected = 1;

			executeCommand(IDENT);
			executeCommand(END_IDENT);

			setStatus(CONNECTED);

		}
		else {
			isConnected = 0;
			setStatus(DISCONNECTED);
		}
	}
	else {

		if ((ret = lwip_close(socket_in)) == 0) {
			setStatus(DISCONNECTED);
			isConnected = 0;
			resetBoards();
			setVersion(NULL);
		}
	}

	refreshBoards();
}

uint8_t isTouchedBoard(TS_StateTypeDef ts_event,  pboard_t *board) {

	if (!ts_event.touchDetected || !board->isEnabled)
		return 0;

	uint16_t x = ts_event.touchX[0], y = ts_event.touchY[0];

	if (x >= board->x && x <= board->x + BOARD_WIDTH &&
			y >= board->y && y <= board->y + BOARD_HEIGTH)
		return 1;

	return 0;

}

uint8_t isTouched(TS_StateTypeDef ts_event,  button_t *button, uint16_t width, uint16_t heigth) {

	if (!button->isEnabled || !ts_event.touchDetected)
		return 0;

	uint16_t x = ts_event.touchX[0], y = ts_event.touchY[0];

	if (x >= button->x_pos && x <= button->x_pos + width &&
			y >= button->y_pos && y <= button->y_pos + heigth)
		return 1;

	return 0;

}

void printNewCycleID() {

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);

	if (cycleSelected == 128){

		BSP_LCD_SetTextColor(LCD_COLOR_RED);
		BSP_LCD_DisplayChar(380, 240, 'N');
	}

	else {
		BSP_LCD_SetTextColor(LCD_COLOR_YELLOW);
		BSP_LCD_DisplayChar(380, 240, '0' + cycleSelected);
	}

	xSemaphoreGive(mutex_drawer);
}

void printNewRampID() {

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);

	if (rampSelected == 128) {
		BSP_LCD_SetTextColor(LCD_COLOR_RED);
		BSP_LCD_DisplayChar(380, 210, 'N');
	}

	else {
		BSP_LCD_SetTextColor(LCD_COLOR_BLUE);
		BSP_LCD_DisplayChar(380, 210, '0' + rampSelected);
	}

	xSemaphoreGive(mutex_drawer);
}

void printNewAnalogInput() {

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_SetTextColor(LCD_COLOR_WHITE);
	BSP_LCD_FillRect(320,150, 80, 20);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);
	BSP_LCD_SetTextColor(LCD_COLOR_BLACK);

	char buffer[5];
	memset(buffer, 0, 5);

	sprintf(buffer, "%d", analogIn);

	BSP_LCD_DisplayStringAt(320, 150, (uint8_t*) buffer, LEFT_MODE);

	xSemaphoreGive(mutex_drawer);

}

void printNewDigitalInput() {

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_SetTextColor(LCD_COLOR_WHITE);
	BSP_LCD_FillRect(320,180, 80, 20);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);
	BSP_LCD_SetTextColor(LCD_COLOR_BLACK);

	char buffer[5];
	memset(buffer, 0, 5);

	sprintf(buffer, "%d", digitalIn);

	BSP_LCD_DisplayStringAt(320, 180, (uint8_t*) buffer, LEFT_MODE);

	xSemaphoreGive(mutex_drawer);

}

void processCommand(enum command command, uint8_t *buffer, uint16_t length) {

	int j;

	switch(command) {

	case IDENT:

		for(int i = 2; i < 32 + 2; ++i)	{

			module_t m = findModule(buffer[i]);

			if(m.type != NONE.type)
				addBoard (m, i - 2);
		}

		if(buffer[1] > 32)
			for (int i = 0; i < 6; i++)
				version[i] = buffer[34 + i];

		setVersion(version);
		break;

	default:

		j = 2;

		for (int i = 0; i < pboards_c; i++){

			if (j + pboards[i].board.module.readBytesCount + 2 <= length)
				for (int w = 0; w < pboards[i].board.module.readBytesCount + 2; w++) {
					pboards[i].board.readBytes[w] = buffer[j];
					j = j + 1;
				}
		}

		break;
	}
}

void prepareCommand(enum command command, uint8_t** buffer, uint16_t *length) {

	int j, length_without_headers;

	switch (command) {

	case RAMP_BLOCK:

		*length  = 2* 1021 + 7;

		*buffer = (uint8_t*) pvPortMalloc(*length * sizeof(uint8_t));

		**buffer = command;
		*(*buffer + 1) = ((*length  - 3) >> 8) & 0xFF;
		*(*buffer + 2) = (*length - 3) & 0xFF;

		*(*buffer + 3)  = selected_board->order + 2;

		*(*buffer + 4)  = 1;
		*(*buffer + 5) = (1021 >> 8) & 0xFF;
		*(*buffer + 6) = 1021 & 0xFF;

		// Data
		j = 0;
		for(int k = 7; k < *length ; k+=2) {

			if (j < 1021) {
				uint16_t value = ramps[rampSelected][j++];
				*(*buffer + k) = (value >> 8) & 0xFF;
				*(*buffer + k + 1) = value & 0xFF;
			}
			else  {
				*(*buffer + k) = 0;
				*(*buffer + k + 1) = 0;
			}
		}

		break;

	case RAMP_ENABLE:
	case RAMP_ENABLE_CYCLIC:
	case RAMP_INIT:
	case CYCLE_ENABLE:
	case ADJUST:

		length_without_headers = 0;
		j = 2;

		for (int i = 0; i < pboards_c; i++)
			length_without_headers += 2 + pboards[i].board.module.writeBytesCount;

		*buffer = (uint8_t*) pvPortMalloc((length_without_headers + 2) * sizeof(uint8_t));

		for (int i = 0; i < pboards_c; i++)
			for (int w = 0; w < 2 + pboards[i].board.module.writeBytesCount; w++) {
				*(*buffer + j) = pboards[i].board.writeBytes[w];
				j++;
			}

		*(*buffer + 1)  = length_without_headers;
		**buffer = (uint8_t) command;

		*length = length_without_headers + 2;
		break;

	case IDENT:
	case END_IDENT:
	default:
		*buffer = (uint8_t*) pvPortMalloc(sizeof(uint8_t));
		**buffer = command;
		*length = 1;
	}

}

void executeCommand(enum command command) {

	if (isConnected) {

		uint8_t buffer_in[256], *buffer_out;
		uint16_t length;

		xSemaphoreTake(mutex_handler, portMAX_DELAY);

		prepareCommand(command, &buffer_out, &length);
		sendCommand(buffer_out,length);

		vPortFree(buffer_out);

		receiveCommand(buffer_in, &length);
		processCommand(command, buffer_in, length);

		xSemaphoreGive(mutex_handler);
	}

}

void receiveCommand(uint8_t *buffer, uint16_t *length) {
	*length = read(socket_in, buffer, 256);
}

void sendCommand(uint8_t* buffer, uint16_t length ) {
	write(socket_in, buffer, length);
}

void resetBoards() {

	pboards_c = 0;

	for (int i = 0; i < NUMBER_OF_BOARDS; i++){
		pboards[i].board.module = NONE;

		pboards[i].isEnabled = 0;
		pboards[i].isPushed = 0;
	}
}

void addBoard(module_t module, int id) {

	pboards[pboards_c].board.module = module;

	pboards[pboards_c].board.readBytes = (uint8_t*) pvPortMalloc((module.readBytesCount + 2)* sizeof(uint8_t));
	pboards[pboards_c].board.writeBytes = (uint8_t*) pvPortMalloc ((module.writeBytesCount + 2) * sizeof(uint8_t));

	pboards[pboards_c].board.writeBytes [0] = 0;
	pboards[pboards_c].board.writeBytes [1] = 128;

	pboards[pboards_c].board.id = id;
	pboards[pboards_c].order = pboards_c;

	pboards[pboards_c].isEnabled = 1;
	pboards[pboards_c].isPushed = 0;

	switch (module.type) {

		case 1:
		case 2:
		case 3:
		case 4:
			pboards[pboards_c].draw_initial_screen = drawLOCON;
			pboards[pboards_c].refresh_function = refreshLOCON;
	}

	pboards_c++;
}

void eventHandlerE0 (void* button) {

	if (cycleSelected != 128) {

		((button_t*) button)->hasBeenAcknowledged = 1;

		buttons[BUTTON_E0].isEnabled = 0;
		buttons[BUTTON_E1].isEnabled = 1;
		buttons[BUTTON_01].isEnabled = 0;
		buttons[BUTTON_05].isEnabled = 0;
		buttons[BUTTON_C8].isEnabled = 0;
		buttons[BUTTON_C9].isEnabled = 0;
		buttons[BUTTON_CB].isEnabled = 0;
		buttons[BUTTON_CC].isEnabled = 0;

		repaintButtons();

		executeCommand(CYCLE_ENABLE);
	}

}

void eventHandlerE1 (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	buttons[BUTTON_E0].isEnabled = 1;
	buttons[BUTTON_E1].isEnabled = 0;
	buttons[BUTTON_01].isEnabled = 1;
	buttons[BUTTON_05].isEnabled = 1;
	buttons[BUTTON_C8].isEnabled = 1;
	buttons[BUTTON_C9].isEnabled = 0;
	buttons[BUTTON_CB].isEnabled = 0;
	buttons[BUTTON_CC].isEnabled = 0;

	repaintButtons();

	executeCommand(CYCLE_ABORT);

}

void eventHandlerConfirm (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	executeCommand(MSG_CONFIRM);
}

void eventHandlerAdjust (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	executeCommand(ADJUST);
}

void eventHandlerC8 (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	if (rampSelected != 128) {

		buttons[BUTTON_E0].isEnabled = 0;
		buttons[BUTTON_E1].isEnabled = 0;
		buttons[BUTTON_01].isEnabled = 0;
		buttons[BUTTON_05].isEnabled = 0;
		buttons[BUTTON_C8].isEnabled = 0;
		buttons[BUTTON_C9].isEnabled = 1;
		buttons[BUTTON_CB].isEnabled = 0;
		buttons[BUTTON_CC].isEnabled = 0;

		repaintButtons();

		executeCommand(RAMP_INIT);

	}

}

void eventHandlerC9 (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	executeCommand(RAMP_BLOCK);

	buttons[BUTTON_E0].isEnabled = 0;
	buttons[BUTTON_E1].isEnabled = 0;
	buttons[BUTTON_01].isEnabled = 0;
	buttons[BUTTON_05].isEnabled = 0;
	buttons[BUTTON_C8].isEnabled = 0;
	buttons[BUTTON_C9].isEnabled = 0;
	buttons[BUTTON_CB].isEnabled = 1;
	buttons[BUTTON_CC].isEnabled = 0;

	repaintButtons();

}

void eventHandlerCB (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	executeCommand(RAMP_ENABLE);

	buttons[BUTTON_E0].isEnabled = 0;
	buttons[BUTTON_E1].isEnabled = 0;
	buttons[BUTTON_01].isEnabled = 0;
	buttons[BUTTON_05].isEnabled = 0;
	buttons[BUTTON_C8].isEnabled = 0;
	buttons[BUTTON_C9].isEnabled = 0;
	buttons[BUTTON_CB].isEnabled = 0;
	buttons[BUTTON_CC].isEnabled = 1;

	repaintButtons();

}

void eventHandlerCC (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	executeCommand(RAMP_ABORT);

	buttons[BUTTON_E0].isEnabled = 1;
	buttons[BUTTON_E1].isEnabled = 0;
	buttons[BUTTON_01].isEnabled = 1;
	buttons[BUTTON_05].isEnabled = 1;
	buttons[BUTTON_C8].isEnabled = 1;
	buttons[BUTTON_C9].isEnabled = 0;
	buttons[BUTTON_CB].isEnabled = 0;
	buttons[BUTTON_CC].isEnabled = 0;

	repaintButtons();

}

void increaseRampID (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	if (rampSelected == 128)
		rampSelected = 0;

	else if (rampSelected < 2 )
		rampSelected++;

	else {
		rampSelected = 2;
		((button_t*) button)->isEnabled = 0;
	}

	if (!buttons_ramp_cycle[0].isEnabled) {
		buttons_ramp_cycle[0].isEnabled = 1;
		placeButton(buttons_ramp_cycle[0], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	}

	printNewRampID();

}

void increaseCycleID (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	if (cycleSelected == 128) {
		cycleSelected = 0;
	}

	else if (cycleSelected < 4 ) {
		cycleSelected++;
	}

	else {
		cycleSelected = 4;
		((button_t*) button)->isEnabled = 0;
	}

	if (!buttons_ramp_cycle[2].isEnabled) {
		buttons_ramp_cycle[2].isEnabled = 1;
		placeButton(buttons_ramp_cycle[2], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	}

	printNewCycleID();

}

void decreaseCycleID (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;


	if (cycleSelected >= 1)
		cycleSelected--;

	else {
		cycleSelected = 128;
		((button_t*) button)->isEnabled = 0;
	}

	if (!buttons_ramp_cycle[3].isEnabled ) {
		buttons_ramp_cycle[3].isEnabled = 1;
		placeButton(buttons_ramp_cycle[3], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	}

	printNewCycleID();
}

void decreaseRampID (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	if (rampSelected >= 1)
		rampSelected--;

	else {
		rampSelected = 128;
		((button_t*) button)->isEnabled = 0;

	}

	if (!buttons_ramp_cycle[1].isEnabled ) {
		buttons_ramp_cycle[1].isEnabled = 1;
		placeButton(buttons_ramp_cycle[1], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	}

	printNewRampID();
}

void increaseAnalogInput (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	uint16_t max = 4095;

	if (selected_board->board.module.type == 3 ||
			selected_board->board.module.type == 4 )
		max = 65535;

	if (analogIn < max) {
		analogIn += 256;
	}

	else {
		analogIn = max;
		((button_t*) button)->isEnabled = 0;
	}

	if (!buttons_ramp_cycle[4].isEnabled) {
		buttons_ramp_cycle[4].isEnabled = 1;
		placeButton(buttons_ramp_cycle[4], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	}

	printNewAnalogInput();
}

void decreaseAnalogInput (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;


	if (analogIn > 256) {
		analogIn -= 256;
	}

	else {
		analogIn = 0;
		((button_t*) button)->isEnabled = 0;
	}

	if (!buttons_ramp_cycle[5].isEnabled) {
		buttons_ramp_cycle[5].isEnabled = 1;
		placeButton(buttons_ramp_cycle[5], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	}

	printNewAnalogInput();
}

void decreaseDigitalInput (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	if (digitalIn > 8) {
		digitalIn -= 8;
	}

	else {
		digitalIn = 0;
		((button_t*) button)->isEnabled = 0;
	}

	if (!buttons_ramp_cycle[7].isEnabled) {
		buttons_ramp_cycle[7].isEnabled = 1;
		placeButton(buttons_ramp_cycle[7], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	}

	printNewDigitalInput();
}

void increaseDigitalInput (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	uint16_t max = 128;

	if (digitalIn < max - 8) {
		digitalIn += 8;
	}

	else {
		digitalIn = max;
		((button_t*) button)->isEnabled = 0;
	}

	if (!buttons_ramp_cycle[6].isEnabled) {
		buttons_ramp_cycle[6].isEnabled = 1;
		placeButton(buttons_ramp_cycle[6], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	}

	printNewDigitalInput();
}





