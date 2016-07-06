#!/bin/bash

echo -n "Compiling .p file... "

pasm -b pru_prosac.p

echo "Ok!"

echo -n "Compiling .c files... "

gcc pru_prosac_loader.c -o pru_prosac_loader -lprussdrv
gcc pru_prosac_loader_with_threads.c -o pru_prosac_loader_with_threads -lprussdrv -lpthread

echo "Ok!"


