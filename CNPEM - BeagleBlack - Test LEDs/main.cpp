#include <iostream>
#include <stdio.h>
#include <unistd.h>
using namespace std;

#define PATH "/sys/class/leds/beaglebone:green:usr3"


int main() {
	cout << "!!!Hello BeagleBone!!!" << endl; // prints !!!Hello BeagleBone!!!

	char option;

	FILE 	*LEDBrightnessHandle = fopen (PATH "/brightness", "r+"),
		*LEDTriggerHandle = fopen (PATH "/trigger", "r+");

	for (int i = 1 ; ; i++) {

		cout << "Iter. no. " << i << " :";

		cin >> option;

		switch (option) {

		case '1':
			cout << "Apagar LED\n";
			fwrite("0", sizeof(char), 1, LEDBrightnessHandle);
			fflush(LEDBrightnessHandle);
			break;

		case '2':

			cout << "Acender LED\n";
			fwrite("none", sizeof(char), 4, LEDTriggerHandle);
			fflush(LEDTriggerHandle);
			fwrite("1", sizeof(char), 1, LEDBrightnessHandle);
			fflush(LEDBrightnessHandle);
			break;

		case '3':

			cout << "Piscar LED\n";
			fwrite("timer", sizeof(char), 5, LEDTriggerHandle);
			fflush(LEDTriggerHandle);
			break;

		case '0':
			cout << "Falows!\n";
			fwrite("none", sizeof(char), 4, LEDTriggerHandle);
			fwrite("0", sizeof(char), 1, LEDBrightnessHandle);
			fclose(LEDBrightnessHandle);
			fclose(LEDTriggerHandle);

			return 0;

		}
	}

	return 0;

}
