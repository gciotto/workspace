################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../src/bsmp.c \
../src/client.c \
../src/server.c \
../src/server_priv.c \
../src/test_application.c 

OBJS += \
./src/bsmp.o \
./src/client.o \
./src/server.o \
./src/server_priv.o \
./src/test_application.o 

C_DEPS += \
./src/bsmp.d \
./src/client.d \
./src/server.d \
./src/server_priv.d \
./src/test_application.d 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	gcc -lm -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


