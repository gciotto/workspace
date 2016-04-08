#!/bin/bash

dtc -O dtb -o pru_overlay-00A0.dtbo -b 0 -@ pru_overlay.dts
pasm -b pru_client.p
gcc pru_ethernet_client.c -o pru_ethernet_client -lprussdrv
