EESchema Schematic File Version 2
LIBS:power
LIBS:device
LIBS:transistors
LIBS:conn
LIBS:linear
LIBS:regul
LIBS:74xx
LIBS:cmos4000
LIBS:adc-dac
LIBS:memory
LIBS:xilinx
LIBS:microcontrollers
LIBS:dsp
LIBS:microchip
LIBS:analog_switches
LIBS:motorola
LIBS:texas
LIBS:intel
LIBS:audio
LIBS:interface
LIBS:digital-audio
LIBS:philips
LIBS:display
LIBS:cypress
LIBS:siliconi
LIBS:opto
LIBS:atmel
LIBS:contrib
LIBS:valves
LIBS:stm32
LIBS:stm8
LIBS:GPS_Cape
LIBS:Cape-485-BBB
LIBS:Cape-485-BBB-cache
LIBS:Controle
LIBS:GPS Cape-cache
EELAYER 25 0
EELAYER END
$Descr A4 11693 8268
encoding utf-8
Sheet 1 1
Title "GPS BBB Cape"
Date "2016-06-30"
Rev ""
Comp "CNPEM - LNLS"
Comment1 "Grupo de Controle"
Comment2 "Gustavo Ciotto Pinton"
Comment3 "Adafruit Ultimate GPS Breakout"
Comment4 "GPS Cape para BeagleBone Black compatível com o módulo receptor "
$EndDescr
$Comp
L R R101
U 1 1 577534EF
P 5250 1250
F 0 "R101" V 5350 1200 50  0000 C CNN
F 1 "1M" V 5250 1250 50  0000 C CNN
F 2 "Resistors_SMD:R_0805_HandSoldering" V 5180 1250 50  0001 C CNN
F 3 "" H 5250 1250 50  0000 C CNN
	1    5250 1250
	0    -1   -1   0   
$EndComp
$Comp
L C C101
U 1 1 5775352A
P 5750 1250
F 0 "C101" V 5850 1350 50  0000 L CNN
F 1 "1u" V 5850 1050 50  0000 L CNN
F 2 "Capacitors_SMD:C_0805_HandSoldering" H 5788 1100 50  0001 C CNN
F 3 "" H 5750 1250 50  0000 C CNN
	1    5750 1250
	0    -1   -1   0   
$EndComp
$Comp
L Adafruit_ultimate_GPS_breakout J101
U 1 1 57753A05
P 2850 1800
F 0 "J101" H 2850 850 60  0000 C CNN
F 1 "Adafruit_ultimate_GPS_breakout" H 2850 1850 60  0000 C CNN
F 2 "Controle:Adafruit_Ultimate_GPS_Breakout" H 2900 1250 60  0001 C CNN
F 3 "" H 2900 1250 60  0000 C CNN
	1    2850 1800
	1    0    0    -1  
$EndComp
$Comp
L VCC #PWR01
U 1 1 57751A67
P 4950 1150
F 0 "#PWR01" H 4950 1000 50  0001 C CNN
F 1 "VCC" H 4950 1300 50  0000 C CNN
F 2 "" H 4950 1150 50  0000 C CNN
F 3 "" H 4950 1150 50  0000 C CNN
	1    4950 1150
	1    0    0    -1  
$EndComp
$Comp
L GND #PWR02
U 1 1 57752120
P 6700 1250
F 0 "#PWR02" H 6700 1000 50  0001 C CNN
F 1 "GND" H 6700 1100 50  0000 C CNN
F 2 "" H 6700 1250 50  0000 C CNN
F 3 "" H 6700 1250 50  0000 C CNN
	1    6700 1250
	0    -1   -1   0   
$EndComp
$Comp
L 74LS123 U101
U 1 1 57752C41
P 5750 2200
F 0 "U101" H 5750 2150 50  0000 C CNN
F 1 "74LS123" H 5750 2050 50  0000 C CNN
F 2 "Housings_DIP:DIP-16_W7.62mm" H 5750 2200 50  0001 C CNN
F 3 "" H 5750 2200 50  0000 C CNN
	1    5750 2200
	1    0    0    -1  
$EndComp
$Comp
L GND #PWR03
U 1 1 57753283
P 4500 1900
F 0 "#PWR03" H 4500 1650 50  0001 C CNN
F 1 "GND" H 4500 1750 50  0000 C CNN
F 2 "" H 4500 1900 50  0000 C CNN
F 3 "" H 4500 1900 50  0000 C CNN
	1    4500 1900
	1    0    0    -1  
$EndComp
Text Label 5000 2000 2    60   ~ 0
PPS
Text Label 3300 2550 0    60   ~ 0
PPS
Text Notes 5600 1000 0    60   ~ 0
Alargador de Pulsos de PPS
$Comp
L C C102
U 1 1 57753B5D
P 6200 3000
F 0 "C102" V 6150 3100 50  0000 L CNN
F 1 "0.1u" V 6150 2750 50  0000 L CNN
F 2 "Capacitors_SMD:C_0805_HandSoldering" H 6238 2850 50  0001 C CNN
F 3 "" H 6200 3000 50  0000 C CNN
	1    6200 3000
	0    1    1    0   
$EndComp
$Comp
L GND #PWR04
U 1 1 57754733
P 3800 2850
F 0 "#PWR04" H 3800 2600 50  0001 C CNN
F 1 "GND" H 3800 2700 50  0000 C CNN
F 2 "" H 3800 2850 50  0000 C CNN
F 3 "" H 3800 2850 50  0000 C CNN
	1    3800 2850
	1    0    0    -1  
$EndComp
Text Notes 2900 1000 0    60   ~ 0
GPS Module Receiver
NoConn ~ 2350 1950
$Comp
L CP C103
U 1 1 57754892
P 1800 2500
F 0 "C103" H 1825 2600 50  0000 L CNN
F 1 "1.0" H 1825 2400 50  0000 L CNN
F 2 "Controle:SuperCap_pads" H 1838 2350 50  0001 C CNN
F 3 "" H 1800 2500 50  0000 C CNN
	1    1800 2500
	1    0    0    -1  
$EndComp
$Comp
L D D101
U 1 1 57754A32
P 1800 1650
F 0 "D101" H 1800 1750 50  0000 C CNN
F 1 "D" H 1800 1550 50  0000 C CNN
F 2 "LEDs:LED_0805" H 1800 1650 50  0001 C CNN
F 3 "" H 1800 1650 50  0000 C CNN
	1    1800 1650
	0    -1   -1   0   
$EndComp
$Comp
L R R102
U 1 1 57754BB1
P 1800 2050
F 0 "R102" V 1880 2050 50  0000 C CNN
F 1 "100" V 1800 2050 50  0000 C CNN
F 2 "Resistors_SMD:R_0805_HandSoldering" V 1730 2050 50  0001 C CNN
F 3 "" H 1800 2050 50  0000 C CNN
	1    1800 2050
	1    0    0    -1  
$EndComp
NoConn ~ 2350 2400
NoConn ~ 6500 2500
NoConn ~ 7700 3150
NoConn ~ 7700 3050
NoConn ~ 8200 3150
NoConn ~ 8200 2950
NoConn ~ 8200 2850
NoConn ~ 8200 2750
NoConn ~ 7700 2950
NoConn ~ 7700 2850
NoConn ~ 7700 2750
NoConn ~ 7700 2650
NoConn ~ 8200 2650
NoConn ~ 8200 2550
NoConn ~ 8200 2450
NoConn ~ 7700 2550
NoConn ~ 7700 2450
NoConn ~ 7700 2350
NoConn ~ 7700 2250
NoConn ~ 8200 2350
NoConn ~ 8200 2250
NoConn ~ 8200 2150
NoConn ~ 7700 2150
NoConn ~ 7700 2050
NoConn ~ 8200 2050
NoConn ~ 8200 1950
NoConn ~ 7700 1950
NoConn ~ 7700 1850
NoConn ~ 8200 1850
NoConn ~ 8200 1750
NoConn ~ 7700 1750
NoConn ~ 7700 1650
NoConn ~ 8200 1650
NoConn ~ 8200 1550
NoConn ~ 8200 1350
NoConn ~ 7700 1350
NoConn ~ 7700 1250
NoConn ~ 7700 1150
Text Label 3300 1950 0    60   ~ 0
TX_GPS
Text Label 3300 2100 0    60   ~ 0
RX_GPS
Text Label 7700 1450 2    60   ~ 0
TX_GPS
Text Label 7700 1550 2    60   ~ 0
RX_GPS
Text Label 6500 1900 0    60   ~ 0
PPS_ENL
$Comp
L Jumper_NC_Small JP102
U 1 1 57758151
P 8450 1600
F 0 "JP102" H 8450 1680 50  0000 C CNN
F 1 "Jumper_NC_Small" H 8460 1540 50  0001 C CNN
F 2 "Pin_Headers:Pin_Header_Straight_1x02" H 8450 1600 50  0001 C CNN
F 3 "" H 8450 1600 50  0000 C CNN
	1    8450 1600
	1    0    0    -1  
$EndComp
$Comp
L Jumper_NO_Small JP101
U 1 1 577581B4
P 8450 1450
F 0 "JP101" H 8450 1530 50  0000 C CNN
F 1 "Jumper_NO_Small" H 8460 1390 50  0001 C CNN
F 2 "Pin_Headers:Pin_Header_Straight_1x02" H 8450 1450 50  0001 C CNN
F 3 "" H 8450 1450 50  0000 C CNN
	1    8450 1450
	1    0    0    -1  
$EndComp
Text Label 8550 1450 0    60   ~ 0
PPS
Text Label 8550 1600 0    60   ~ 0
PPS_ENL
$Comp
L VCC #PWR05
U 1 1 5775DB8D
P 9050 1100
F 0 "#PWR05" H 9050 950 50  0001 C CNN
F 1 "VCC" H 9050 1250 50  0000 C CNN
F 2 "" H 9050 1100 50  0000 C CNN
F 3 "" H 9050 1100 50  0000 C CNN
	1    9050 1100
	-1   0    0    1   
$EndComp
$Comp
L GND #PWR06
U 1 1 5776B4BF
P 8950 900
F 0 "#PWR06" H 8950 650 50  0001 C CNN
F 1 "GND" H 8950 750 50  0000 C CNN
F 2 "" H 8950 900 50  0000 C CNN
F 3 "" H 8950 900 50  0000 C CNN
	1    8950 900 
	-1   0    0    1   
$EndComp
NoConn ~ 7700 950 
NoConn ~ 7700 1050
$Comp
L BeagleBone Beagle101
U 2 1 5775593F
P 7950 2050
F 0 "Beagle101" H 7950 3300 50  0000 C CNN
F 1 "BeagleBone" H 8100 800 50  0000 C CNN
F 2 "Controle:BEAGLEBONEBLACK" H 7950 950 60  0001 C CNN
F 3 "" H 7950 950 60  0000 C CNN
	2    7950 2050
	1    0    0    -1  
$EndComp
$Comp
L VCC #PWR07
U 1 1 578CD92F
P 3750 1250
F 0 "#PWR07" H 3750 1100 50  0001 C CNN
F 1 "VCC" H 3750 1400 50  0000 C CNN
F 2 "" H 3750 1250 50  0000 C CNN
F 3 "" H 3750 1250 50  0000 C CNN
	1    3750 1250
	1    0    0    -1  
$EndComp
$Comp
L BeagleBone Beagle101
U 1 1 578CFCCB
P 9850 2100
F 0 "Beagle101" H 9850 3350 50  0000 C CNN
F 1 "BeagleBone" H 10000 850 50  0000 C CNN
F 2 "Controle:BEAGLEBONEBLACK" H 9850 1000 60  0001 C CNN
F 3 "" H 9850 1000 60  0000 C CNN
	1    9850 2100
	1    0    0    -1  
$EndComp
NoConn ~ 10100 1000
NoConn ~ 10100 1100
NoConn ~ 10100 1200
NoConn ~ 10100 1300
NoConn ~ 10100 1400
NoConn ~ 10100 1500
NoConn ~ 10100 1600
NoConn ~ 10100 1700
NoConn ~ 10100 1800
NoConn ~ 10100 1900
NoConn ~ 10100 2000
NoConn ~ 10100 2100
NoConn ~ 10100 2200
NoConn ~ 10100 2300
NoConn ~ 10100 2400
NoConn ~ 10100 2500
NoConn ~ 10100 2600
NoConn ~ 10100 2700
NoConn ~ 10100 2800
NoConn ~ 10100 2900
NoConn ~ 10100 3000
NoConn ~ 10100 3100
NoConn ~ 10100 3200
NoConn ~ 9600 3200
NoConn ~ 9600 3100
NoConn ~ 9600 3000
NoConn ~ 9600 2900
NoConn ~ 9600 2800
NoConn ~ 9600 2700
NoConn ~ 9600 2600
NoConn ~ 9600 2500
NoConn ~ 9600 2400
NoConn ~ 9600 2300
NoConn ~ 9600 2200
NoConn ~ 9600 2100
NoConn ~ 9600 2000
NoConn ~ 9600 1900
NoConn ~ 9600 1800
NoConn ~ 9600 1700
NoConn ~ 9600 1600
NoConn ~ 9600 1500
NoConn ~ 9600 1400
NoConn ~ 9600 1300
NoConn ~ 9600 1200
NoConn ~ 9600 1100
$Comp
L R R103
U 1 1 578CDE9B
P 4650 2450
F 0 "R103" V 4730 2450 50  0000 C CNN
F 1 "10k" V 4650 2450 50  0000 C CNN
F 2 "Resistors_SMD:R_0805_HandSoldering" V 4580 2450 50  0001 C CNN
F 3 "" H 4650 2450 50  0000 C CNN
	1    4650 2450
	1    0    0    -1  
$EndComp
$Comp
L C C104
U 1 1 578CEC32
P 1750 3850
F 0 "C104" H 1775 3950 50  0000 L CNN
F 1 "100n" H 1775 3750 50  0000 L CNN
F 2 "Capacitors_SMD:C_0805_HandSoldering" H 1788 3700 50  0001 C CNN
F 3 "" H 1750 3850 50  0000 C CNN
	1    1750 3850
	1    0    0    -1  
$EndComp
$Comp
L VCC #PWR08
U 1 1 578CED58
P 1750 3600
F 0 "#PWR08" H 1750 3450 50  0001 C CNN
F 1 "VCC" H 1750 3750 50  0000 C CNN
F 2 "" H 1750 3600 50  0000 C CNN
F 3 "" H 1750 3600 50  0000 C CNN
	1    1750 3600
	1    0    0    -1  
$EndComp
$Comp
L GND #PWR09
U 1 1 578CED8D
P 1750 4150
F 0 "#PWR09" H 1750 3900 50  0001 C CNN
F 1 "GND" H 1750 4000 50  0000 C CNN
F 2 "" H 1750 4150 50  0000 C CNN
F 3 "" H 1750 4150 50  0000 C CNN
	1    1750 4150
	1    0    0    -1  
$EndComp
Text Notes 1500 4500 0    60   ~ 0
Capacitor de Ruído Alargador de Pulso
$Comp
L VCC #PWR010
U 1 1 578D2E37
P 4100 3550
F 0 "#PWR010" H 4100 3400 50  0001 C CNN
F 1 "VCC" H 4100 3700 50  0000 C CNN
F 2 "" H 4100 3550 50  0000 C CNN
F 3 "" H 4100 3550 50  0000 C CNN
	1    4100 3550
	1    0    0    -1  
$EndComp
$Comp
L C C105
U 1 1 578D2E6C
P 4100 3850
F 0 "C105" H 4125 3950 50  0000 L CNN
F 1 "100n" H 4125 3750 50  0000 L CNN
F 2 "Capacitors_SMD:C_0805_HandSoldering" H 4138 3700 50  0001 C CNN
F 3 "" H 4100 3850 50  0000 C CNN
	1    4100 3850
	1    0    0    -1  
$EndComp
$Comp
L GND #PWR011
U 1 1 578D2F07
P 4100 4200
F 0 "#PWR011" H 4100 3950 50  0001 C CNN
F 1 "GND" H 4100 4050 50  0000 C CNN
F 2 "" H 4100 4200 50  0000 C CNN
F 3 "" H 4100 4200 50  0000 C CNN
	1    4100 4200
	1    0    0    -1  
$EndComp
Text Notes 3750 4500 0    60   ~ 0
Capacitor de Ruído GPS
$Comp
L R R104
U 1 1 578CF6AE
P 2100 1850
F 0 "R104" V 2180 1850 50  0000 C CNN
F 1 "10k" V 2100 1850 50  0000 C CNN
F 2 "Resistors_SMD:R_0805_HandSoldering" V 2030 1850 50  0001 C CNN
F 3 "" H 2100 1850 50  0000 C CNN
	1    2100 1850
	1    0    0    -1  
$EndComp
$Comp
L C C106
U 1 1 578CF754
P 2100 2550
F 0 "C106" V 2050 2650 50  0000 L CNN
F 1 "0.1u" V 2050 2300 50  0000 L CNN
F 2 "Capacitors_SMD:C_0805_HandSoldering" H 2138 2400 50  0001 C CNN
F 3 "" H 2100 2550 50  0000 C CNN
	1    2100 2550
	-1   0    0    1   
$EndComp
NoConn ~ 9600 1000
NoConn ~ 8200 3050
$Comp
L +5V #PWR012
U 1 1 578F8DA9
P 8800 1250
F 0 "#PWR012" H 8800 1100 50  0001 C CNN
F 1 "+5V" H 8800 1390 50  0000 C CNN
F 2 "" H 8800 1250 50  0000 C CNN
F 3 "" H 8800 1250 50  0000 C CNN
	1    8800 1250
	1    0    0    -1  
$EndComp
$Comp
L +5V #PWR013
U 1 1 578F95AD
P 6650 3450
F 0 "#PWR013" H 6650 3300 50  0001 C CNN
F 1 "+5V" H 6650 3590 50  0000 C CNN
F 2 "" H 6650 3450 50  0000 C CNN
F 3 "" H 6650 3450 50  0000 C CNN
	1    6650 3450
	1    0    0    -1  
$EndComp
Text Label 5800 4900 2    60   ~ 0
PPS_ENL
$Comp
L GND #PWR014
U 1 1 578F9ED2
P 6650 5200
F 0 "#PWR014" H 6650 4950 50  0001 C CNN
F 1 "GND" H 6650 5050 50  0000 C CNN
F 2 "" H 6650 5200 50  0000 C CNN
F 3 "" H 6650 5200 50  0000 C CNN
	1    6650 5200
	1    0    0    -1  
$EndComp
$Comp
L R R105
U 1 1 578FA259
P 6100 4900
F 0 "R105" V 6200 4900 50  0000 C CNN
F 1 "10k" V 6100 4900 50  0000 C CNN
F 2 "Resistors_SMD:R_0805_HandSoldering" V 6030 4900 50  0001 C CNN
F 3 "" H 6100 4900 50  0000 C CNN
	1    6100 4900
	0    1    1    0   
$EndComp
Wire Wire Line
	4650 1250 5100 1250
Wire Wire Line
	5400 1250 5600 1250
Wire Wire Line
	5900 1250 6700 1250
Wire Wire Line
	5550 1400 5550 1250
Connection ~ 5550 1250
Wire Notes Line
	4350 3150 6950 3150
Wire Notes Line
	6950 3150 6950 900 
Wire Notes Line
	6950 900  4350 900 
Wire Wire Line
	4950 1150 4950 1250
Connection ~ 4950 1250
Wire Wire Line
	5950 1400 5950 1250
Connection ~ 5950 1250
Wire Wire Line
	4500 1900 4500 1850
Wire Wire Line
	4500 1850 5000 1850
Wire Notes Line
	1450 900  1450 3150
Wire Notes Line
	1450 3150 4050 3150
Wire Notes Line
	1450 900  4050 900 
Wire Wire Line
	4650 3000 6050 3000
Connection ~ 5750 3000
Wire Wire Line
	6350 3000 6550 3000
Wire Wire Line
	6550 3000 6550 1250
Connection ~ 6550 1250
Wire Notes Line
	4350 900  4350 3150
Wire Wire Line
	3300 2250 3800 2250
Wire Wire Line
	3800 2250 3800 2850
Wire Wire Line
	3750 2400 3300 2400
Wire Wire Line
	1800 2650 1800 2800
Wire Wire Line
	1800 2800 3800 2800
Connection ~ 3800 2800
Wire Wire Line
	2350 2250 1800 2250
Connection ~ 1800 2250
Wire Wire Line
	1800 1900 1800 1800
Wire Wire Line
	1800 1500 1800 1400
Wire Wire Line
	1800 2200 1800 2350
Connection ~ 2100 1400
Wire Notes Line
	4050 900  4050 3150
Wire Wire Line
	3750 1250 3750 2400
Wire Wire Line
	1800 1400 3750 1400
Connection ~ 3750 1400
Wire Wire Line
	8200 1450 8350 1450
Wire Wire Line
	8350 1600 8300 1600
Wire Wire Line
	8300 1600 8300 1450
Connection ~ 8300 1450
Wire Wire Line
	9050 1050 9050 1100
Wire Wire Line
	8950 950  8950 900 
Wire Wire Line
	8200 950  8950 950 
Wire Wire Line
	4650 1250 4650 2300
Wire Wire Line
	4650 2600 4650 3000
Wire Wire Line
	1750 4150 1750 4000
Wire Wire Line
	1750 3700 1750 3600
Wire Notes Line
	1450 3350 2200 3350
Wire Notes Line
	2200 3350 2200 4550
Wire Notes Line
	2200 4550 1450 4550
Wire Notes Line
	1450 4550 1450 3350
Wire Wire Line
	4100 3550 4100 3700
Wire Wire Line
	4100 4200 4100 4000
Wire Notes Line
	3700 3250 3700 4550
Wire Notes Line
	3700 4550 4800 4550
Wire Notes Line
	4800 4550 4800 3250
Wire Notes Line
	4800 3250 3700 3250
Wire Wire Line
	8200 1050 9050 1050
Wire Wire Line
	2100 2700 2100 2800
Connection ~ 2100 2800
Wire Wire Line
	2350 2100 2100 2100
Connection ~ 2100 2100
Wire Wire Line
	2100 1700 2100 1400
Wire Wire Line
	2100 2000 2100 2400
Wire Notes Line
	5350 3250 7550 3250
Wire Notes Line
	5350 5450 7550 5450
Wire Notes Line
	5350 3250 5350 5450
Wire Wire Line
	5800 4900 5950 4900
Wire Notes Line
	7550 5450 7550 3250
$Comp
L R R106
U 1 1 578FB69A
P 6650 4350
F 0 "R106" V 6730 4350 50  0000 C CNN
F 1 "100" V 6650 4350 50  0000 C CNN
F 2 "Resistors_SMD:R_0805_HandSoldering" V 6580 4350 50  0001 C CNN
F 3 "" H 6650 4350 50  0000 C CNN
	1    6650 4350
	1    0    0    -1  
$EndComp
$Comp
L LED D102
U 1 1 578FB6EB
P 6650 3900
F 0 "D102" H 6650 4000 50  0000 C CNN
F 1 "LED" H 6650 3800 50  0000 C CNN
F 2 "LEDs:LED_0805" H 6650 3900 50  0001 C CNN
F 3 "" H 6650 3900 50  0000 C CNN
	1    6650 3900
	0    -1   -1   0   
$EndComp
Wire Wire Line
	6650 3700 6650 3450
Wire Wire Line
	6650 4200 6650 4100
Text Notes 5450 3400 0    60   ~ 0
LED PPS
$Comp
L Q_NPN_BEC Q101
U 1 1 578FE3E0
P 6550 4900
F 0 "Q101" H 6850 4950 50  0000 R CNN
F 1 "Q_NPN_BEC" H 7150 4850 50  0000 R CNN
F 2 "TO_SOT_Packages_SMD:SOT-23_Handsoldering" H 6750 5000 50  0001 C CNN
F 3 "" H 6550 4900 50  0000 C CNN
	1    6550 4900
	1    0    0    -1  
$EndComp
Wire Wire Line
	6650 4700 6650 4500
Wire Wire Line
	6350 4900 6250 4900
Wire Wire Line
	6650 5200 6650 5100
NoConn ~ 8200 1150
Wire Wire Line
	8800 1250 8200 1250
$EndSCHEMATC
