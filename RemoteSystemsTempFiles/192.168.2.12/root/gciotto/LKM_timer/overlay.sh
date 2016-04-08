#!/bin/bash

cd /lib/firmware/
echo pwm_P9_22 > $SLOTS
echo pwm > /sys/devices/ocp.3/P9_22_pinmux.12/state
cd /sys/class/pwm/
echo 0 > export
cd /sys/class/pwm/pwm0/
echo 2000000 > duty_ns
echo 70000000 > period_ns
echo 1 > run
cd ~/gciotto/LKM_timer
