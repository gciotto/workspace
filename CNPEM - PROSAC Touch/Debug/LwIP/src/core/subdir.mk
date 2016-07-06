################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../LwIP/src/core/def.c \
../LwIP/src/core/dhcp.c \
../LwIP/src/core/dns.c \
../LwIP/src/core/init.c \
../LwIP/src/core/lwip_timers.c \
../LwIP/src/core/mem.c \
../LwIP/src/core/memp.c \
../LwIP/src/core/netif.c \
../LwIP/src/core/pbuf.c \
../LwIP/src/core/raw.c \
../LwIP/src/core/stats.c \
../LwIP/src/core/sys.c \
../LwIP/src/core/tcp.c \
../LwIP/src/core/tcp_in.c \
../LwIP/src/core/tcp_out.c \
../LwIP/src/core/udp.c 

OBJS += \
./LwIP/src/core/def.o \
./LwIP/src/core/dhcp.o \
./LwIP/src/core/dns.o \
./LwIP/src/core/init.o \
./LwIP/src/core/lwip_timers.o \
./LwIP/src/core/mem.o \
./LwIP/src/core/memp.o \
./LwIP/src/core/netif.o \
./LwIP/src/core/pbuf.o \
./LwIP/src/core/raw.o \
./LwIP/src/core/stats.o \
./LwIP/src/core/sys.o \
./LwIP/src/core/tcp.o \
./LwIP/src/core/tcp_in.o \
./LwIP/src/core/tcp_out.o \
./LwIP/src/core/udp.o 

C_DEPS += \
./LwIP/src/core/def.d \
./LwIP/src/core/dhcp.d \
./LwIP/src/core/dns.d \
./LwIP/src/core/init.d \
./LwIP/src/core/lwip_timers.d \
./LwIP/src/core/mem.d \
./LwIP/src/core/memp.d \
./LwIP/src/core/netif.d \
./LwIP/src/core/pbuf.d \
./LwIP/src/core/raw.d \
./LwIP/src/core/stats.d \
./LwIP/src/core/sys.d \
./LwIP/src/core/tcp.d \
./LwIP/src/core/tcp_in.d \
./LwIP/src/core/tcp_out.d \
./LwIP/src/core/udp.d 


# Each subdirectory must supply rules for building sources it contributes
LwIP/src/core/%.o: ../LwIP/src/core/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: MCU GCC Compiler'
	@echo $(PWD)
	arm-none-eabi-gcc -mcpu=cortex-m7 -mthumb -mfloat-abi=hard -mfpu=fpv5-sp-d16 -DSTM32F746G_DISCO -DSTM32F746NGHx -DSTM32F7 -DSTM32 -DDEBUG -DUSE_HAL_DRIVER -DSTM32F746xx -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/inc" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/LwIP/system" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/LwIP/src/include/ipv4" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/FreeRTOS/Source/include" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/FreeRTOS/Source/portable/GCC/ARM_CM7/r0p1" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/FreeRTOS/Source/CMSIS_RTOS" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/LwIP/src/include" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/inc/mxconstants.h" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/STM32746G-Discovery" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Fonts" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/st7735" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/Common" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/wm8994" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/exc7200" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/ov9655" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/mfxstm32l152" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/s5k5cag" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/rk043fn48h" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/n25q128a" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/ampire640480" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/stmpe811" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/ampire480272" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/ft5336" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/n25q512a" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Components/ts3510" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/Utilities/Log" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/HAL_Driver/Inc" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/HAL_Driver/Inc/Legacy" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/CMSIS/device" -I"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/CMSIS/core" -include"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/inc/mxconstants.h" -O0 -g3 -Wall -fmessage-length=0 -ffunction-sections -c -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


