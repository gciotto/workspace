#!/bin/bash

# This script runs all SPEC CPU2006 benchmarks with PIN tool in order
# to count how many instructions are executed.
# Each benchmark is run with the following command
# 'runspec --config=project1.cfg --iterations=1 --size=test --noreportable benchmark',
# where project1.cfg configures the tune parameter as 'base' and output_root as the directory
# of this project.
#
# Author: Gustavo CIOTTO PINTON RA 117136
# Computer Architecture II MO601B

SPEC_DIR=/home/gciotto/SPEC
PIN_DIR=/home/gciotto/pin-3.0-76991-gcc-linux
export PIN_ROOT=${PIN_DIR}
PROJECT_DIR=${PWD}

echo $PROJECT_DIR

function runPintoolForBenchmark {
		
	mkdir ${PROJECT_DIR}/${1}	
	
	(cd ${PIN_DIR} && ./pin -t ${PROJECT_DIR}/obj-intel64/most-used-ins.so -o  ${PROJECT_DIR}/${1}/${1}.out -- runspec --config=project1d --iterations=1 --size=test --noreportable ${1})

}

# Source shrc file. To do it, we need to be in the same directory as the file
cd ${SPEC_DIR}
source shrc

# Build inscount1 pintool, according to the pin manual page. 'inscount1' presents better performance
# than 'inscount0' because it considers inserting the analysis callback function after each trace instead of
# every intruction.
echo "Compiling 'most-used-ins.cpp'"
(cd ${PROJECT_DIR} && make dir obj-intel64/most-used-ins.so)


benchmarks=( perlbench bzip2 gcc mcf gobmk)

# Iterates only directories, which are the benchmarks
for d in ${benchmarks[@]}; do
	echo $d
	runPintoolForBenchmark "${d}"
done

