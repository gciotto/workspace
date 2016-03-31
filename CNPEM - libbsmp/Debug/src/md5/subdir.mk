################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../src/md5/md5.c 

OBJS += \
./src/md5/md5.o 

C_DEPS += \
./src/md5/md5.d 


# Each subdirectory must supply rules for building sources it contributes
src/md5/%.o: ../src/md5/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	gcc -lm -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


