## PROSAC pyHP232

### Authors:
Gustavo CIOTTO PINTON  
Eduardo PEREIRA COELHO

### Description

This project consists of a python module which receives PROSAC requests and translates them to the HP U34401A multimeter protocol. It should be use together with an USB - RS232 converter in order to remplace the set 'PSICO - HP232 board', that is still in use in some places.

### Setup

1. Open `pyHP232.service` and insert the right directory address of the file `pyHP232.python`
2. Copy `pyHP232.service` to `/lib/systemd/system/`
3. Enable this service with `systemctl enable pyHP232.service`
4. Start it typing in `systemctl start pyHP232.service`

Enjoy!
