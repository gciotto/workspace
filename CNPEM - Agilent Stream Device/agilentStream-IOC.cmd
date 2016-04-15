#!../bin/linux-x86_64/streamApp

# Environment variables.

epicsEnvSet("EPICS_BASE", "/home/gciotto/epics/base-3.14.12.5")
epicsEnvSet("ASYN", "/home/gciotto/epics/asyn4-28")
epicsEnvSet("TOP", "/home/gciotto/agilent-ion pump/stream-ioc")
epicsEnvSet("ARCH", "linux-x86_64")
epicsEnvSet ("STREAM_PROTOCOL_PATH", "$(TOP)/protocol")

# Load database definition file.

cd ${TOP}
dbLoadDatabase("dbd/streamApp.dbd")
streamApp_registerRecordDeviceDriver(pdbbase)

# Beginning of device configurations. In each block below, the communication to a specific device
# and respective record loading are specified.

####################################################################################################
#
# MBTemp - Beginning of configuration
#
####################################################################################################

# Only for test purposes
# drvAsynIPServerPortConfigure("portName", "serverInfo", maxClients, priority, 
#      noAutoConnect, noProcessEos);
drvAsynIPPortConfigure ("udpPort1", "localhost:556 UDP")

## According to the reference manual, page 73, the communication format must follow:
# 8 data bit
# No parity
# 1 stop bit
# Baud rate: 9600
# drvAsynSerialPortConfigure("serialPort1", "/dev/ttyUSB0")
# asynSetOption("serialPort1", 0, "baud", "9600")
# asynSetOption("serialPort1", 0, "bits", "8")
# asynSetOption("serialPort1", 0, "parity", "none")
# asynSetOption("serialPort1", 0, "stop", "1")

# Records corresponding to the eight temperature measurements given by a board at serial address 1.
dbLoadRecords("database/Agilent.db.db", "RECORD_NAME = Agilent:FANTemperature, SCAN_RATE = 1 second, DESCRIPTION = TESTE, PORT = udpPort1")

####################################################################################################
#
# MBTemp - End of configuration
#
####################################################################################################

# End of device configurations.

# Effectively initializes the IOC.

cd iocBoot
iocInit