################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../LwIP/src/api/api_lib.c \
../LwIP/src/api/api_msg.c \
../LwIP/src/api/err.c \
../LwIP/src/api/netbuf.c \
../LwIP/src/api/netdb.c \
../LwIP/src/api/netifapi.c \
../LwIP/src/api/sockets.c \
../LwIP/src/api/tcpip.c 

OBJS += \
./LwIP/src/api/api_lib.o \
./LwIP/src/api/api_msg.o \
./LwIP/src/api/err.o \
./LwIP/src/api/netbuf.o \
./LwIP/src/api/netdb.o \
./LwIP/src/api/netifapi.o \
./LwIP/src/api/sockets.o \
./LwIP/src/api/tcpip.o 

C_DEPS += \
./LwIP/src/api/api_lib.d \
./LwIP/src/api/api_msg.d \
./LwIP/src/api/err.d \
./LwIP/src/api/netbuf.d \
./LwIP/src/api/netdb.d \
./LwIP/src/api/netifapi.d \
./LwIP/src/api/sockets.d \
./LwIP/src/api/tcpip.d 


# Each subdirectory must supply rules for building sources it contributes
LwIP/src/api/%.o: ../LwIP/src/api/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: MCU GCC Compiler'
	@echo $(PWD)
	arm-none-eabi-gcc -mcpu=cortex-m7 -mthumb -mfloat-abi=hard -mfpu=fpv5-sp-d16 -DSTM32F746G_DISCO -DSTM32F746NGHx -DSTM32F7 -DSTM32 -DDEBUG -DUSE_HAL_DRIVER -DSTM32F746xx -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/inc" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/LwIP/system" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/LwIP/src/include/ipv4" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/FreeRTOS/Source/include" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/FreeRTOS/Source/portable/GCC/ARM_CM7/r0p1" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/FreeRTOS/Source/CMSIS_RTOS" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/LwIP/src/include" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/inc/mxconstants.h" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/STM32746G-Discovery" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Fonts" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/st7735" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/Common" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/wm8994" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/exc7200" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/ov9655" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/mfxstm32l152" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/s5k5cag" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/rk043fn48h" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/n25q128a" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/ampire640480" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/stmpe811" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/ampire480272" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/ft5336" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/n25q512a" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/ts3510" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Log" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/HAL_Driver/Inc" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/HAL_Driver/Inc/Legacy" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/CMSIS/device" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/CMSIS/core" -include"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/inc/mxconstants.h" -O0 -g3 -Wall -fmessage-length=0 -ffunction-sections -c -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


