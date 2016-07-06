#!/bin/bash

echo -n "Compiling .dts file... "

dtc -O dtb -o pru_prosac-00A0.dtbo -b 0 -@ pru_overlay_prosac.dts

echo "Ok!"

echo -n "Moving .dtbo file... "

rm /lib/firmware/pru_prosac-00A0.dtbo

cp pru_prosac-00A0.dtbo /lib/firmware/
rm pru_prosac-00A0.dtbo

echo "Ok!"

echo "pru_prosac" > $SLOTS
