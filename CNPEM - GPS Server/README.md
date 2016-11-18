## GPS - NTP building scripts ##

###Author 

Gustavo CIOTTO PINTON

### Description

The repository contains a set of scripts used to setup and initialize NTPD and GPSD services in a BeagleBone board. It also contains
a device overlay file, which configures the required GPIO pins. A bash file is provided in order to build and configure all components.

### Contents ###

The repository is composed of three types of files:

1. `build.sh`: it downloads, installs and configures the dependencies of the project. Refer to the next section.
2. \*.service: systemd service files
3. overlay_GPS.dts: pin configuration files. Two versions are provided that use different pins for the PPS signal: `overlay\_GPS.dts` uses P8.7 while `overlay\_GPS\_P9.dts`, P9.12. `build.sh` uses the latter by default. The serial communication is done through pins P9.11 (UART RX) and P9.13 (UART TX).

### Running ###

1. Execute `./build.sh --help` to list the available options
2. Execute `./build.sh --all` to set everything up
 
