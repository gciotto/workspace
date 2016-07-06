################################################################################
# Automatically-generated file. Do not edit!
################################################################################

-include ../makefile.init

RM := rm -rf

# All of the sources participating in the build are defined here
-include sources.mk
-include startup/subdir.mk
-include src/subdir.mk
-include Utilities/STM32746G-Discovery/subdir.mk
-include Utilities/Log/subdir.mk
-include Utilities/Components/wm8994/subdir.mk
-include Utilities/Components/ts3510/subdir.mk
-include Utilities/Components/stmpe811/subdir.mk
-include Utilities/Components/st7735/subdir.mk
-include Utilities/Components/s5k5cag/subdir.mk
-include Utilities/Components/ov9655/subdir.mk
-include Utilities/Components/mfxstm32l152/subdir.mk
-include Utilities/Components/ft5336/subdir.mk
-include Utilities/Components/exc7200/subdir.mk
-include LwIP/system/OS/subdir.mk
-include LwIP/src/netif/ppp/subdir.mk
-include LwIP/src/netif/subdir.mk
-include LwIP/src/core/snmp/subdir.mk
-include LwIP/src/core/ipv4/subdir.mk
-include LwIP/src/core/subdir.mk
-include LwIP/src/api/subdir.mk
-include HAL_Driver/Src/subdir.mk
-include FreeRTOS/Source/portable/MemMang/subdir.mk
-include FreeRTOS/Source/portable/GCC/ARM_CM7/r0p1/subdir.mk
-include FreeRTOS/Source/CMSIS_RTOS/subdir.mk
-include FreeRTOS/Source/subdir.mk
-include subdir.mk
-include objects.mk

ifneq ($(MAKECMDGOALS),clean)
ifneq ($(strip $(S_UPPER_DEPS)),)
-include $(S_UPPER_DEPS)
endif
ifneq ($(strip $(C_DEPS)),)
-include $(C_DEPS)
endif
endif

-include ../makefile.defs

# Add inputs and outputs from these tool invocations to the build variables 

# All Target
all: CNPEM\ -\ PROSAC\ Touch.elf

# Tool invocations
CNPEM\ -\ PROSAC\ Touch.elf: $(OBJS) $(USER_OBJS)
	@echo 'Building target: $@'
	@echo 'Invoking: MCU GCC Linker'
	arm-none-eabi-gcc -mcpu=cortex-m7 -mthumb -mfloat-abi=hard -mfpu=fpv5-sp-d16 -T"/home/gciotto/workspace/workspace/CNPEM - PROSAC Touch/LinkerScript.ld" -Wl,-Map=output.map -Wl,--gc-sections -lm -o "CNPEM - PROSAC Touch.elf" @"objects.list" $(USER_OBJS) $(LIBS)
	@echo 'Finished building target: $@'
	@echo ' '
	$(MAKE) --no-print-directory post-build

# Other Targets
clean:
	-$(RM) *
	-@echo ' '

post-build:
	-@echo 'Generating binary and Printing size information:'
	arm-none-eabi-objcopy -O binary "CNPEM - PROSAC Touch.elf" "CNPEM - PROSAC Touch.bin"
	arm-none-eabi-size "CNPEM - PROSAC Touch.elf"
	-@echo ' '

.PHONY: all clean dependents
.SECONDARY: post-build

-include ../makefile.targets
