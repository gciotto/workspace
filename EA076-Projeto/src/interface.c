/*
 * interface.c

 *
 *  Created on: May 5, 2016
 *      Author: gciotto
 */
#include "interface.h"

const module_t NONE 	= { .type = 0x3F, .name = "NONE", .readBytesCount = 0, .writeBytesCount = 0, .background_color = LCD_COLOR_LIGHTBLUE, .pushed_background_color = LCD_COLOR_DARKBLUE};
const module_t EEPROM	= { .type = 0x01, .name = "Read EEPROM", .readBytesCount = 3, .writeBytesCount = 3, .background_color = LCD_COLOR_LIGHTGREEN, .pushed_background_color = LCD_COLOR_DARKGREEN};
const module_t TEMP		= { .type = 0x02, .name = "Temperature", .readBytesCount = 3, .writeBytesCount = 3, .background_color = LCD_COLOR_LIGHTRED, .pushed_background_color = LCD_COLOR_DARKRED };
const module_t ADC_O	= { .type = 0x03, .name = "Use ADC", .readBytesCount = 3, .writeBytesCount = 3, .background_color = LCD_COLOR_LIGHTCYAN, .pushed_background_color = LCD_COLOR_DARKCYAN };
const module_t DAC_O		= { .type = 0x04, .name = "Use DAC", .readBytesCount = 3, .writeBytesCount = 3, .background_color = LCD_COLOR_LIGHTMAGENTA, .pushed_background_color = LCD_COLOR_DARKMAGENTA };

module_t findModule(uint8_t code) {

	if (code == EEPROM.type) return EEPROM;
	else if (code == TEMP.type) return TEMP;
	else if (code == ADC_O.type) return ADC_O;
	else if (code == DAC_O.type) return DAC_O;

	return NONE;
}

void processCommand(enum command command, uint8_t *buffer, uint16_t length) {

	int j;

	switch(command) {

	case IDENT:

		for(int i = 2; i < 32 + 2; ++i)
		{
			module_t m = findModule(buffer[i]);

			if(m.type != NONE.type)
			{
				pboards[pboards_c].board.module = m;

				pboards[pboards_c].board.readBytes = (uint8_t*) pvPortMalloc((m.readBytesCount + 2)* sizeof(uint8_t));
				pboards[pboards_c].board.writeBytes = (uint8_t*) pvPortMalloc ((m.writeBytesCount + 2) * sizeof(uint8_t));

				pboards[pboards_c].board.writeBytes [0] = 0;
				pboards[pboards_c].board.writeBytes [1] = 128;

				pboards[pboards_c].board.id = i - 2;
				pboards[pboards_c].order = pboards_c;

				pboards[pboards_c].isEnabled = 1;
				pboards[pboards_c].isPressed = 0;

				switch (m.type) {

				case 1:
				case 2:
				case 3:
				case 4:
					//pboards[pboards_c].draw_initial_screen = draw_LOCON;
					//pboards[pboards_c].refresh_function = refresh_LOCON;
					break;
				}

				pboards_c++;

			}
		}

		if(buffer[1] > 32) {

			for (int i = 0; i < 6; i++)
				version[i] = buffer[34 + i];

		}

		break;

	default:

		j = 2;

		for (int i = 0; i < pboards_c; i++){

			if (j + pboards[i].board.module.readBytesCount + 2 <= length)
				for (int w = 0; w < pboards[i].board.module.readBytesCount + 2; w++) {
					pboards[i].board.readBytes[w] = buffer[j];
					int t = buffer[j];
					j = j + 1;
				}
		}

		break;
	}
}

void receiveCommand(uint8_t *buffer, uint16_t *length) {


}

void prepareCommand(enum command command, uint8_t** buffer, uint16_t *length) {

	int j, length_without_headers, packet_length;

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
				//uint16_t value = ramps[rampSelected][j++];
				//*(*buffer + k) = (value >> 8) & 0xFF;
				//*(*buffer + k + 1) = value & 0xFF;
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

void sendCommand(uint8_t* buffer, uint16_t length ) {

	setStatus(CONNECTED);
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

	if (isTouched(ts_event, &buttons[BUTTON_CONNECT], BUTTON_CONNECT_WIDTH, BUTTON_HEIGTH)) {

		if (!buttons[BUTTON_CONNECT].hasBeenAcknowledged)
			buttons[BUTTON_CONNECT].buttonPressedHandler((void*) &buttons[BUTTON_CONNECT]);

		if (!buttons[BUTTON_CONNECT].isPushed)
			placeButton(buttons[BUTTON_CONNECT], color_back_pushed, LCD_COLOR_WHITE, BUTTON_CONNECT_WIDTH, BUTTON_HEIGTH);
		buttons[BUTTON_CONNECT].isPushed = 1;
	}
	else {

		if (buttons[BUTTON_CONNECT].isPushed)
			placeButton(buttons[BUTTON_CONNECT], color_back_normal, LCD_COLOR_BLACK, BUTTON_CONNECT_WIDTH, BUTTON_HEIGTH);

		buttons[BUTTON_CONNECT].isPushed = 0;
		buttons[BUTTON_CONNECT].hasBeenAcknowledged = 0;
	}
}


void refreshBoards() {

	for (int i = 0; i < 4; i++) {

		if (isConnected) pboards[i].isEnabled = 1;
		else pboards[i].isEnabled = 0;

		placeBoard(&pboards[i], pboards[i].board.module.background_color, LCD_COLOR_BLACK);
	}
}

void resetBoards() {

	pboards_c = 0;

	for (int i = 0; i < NUMBER_OF_BOARDS; i++){
		pboards[i].board.module = NONE;

		pboards[i].isEnabled = 0;
		pboards[i].isPressed = 0;
	}
}

void buttonConnectPressed(void* source){

	((button_t*)source)->hasBeenAcknowledged = 1;

	setStatus(CONNECTING);

	if (!isConnected) {

		/* TO DO: Se conexao bem-sucedida. */
		if (1) {
			isConnected = 1;

			/* TO DO - I2C */

			setStatus(CONNECTED);

		}
		else {
			isConnected = 0;
			setStatus(DISCONNECTED);
		}


	}
	else {

		/* TO DO: Se desconexao bem sucedida. */
		if (1) {
			setStatus(DISCONNECTED);
			isConnected = 0;

			//resetBoards();
		}
	}

	refreshBoards();
}

void refreshButtonState (TS_StateTypeDef ts_event,  button_t *button, uint16_t width, uint16_t heigth) {

	if (isTouched(ts_event, button, width, heigth)) {

		if (!button->isPushed) {
			paintPushedButton(*button, width, heigth);
			if (!strcmp(button->title, "<Home"))
				osDelay(150);
		}

		if (!button->hasBeenAcknowledged) {
			button->hasBeenAcknowledged = 1;
			button->buttonPressedHandler((void*) button);
		}

		button->isPushed = 1;
	}
	else {

		if (button->isPushed)
			unpaintPushedButton(*button, width, heigth);

		button->hasBeenAcknowledged = 0;
		button->isPushed = 0;
	}

}

void paintPushedBoard(pboard_t *board) {

	placeBoard(board, board->board.module.pushed_background_color, LCD_COLOR_WHITE);
}

void unpaintPushedBoard(pboard_t *board) {
	placeBoard(board, board->board.module.background_color, LCD_COLOR_BLACK);
}

void addEEPROMValueAt(uint16_t address, union EEPROM_data value) {

	uint16_t base_address_x = 212, base_address_y = 45, y_n;

	if (address <= 0x1c)
		y_n = base_address_y + (address / 4)*20;

	else {

		base_address_x = 347;
		y_n = base_address_y + ((address - 0x20) / 4)*20;

	}

	char buffer[32];
	memset(buffer, 0, 32);

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	sprintf(buffer, "0x%02x", address);

	BSP_LCD_SetTextColor(LCD_COLOR_LIGHTGREEN);
	BSP_LCD_DisplayStringAt(base_address_x, y_n, (uint8_t*) buffer, LEFT_MODE);

	memset(buffer, 0, 32);
	sprintf(buffer, "%02.2f", value.data_as_float);

	BSP_LCD_SetTextColor(LCD_COLOR_BLACK);
	BSP_LCD_DisplayStringAt(base_address_x + 60, y_n, (uint8_t*) buffer, LEFT_MODE);

	xSemaphoreGive(mutex_drawer);

}

void setEEPROM_max(union EEPROM_data max) {

	char buffer[32];
	memset(buffer, 0, 32);

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	sprintf(buffer, "%02.2f", max.data_as_float);

	BSP_LCD_SetTextColor(LCD_COLOR_DARKGREEN);
	BSP_LCD_DisplayStringAt(265, 220, (uint8_t*) buffer, LEFT_MODE);

	xSemaphoreGive(mutex_drawer);
}

void setEEPROM_min(union EEPROM_data max) {

	char buffer[32];
	memset(buffer, 0, 32);

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	sprintf(buffer, "%02.2f", max.data_as_float);

	BSP_LCD_SetTextColor(LCD_COLOR_DARKBLUE);
	BSP_LCD_DisplayStringAt(265, 245, (uint8_t*) buffer, LEFT_MODE);

	xSemaphoreGive(mutex_drawer);
}

void setEEPROM_mean(union EEPROM_data max) {

	char buffer[32];
	memset(buffer, 0, 32);

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	sprintf(buffer, "%02.2f", max.data_as_float);

	BSP_LCD_SetTextColor(LCD_COLOR_DARKGRAY);
	BSP_LCD_DisplayStringAt(405, 220, (uint8_t*) buffer, LEFT_MODE);

	xSemaphoreGive(mutex_drawer);
}

void setEEPROM_status(enum EEPROM_status status) {

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);

	if (status == STOPPED) {

		BSP_LCD_SetTextColor(LCD_COLOR_RED);
		BSP_LCD_DisplayStringAt(330, 10, (uint8_t*) "STOPPED", LEFT_MODE);

	}
	else {

		BSP_LCD_SetTextColor(LCD_COLOR_GREEN);
		BSP_LCD_DisplayStringAt(330, 10, (uint8_t*) "STARTED", LEFT_MODE);
	}

	xSemaphoreGive(mutex_drawer);

}



void setDAC_Tension() {

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_SetTextColor(LCD_COLOR_WHITE);
	BSP_LCD_FillRect(330, 170, 50, 20);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);
	BSP_LCD_SetTextColor(LCD_COLOR_BLACK);

	char buffer[5];
	memset(buffer, 0, 5);

	if (dac_set_new_value > 0)
		sprintf(buffer, "%1.1f", dac_set_new_value);
	else sprintf(buffer, "0.0");

	BSP_LCD_DisplayStringAt(330, 170, buffer, LEFT_MODE);

	xSemaphoreGive(mutex_drawer);

}

void setDAC_Period() {

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_SetTextColor(LCD_COLOR_WHITE);
	BSP_LCD_FillRect(330, 112, 50, 20);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);
	BSP_LCD_SetTextColor(LCD_COLOR_BLACK);

	char buffer[5];
	memset(buffer, 0, 5);

	sprintf(buffer, "%d", dac_period);

	BSP_LCD_DisplayStringAt(330, 112, buffer, LEFT_MODE);

	xSemaphoreGive(mutex_drawer);

}

void setDAC_Status(enum button_index source) {

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_SetTextColor(LCD_COLOR_WHITE);
	BSP_LCD_FillRect(350, 50, 130, 20);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);

	if (source == DAC_SIN) {
		BSP_LCD_SetTextColor(LCD_COLOR_GREEN);
		BSP_LCD_DisplayStringAt(350, 50, "SIN. WAVE", LEFT_MODE);
	}
	else if (source == DAC_QUAD) {
		BSP_LCD_SetTextColor(LCD_COLOR_RED);
		BSP_LCD_DisplayStringAt(350, 50, "QUA. WAVE", LEFT_MODE);
	}
	else if (source == DAC_TRI){
		BSP_LCD_SetTextColor(LCD_COLOR_BLUE);
		BSP_LCD_DisplayStringAt(350, 50, "TRI. WAVE", LEFT_MODE);
	}
	else {
		BSP_LCD_SetTextColor(LCD_COLOR_RED);
		BSP_LCD_DisplayStringAt(350, 50, "NO WAVE", LEFT_MODE);
	}

	xSemaphoreGive(mutex_drawer);

}


void draw_EEPROM(void* board) {

	pboard_t *aBoard = (pboard_t*) board;

	aScreen = EEPROM_SELECTED;

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_Clear(LCD_COLOR_WHITE);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);

	BSP_LCD_SetFont(&Font24);

	BSP_LCD_SetTextColor(LCD_COLOR_BLUE);

	BSP_LCD_SetTextColor(aBoard->board.module.background_color);
	BSP_LCD_DisplayStringAt(10, 50, aBoard->board.module.name, LEFT_MODE);

	BSP_LCD_SetFont(&Font20);

	BSP_LCD_SetTextColor(LCD_COLOR_LIGHTRED);
	BSP_LCD_DisplayStringAt(210, 10, (uint8_t*) "STATUS :", LEFT_MODE);

	/* STATUS X = 370, Y = 10 */
	BSP_LCD_SetTextColor(LCD_COLOR_RED);
	BSP_LCD_DisplayStringAt(330, 10, (uint8_t*) "STOPPED", LEFT_MODE);

	/* VALUES BOX */

	BSP_LCD_SetTextColor(LCD_COLOR_LIGHTGRAY);
	BSP_LCD_DrawRect(210, 40, 268, 170);

	/* MAX, MIN and MEAN */

	BSP_LCD_SetTextColor(LCD_COLOR_LIGHTGREEN);
	BSP_LCD_DisplayStringAt(210, 220, (uint8_t*) "MAX.", LEFT_MODE);

	BSP_LCD_SetTextColor(LCD_COLOR_LIGHTGRAY);
	BSP_LCD_DisplayStringAt(345, 220, (uint8_t*) "MEAN", LEFT_MODE);

	BSP_LCD_SetTextColor(LCD_COLOR_LIGHTBLUE);
	BSP_LCD_DisplayStringAt(210, 245, (uint8_t*) "MIN.", LEFT_MODE);

	buttons[EEPROM_START].isEnabled = 1;
	buttons[EEPROM_STOP].isEnabled = 0;
	buttons[EEPROM_STATS].isEnabled = 0;
	buttons[EEPROM_STATE].isEnabled = 0;

	buttons[TEMP_READ].isEnabled = 0;
	buttons[TEMP_SAVE].isEnabled = 0;

	buttons[DAC_SIN].isEnabled = 0;
	buttons[DAC_TRI].isEnabled = 0;
	buttons[DAC_QUAD].isEnabled = 0;
	buttons[DAC_SET].isEnabled = 0;

	xSemaphoreGive(mutex_drawer);

	union EEPROM_data teste;
	teste.data_as_float = 22.5;
	addEEPROMValueAt(0, teste);
	addEEPROMValueAt(4, teste);
	addEEPROMValueAt(0x1c, teste);
	addEEPROMValueAt(0x24, teste);

	teste.data_as_float = 22.5;
	setEEPROM_max(teste);
	setEEPROM_mean(teste);
	setEEPROM_min(teste);

	placeButton(buttons[EEPROM_START], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[EEPROM_STOP], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[EEPROM_STATS], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[EEPROM_STATE], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);

	placeButton(button_home, LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_CONNECT_WIDTH, BUTTON_HEIGTH);

}

void draw_DAC(void* board) {

	pboard_t *aBoard = (pboard_t*) board;
	dac_period = 250;
	dac_set_new_value = 1.1;

	aScreen = DAC_SELECTED;

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_Clear(LCD_COLOR_WHITE);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);

	BSP_LCD_SetFont(&Font24);

	BSP_LCD_SetTextColor(LCD_COLOR_BLUE);

	BSP_LCD_SetTextColor(aBoard->board.module.background_color);
	BSP_LCD_DisplayStringAt(25, 50, aBoard->board.module.name, LEFT_MODE);

	BSP_LCD_SetFont(&Font20);

	BSP_LCD_SetTextColor(LCD_COLOR_LIGHTRED);
	BSP_LCD_DisplayStringAt(230, 50, (uint8_t*) "STATUS :", LEFT_MODE);

	BSP_LCD_SetTextColor(LCD_COLOR_RED);
	BSP_LCD_DisplayStringAt(350, 50, (uint8_t*) "NO WAVE", LEFT_MODE);

	BSP_LCD_SetTextColor(LCD_COLOR_LIGHTGREEN);
	BSP_LCD_DisplayStringAt(230, 80, (uint8_t*) "PERIOD (ms) :", LEFT_MODE);

	BSP_LCD_SetTextColor(LCD_COLOR_BLACK);
	BSP_LCD_DisplayStringAt(330, 112, (uint8_t*) "250", LEFT_MODE);

	BSP_LCD_SetTextColor(LCD_COLOR_LIGHTYELLOW);
	BSP_LCD_DisplayStringAt(230, 140, (uint8_t*) "NEW VALUE (V) :", LEFT_MODE);

	BSP_LCD_SetTextColor(LCD_COLOR_BLACK);
	BSP_LCD_DisplayStringAt(330, 170, (uint8_t*) "1.1", LEFT_MODE);


	buttons[DAC_SIN].isEnabled = 1;
	buttons[DAC_QUAD].isEnabled = 1;
	buttons[DAC_TRI].isEnabled = 1;
	buttons[DAC_SET].isEnabled = 1;
	buttons[DAC_SET].isEnabled = 1;
	buttons[DAC_PERIOD_DECREASE].isEnabled = 1;
	buttons[DAC_PERIOD_INCREASE].isEnabled = 1;
	buttons[DAC_TENSION_DECREASE].isEnabled = 1;
	buttons[DAC_TENSION_INCREASE].isEnabled = 1;

	xSemaphoreGive(mutex_drawer);

	placeButton(buttons[DAC_SIN], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[DAC_QUAD], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[DAC_TRI], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[DAC_SET], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[DAC_PERIOD_DECREASE], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	placeButton(buttons[DAC_PERIOD_INCREASE], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	placeButton(buttons[DAC_TENSION_DECREASE], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	placeButton(buttons[DAC_TENSION_INCREASE], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);

	placeButton(button_home, LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_CONNECT_WIDTH, BUTTON_HEIGTH);

}

void draw_ADC (void* board) {

	pboard_t *aBoard = (pboard_t*) board;
	dac_period = 250;
	dac_set_new_value = 1.1;

	aScreen = ADC_SELECTED;

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_Clear(LCD_COLOR_WHITE);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);

	BSP_LCD_SetFont(&Font24);

	BSP_LCD_SetTextColor(LCD_COLOR_BLUE);

	BSP_LCD_SetTextColor(aBoard->board.module.background_color);
	BSP_LCD_DisplayStringAt(25, 50, aBoard->board.module.name, LEFT_MODE);

	BSP_LCD_SetFont(&Font20);

	BSP_LCD_SetTextColor(LCD_COLOR_LIGHTRED);
	BSP_LCD_DisplayStringAt(230, 50, (uint8_t*) "STATUS :", LEFT_MODE);

	BSP_LCD_SetTextColor(LCD_COLOR_RED);
	BSP_LCD_DisplayStringAt(350, 50, (uint8_t*) "NO WAVE", LEFT_MODE);

	BSP_LCD_SetTextColor(LCD_COLOR_LIGHTGREEN);
	BSP_LCD_DisplayStringAt(230, 80, (uint8_t*) "PERIOD (ms) :", LEFT_MODE);

	BSP_LCD_SetTextColor(LCD_COLOR_BLACK);
	BSP_LCD_DisplayStringAt(330, 112, (uint8_t*) "250", LEFT_MODE);

	BSP_LCD_SetTextColor(LCD_COLOR_LIGHTYELLOW);
	BSP_LCD_DisplayStringAt(230, 140, (uint8_t*) "NEW VALUE (V) :", LEFT_MODE);

	BSP_LCD_SetTextColor(LCD_COLOR_BLACK);
	BSP_LCD_DisplayStringAt(330, 170, (uint8_t*) "1.1", LEFT_MODE);


	buttons[DAC_SIN].isEnabled = 1;
	buttons[DAC_QUAD].isEnabled = 1;
	buttons[DAC_TRI].isEnabled = 1;
	buttons[DAC_SET].isEnabled = 1;
	buttons[DAC_SET].isEnabled = 1;
	buttons[DAC_PERIOD_DECREASE].isEnabled = 1;
	buttons[DAC_PERIOD_INCREASE].isEnabled = 1;
	buttons[DAC_TENSION_DECREASE].isEnabled = 1;
	buttons[DAC_TENSION_INCREASE].isEnabled = 1;

	xSemaphoreGive(mutex_drawer);

	placeButton(buttons[DAC_SIN], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[DAC_QUAD], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[DAC_TRI], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[DAC_SET], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[DAC_PERIOD_DECREASE], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	placeButton(buttons[DAC_PERIOD_INCREASE], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	placeButton(buttons[DAC_TENSION_DECREASE], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	placeButton(buttons[DAC_TENSION_INCREASE], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);

	placeButton(button_home, LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_CONNECT_WIDTH, BUTTON_HEIGTH);

}


void refreshBoardState (TS_StateTypeDef ts_event,  pboard_t *board) {

	if (isTouchedBoard(ts_event, board)) {

		if (!board->isPressed)
			paintPushedBoard(board);

		osDelay(100);

		selected_board = board;

		board->draw_initial_screen((void*) board);
		//board->refresh_function((void*) board);

		board->isPressed = 1;
	}
	else {

		if (board->isPressed)
			unpaintPushedBoard(board);

		board->isPressed = 0;
	}

}

void paintPushedButton (button_t button, uint16_t width, uint16_t heigth) {

	placeButton(button, LCD_COLOR_LIGHTGRAY, LCD_COLOR_BLACK, width, heigth);
}

void unpaintPushedButton (button_t button, uint16_t width, uint16_t heigth) {

	placeButton(button, LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, width, heigth);
}

void setStatus(enum connection_state isConnected){

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);

	BSP_LCD_SetTextColor(LCD_COLOR_WHITE);

	BSP_LCD_FillRect(0, STATUS_Y, 320, 20);

	BSP_LCD_SetTextColor(LCD_COLOR_BLACK);

	BSP_LCD_DisplayStringAt(30, STATUS_Y, (uint8_t*) "Status: ", LEFT_MODE);

	if (isConnected == CONNECTED) {
		BSP_LCD_SetTextColor(LCD_COLOR_GREEN);
		BSP_LCD_DisplayStringAt(140, STATUS_Y, (uint8_t*) " CONNECTED.  ", LEFT_MODE);
	}
	else if (isConnected == DISCONNECTED){
		BSP_LCD_SetTextColor(LCD_COLOR_RED);
		BSP_LCD_DisplayStringAt(140, STATUS_Y, (uint8_t*) "DISCONNECTED.", LEFT_MODE);
	}
	else if (isConnected == CONNECTING){
		BSP_LCD_SetTextColor(LCD_COLOR_YELLOW);
		BSP_LCD_DisplayStringAt(140, STATUS_Y, (uint8_t*) "CONNECTING...", LEFT_MODE);
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

void setTitle(char *title) {

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);

	BSP_LCD_SetTextColor(LCD_COLOR_WHITE);

	BSP_LCD_FillRect(0, 10, BSP_LCD_GetXSize(), 20);

	BSP_LCD_SetTextColor(LCD_COLOR_BLUE);

	BSP_LCD_DisplayStringAt(0, 5, (uint8_t*) title, CENTER_MODE);

	xSemaphoreGive(mutex_drawer);
}


void setSubTitle(char *title) {

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	BSP_LCD_SetBackColor(LCD_COLOR_WHITE);

	BSP_LCD_SetTextColor(LCD_COLOR_WHITE);

	BSP_LCD_FillRect(0, 30, BSP_LCD_GetXSize(), 20);

	BSP_LCD_SetTextColor(LCD_COLOR_RED);

	BSP_LCD_DisplayStringAt(0, 30, (uint8_t*) title, CENTER_MODE);

	xSemaphoreGive(mutex_drawer);
}


void placeBoard(pboard_t *board, uint32_t color_back, uint32_t color_text) {

	xSemaphoreTake(mutex_drawer, portMAX_DELAY);

	char buffer[32] = "Operation #";

	buffer[10] = '0' + board->order + 1;

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

	BSP_LCD_DisplayStringAt(button.x_pos + width/12, button.y_pos + heigth/6, (uint8_t*) button.title, LEFT_MODE);

	xSemaphoreGive(mutex_drawer);
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

void print_initial_screen(){

	setTitle("Projeto EA076 2016");

	setSubTitle("Gustavo C. e Anderson U.");

	setStatus(isConnected);

	/*placeButton(buttons[BUTTON_E0], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_E1], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_01], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_05], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_C8], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_C9], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_CB], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_CC], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);*/

	uint32_t color_connect;
	if (isConnected)
		color_connect = LCD_COLOR_LIGHTRED;
	else color_connect = LCD_COLOR_LIGHTGREEN;

	placeButton(buttons[BUTTON_CONNECT], color_connect, LCD_COLOR_BLACK, BUTTON_CONNECT_WIDTH, BUTTON_HEIGTH);

	placeBoard(&pboards[0], pboards[0].board.module.background_color, LCD_COLOR_BLACK);
	placeBoard(&pboards[1], pboards[1].board.module.background_color, LCD_COLOR_BLACK);
	placeBoard(&pboards[2], pboards[2].board.module.background_color, LCD_COLOR_BLACK);
	placeBoard(&pboards[3], pboards[3].board.module.background_color, LCD_COLOR_BLACK);

}

void print_new_ramp() {

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

void print_new_cycle() {

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

void repaintButtons() {

	/*
	placeButton(buttons[BUTTON_E0], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_E1], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_01], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_05], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_C8], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_C9], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_CB], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	placeButton(buttons[BUTTON_CC], LCD_COLOR_DARKBLUE, LCD_COLOR_WHITE, BUTTON_WIDTH, BUTTON_HEIGTH);
	 */
}

/*
void E0_pressed (void* button) {

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

void E1_pressed (void* button) {

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
 */

void Confirm_pressed (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	executeCommand(MSG_CONFIRM);
}

void Adjust_pressed (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	executeCommand(ADJUST);
}

/*
void C8_pressed (void* button) {

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

void C9_pressed (void* button) {

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

void CB_pressed (void* button) {

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

void CC_pressed (void* button) {

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
 */

void increaseRamp (void* button) {
	/*
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

	print_new_ramp();
	 */
}

void increaseCycle (void* button) {
	/*
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

	print_new_cycle();
	 */

}


void increaseAnalogIn (void* button) {
	/*
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

	print_new_analog_in();*/
}

void decreaseAnalogIn (void* button) {

	/*
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

	print_new_analog_in();**/
}

void decreaseCycle (void* button) {

	/*
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

	print_new_cycle();
	 */
}

void decreaseRamp (void* button) {

	/*
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

	print_new_ramp();
	 */
}

void back_initial_screen (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	aScreen = BOARD_SELECTION;

	BSP_LCD_Clear(LCD_COLOR_WHITE);

	print_initial_screen();
}

void EEPROM_Start_Handler (void* button) {

	setEEPROM_status(STARTED);

	buttons[EEPROM_START].isEnabled = 0;
	placeButton(buttons[EEPROM_START], LCD_COLOR_LIGHTBLUE, LCD_COLOR_BLACK, BUTTON_WIDTH, BUTTON_HEIGTH);

	buttons[EEPROM_STOP].isEnabled = 1;
	placeButton(buttons[EEPROM_STOP], LCD_COLOR_LIGHTBLUE, LCD_COLOR_BLACK, BUTTON_WIDTH, BUTTON_HEIGTH);

	buttons[EEPROM_STATS].isEnabled = 1;
	placeButton(buttons[EEPROM_STATS], LCD_COLOR_LIGHTBLUE, LCD_COLOR_BLACK, BUTTON_WIDTH, BUTTON_HEIGTH);

	buttons[EEPROM_STATE].isEnabled = 1;
	placeButton(buttons[EEPROM_STATE], LCD_COLOR_LIGHTBLUE, LCD_COLOR_BLACK, BUTTON_WIDTH, BUTTON_HEIGTH);


}

void EEPROM_Stop_Handler (void* button) {

	setEEPROM_status(STOPPED);

	buttons[EEPROM_START].isEnabled = 1;
	placeButton(buttons[EEPROM_START], LCD_COLOR_LIGHTBLUE, LCD_COLOR_BLACK, BUTTON_WIDTH, BUTTON_HEIGTH);

	buttons[EEPROM_STOP].isEnabled = 0;
	placeButton(buttons[EEPROM_STOP], LCD_COLOR_LIGHTBLUE, LCD_COLOR_BLACK, BUTTON_WIDTH, BUTTON_HEIGTH);

	buttons[EEPROM_STATS].isEnabled = 0;
	placeButton(buttons[EEPROM_STATS], LCD_COLOR_LIGHTBLUE, LCD_COLOR_BLACK, BUTTON_WIDTH, BUTTON_HEIGTH);

	buttons[EEPROM_STATE].isEnabled = 0;
	placeButton(buttons[EEPROM_STATE], LCD_COLOR_LIGHTBLUE, LCD_COLOR_BLACK, BUTTON_WIDTH, BUTTON_HEIGTH);
}

void EEPROM_Print_Handler (void* button) {

}

void EEPROM_List_Handler (void* button) {

}

void DAC_Decrease_Period_handler (void* button) {


	((button_t*) button)->hasBeenAcknowledged = 1;

	if (dac_period > 25) {
		dac_period -= 25;
	}

	else {
		dac_period = 0;
		((button_t*) button)->isEnabled = 0;
	}

	if (!buttons[DAC_PERIOD_INCREASE].isEnabled) {
		buttons[DAC_PERIOD_INCREASE].isEnabled = 1;
		placeButton(buttons[DAC_PERIOD_INCREASE], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	}

	setDAC_Period();
}

void DAC_Increase_Period_handler (void* button) {


	((button_t*) button)->hasBeenAcknowledged = 1;

	dac_period += 25;

	if (!buttons[DAC_PERIOD_DECREASE].isEnabled) {
		buttons[DAC_PERIOD_DECREASE].isEnabled = 1;
		placeButton(buttons[DAC_PERIOD_DECREASE], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	}

	setDAC_Period();
}

void DAC_Decrease_Tension_handler (void* button) {


	((button_t*) button)->hasBeenAcknowledged = 1;

	if (dac_set_new_value > 0.2) {
		dac_set_new_value -= 0.1;
	}

	else {
		dac_set_new_value = 0;
		((button_t*) button)->isEnabled = 0;
	}

	if (!buttons[DAC_TENSION_INCREASE].isEnabled) {
		buttons[DAC_TENSION_INCREASE].isEnabled = 1;
		placeButton(buttons[DAC_TENSION_INCREASE], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	}

	setDAC_Tension();
}

void DAC_Increase_Tension_handler (void* button) {

	((button_t*) button)->hasBeenAcknowledged = 1;

	if (dac_set_new_value < 3.2) {
		dac_set_new_value += 0.1;
	}

	else {
		dac_set_new_value = 3.3;
		((button_t*) button)->isEnabled = 0;
	}

	if (!buttons[DAC_TENSION_DECREASE].isEnabled) {
		buttons[DAC_TENSION_DECREASE].isEnabled = 1;
		placeButton(buttons[DAC_TENSION_DECREASE], LCD_COLOR_LIGHTBLUE, LCD_COLOR_WHITE, BUTTON_INCREASE_W, BUTTON_INCREASE_H);
	}

	setDAC_Tension();
}

void DAC_Sin_handler (void* button) {

	setDAC_Status(DAC_SIN);

}

void DAC_Quad_handler (void* button) {

	setDAC_Status(DAC_QUAD);

}

void DAC_Tri_handler (void* button) {

	setDAC_Status(DAC_TRI);

}

void DAC_Set_handler (void* button) {

	setDAC_Status(DAC_SET);

}

void init_interface(void) {

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
	button_home.buttonPressedHandler = back_initial_screen;

	buttons[EEPROM_START].x_pos = EEPROM_START_X;
	buttons[EEPROM_START].y_pos = EEPROM_START_Y;
	buttons[EEPROM_START].isPushed = 0;
	buttons[EEPROM_START].hasBeenAcknowledged = 0;
	buttons[EEPROM_START].isEnabled = 0;
	buttons[EEPROM_START].buttonPressedHandler = EEPROM_Start_Handler;
	memcpy(buttons[EEPROM_START].title, "Start Sampl.\0", 13);

	buttons[EEPROM_STOP].x_pos = EEPROM_STOP_X;
	buttons[EEPROM_STOP].y_pos = EEPROM_STOP_Y;
	buttons[EEPROM_STOP].isPushed = 0;
	buttons[EEPROM_STOP].hasBeenAcknowledged = 0;
	buttons[EEPROM_STOP].buttonPressedHandler = EEPROM_Stop_Handler;
	memcpy(buttons[EEPROM_STOP].title, "Stop Sampl.\0", 12);
	buttons[EEPROM_STOP].isEnabled = 0;

	buttons[EEPROM_STATS].x_pos = EEPROM_STATS_X;
	buttons[EEPROM_STATS].y_pos = EEPROM_STATS_Y;
	buttons[EEPROM_STATS].isPushed = 0;
	buttons[EEPROM_STATS].isEnabled = 0;
	buttons[EEPROM_STATS].hasBeenAcknowledged = 0;
	buttons[EEPROM_STATS].buttonPressedHandler = EEPROM_Print_Handler;
	memcpy(buttons[EEPROM_STATS].title, "Print Stats.\0", 13);

	buttons[EEPROM_STATE].x_pos = EEPROM_STATE_X;
	buttons[EEPROM_STATE].y_pos = EEPROM_STATE_Y;
	buttons[EEPROM_STATE].isPushed = 0;
	buttons[EEPROM_STATE].hasBeenAcknowledged = 0;
	buttons[EEPROM_STATE].isEnabled = 0;
	buttons[EEPROM_STATE].buttonPressedHandler = EEPROM_List_Handler;
	memcpy(buttons[EEPROM_STATE].title, "List Values.\0", 13);


	buttons[DAC_SIN].x_pos = DAC_SIN_X;
	buttons[DAC_SIN].y_pos = DAC_SIN_Y;
	buttons[DAC_SIN].isPushed = 0;
	buttons[DAC_SIN].hasBeenAcknowledged = 0;
	buttons[DAC_SIN].isEnabled = 0;
	buttons[DAC_SIN].buttonPressedHandler = DAC_Sin_handler;
	memcpy(buttons[DAC_SIN].title, "Sin() Wave\0", 11);

	buttons[DAC_QUAD].x_pos = DAC_QUAD_X;
	buttons[DAC_QUAD].y_pos = DAC_QUAD_Y;
	buttons[DAC_QUAD].isPushed = 0;
	buttons[DAC_QUAD].hasBeenAcknowledged = 0;
	buttons[DAC_QUAD].isEnabled = 0;
	buttons[DAC_QUAD].buttonPressedHandler = DAC_Quad_handler;
	memcpy(buttons[DAC_QUAD].title, "Step Wave\0", 10);

	buttons[DAC_TRI].x_pos = DAC_TRI_X;
	buttons[DAC_TRI].y_pos = DAC_TRI_Y;
	buttons[DAC_TRI].isPushed = 0;
	buttons[DAC_TRI].hasBeenAcknowledged = 0;
	buttons[DAC_TRI].isEnabled = 0;
	buttons[DAC_TRI].buttonPressedHandler = DAC_Tri_handler;
	memcpy(buttons[DAC_TRI].title, "Tri. Wave\0", 10);

	buttons[DAC_SET].x_pos = DAC_SET_X;
	buttons[DAC_SET].y_pos = DAC_SET_Y;
	buttons[DAC_SET].isPushed = 0;
	buttons[DAC_SET].hasBeenAcknowledged = 0;
	buttons[DAC_SET].isEnabled = 0;
	buttons[DAC_SET].buttonPressedHandler = DAC_Set_handler;
	memcpy(buttons[DAC_SET].title, "Set Value.\0", 10);

	buttons[DAC_PERIOD_DECREASE].x_pos = DAC_PERIOD_DECREASE_X;
	buttons[DAC_PERIOD_DECREASE].y_pos = DAC_PERIOD_DECREASE_Y;
	buttons[DAC_PERIOD_DECREASE].isPushed = 0;
	buttons[DAC_PERIOD_DECREASE].hasBeenAcknowledged = 0;
	buttons[DAC_PERIOD_DECREASE].isEnabled = 0;
	buttons[DAC_PERIOD_DECREASE].buttonPressedHandler = DAC_Decrease_Period_handler;
	memcpy(buttons[DAC_PERIOD_DECREASE].title, "<<\0", 3);

	buttons[DAC_PERIOD_INCREASE].x_pos = DAC_PERIOD_INCREASE_X;
	buttons[DAC_PERIOD_INCREASE].y_pos = DAC_PERIOD_INCREASE_Y;
	buttons[DAC_PERIOD_INCREASE].isPushed = 0;
	buttons[DAC_PERIOD_INCREASE].hasBeenAcknowledged = 0;
	buttons[DAC_PERIOD_INCREASE].isEnabled = 0;
	buttons[DAC_PERIOD_INCREASE].buttonPressedHandler = DAC_Increase_Period_handler;
	memcpy(buttons[DAC_PERIOD_INCREASE].title, ">>\0", 3);


	buttons[DAC_TENSION_DECREASE].x_pos = DAC_TENSION_DECREASE_X;
	buttons[DAC_TENSION_DECREASE].y_pos = DAC_TENSION_DECREASE_Y;
	buttons[DAC_TENSION_DECREASE].isPushed = 0;
	buttons[DAC_TENSION_DECREASE].hasBeenAcknowledged = 0;
	buttons[DAC_TENSION_DECREASE].isEnabled = 0;
	buttons[DAC_TENSION_DECREASE].buttonPressedHandler = DAC_Decrease_Tension_handler;
	memcpy(buttons[DAC_TENSION_DECREASE].title, "<<\0", 3);

	buttons[DAC_TENSION_INCREASE].x_pos = DAC_TENSION_INCREASE_X;
	buttons[DAC_TENSION_INCREASE].y_pos = DAC_TENSION_INCREASE_Y;
	buttons[DAC_TENSION_INCREASE].isPushed = 0;
	buttons[DAC_TENSION_INCREASE].hasBeenAcknowledged = 0;
	buttons[DAC_TENSION_INCREASE].isEnabled = 0;
	buttons[DAC_TENSION_INCREASE].buttonPressedHandler = DAC_Increase_Tension_handler;
	memcpy(buttons[DAC_TENSION_INCREASE].title, ">>\0", 3);


	buttons[BUTTON_CONNECT].x_pos = CONNECT_BUTTON_X;
	buttons[BUTTON_CONNECT].y_pos = CONNECT_BUTTON_Y;
	buttons[BUTTON_CONNECT].isPushed = 0;
	buttons[BUTTON_CONNECT].hasBeenAcknowledged = 0;
	buttons[BUTTON_CONNECT].buttonPressedHandler = buttonConnectPressed;
	buttons[BUTTON_CONNECT].isEnabled = 1;
	memcpy(buttons[BUTTON_CONNECT].title, "Conn.\0", 6);

	pboards_c = 0;
	for (int i = 0; i < NUMBER_OF_BOARDS; i++){
		pboards[i].board.module = NONE;

		pboards[i].isEnabled = 0;
		pboards[i].isPressed = 0;
		pboards[i].order = (uint8_t) i;
	}

	pboards[0].board.module = EEPROM;
	pboards[0].draw_initial_screen = draw_EEPROM;
	pboards[0].x = 25;
	pboards[0].y = 90;

	pboards[1].board.module = TEMP;
	pboards[1].x = 25;
	pboards[1].y = 180;

	pboards[2].board.module = ADC_O;
	pboards[2].x = 240;
	pboards[2].y = 90;

	pboards[3].board.module = DAC_O;
	pboards[3].draw_initial_screen = draw_DAC;
	pboards[3].x = 240;
	pboards[3].y = 180;

	BSP_TS_Init(BSP_LCD_GetXSize(), BSP_LCD_GetYSize());

	//setReceivedData();

	aScreen = BOARD_SELECTION;

	print_initial_screen();

	isReady = 0;
	//BSP_TS_ITConfig();
}
