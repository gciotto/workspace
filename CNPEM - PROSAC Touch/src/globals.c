#include "globals.h"
#include "stm32746g_discovery_lcd.h"
#include <math.h>

/* supported PROSAC boards */
const module_t NONE 	= { .type = 0x3F, .name = "NONE", 	  .readBytesCount = 0,  .writeBytesCount = 0, .background_color = LCD_COLOR_LIGHTBLUE,    .pushed_background_color = LCD_COLOR_DARKBLUE};
const module_t LCN12BMP = { .type = 0x01, .name = "LCN12BMP", .readBytesCount = 3,  .writeBytesCount = 3, .background_color = LCD_COLOR_LIGHTGREEN,   .pushed_background_color = LCD_COLOR_DARKGREEN};
const module_t LCN12BBP = { .type = 0x02, .name = "LCN12BBP", .readBytesCount = 3,  .writeBytesCount = 3, .background_color = LCD_COLOR_LIGHTRED,     .pushed_background_color = LCD_COLOR_DARKRED };
const module_t LCN16BMP = { .type = 0x03, .name = "LCN16BMP", .readBytesCount = 3,  .writeBytesCount = 3, .background_color = LCD_COLOR_LIGHTCYAN,    .pushed_background_color = LCD_COLOR_DARKCYAN };
const module_t LCN16BBP = { .type = 0x04, .name = "LCN16BBP", .readBytesCount = 3,  .writeBytesCount = 3, .background_color = LCD_COLOR_LIGHTMAGENTA, .pushed_background_color = LCD_COLOR_DARKMAGENTA };
const module_t MUX16BBP = { .type = 0x11, .name = "MUX16BBP", .readBytesCount = 21, .writeBytesCount = 4, .background_color = LCD_COLOR_LIGHTYELLOW,  .pushed_background_color = LCD_COLOR_DARKYELLOW };

/* FUNCTION generate_ramps:
 * Creates three ramp curves */
void generate_ramps() {

	for(int i = 0; i < 1021; ++i) {

		ramps[0][i] = (i+1)*30;
		ramps[1][i] = 3*i;
		ramps[2][i] = 4000 * rand();
	}
}

/* Finds a module board by code */
module_t findModule(uint8_t code) {

	if (code == LCN12BMP.type) return LCN12BMP;
	else if (code == LCN12BBP.type) return LCN12BBP;
	else if (code == LCN16BMP.type) return LCN16BMP;
	else if (code == LCN16BBP.type) return LCN16BBP;
	else if (code == MUX16BBP.type) return MUX16BBP;

	return NONE;
}
