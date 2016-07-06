/* ###################################################################
**     THIS COMPONENT MODULE IS GENERATED BY THE TOOL. DO NOT MODIFY IT.
**     Filename    : BitsIoLdd1.h
**     Project     : ea076-exp3
**     Processor   : MKL25Z128VLK4
**     Component   : BitsIO_LDD
**     Version     : Component 01.029, Driver 01.05, CPU db: 3.00.000
**     Compiler    : GNU C Compiler
**     Date/Time   : 2016-04-04, 15:48, # CodeGen: 0
**     Abstract    :
**         The HAL BitsIO component provides a low level API for unified
**         access to general purpose digital input/output 32 pins across
**         various device designs.
**
**         RTOS drivers using HAL BitsIO API are simpler and more
**         portable to various microprocessors.
**     Settings    :
**          Component name                                 : BitsIoLdd1
**          Port                                           : PTC
**          Pins                                           : 8
**            Pin0                                         : 
**              Pin                                        : ADC0_SE14/TSI0_CH13/PTC0/EXTRG_IN/CMP0_OUT
**              Pin signal                                 : 
**            Pin1                                         : 
**              Pin                                        : ADC0_SE15/TSI0_CH14/PTC1/LLWU_P6/RTC_CLKIN/I2C1_SCL/TPM0_CH0
**              Pin signal                                 : 
**            Pin2                                         : 
**              Pin                                        : ADC0_SE11/TSI0_CH15/PTC2/I2C1_SDA/TPM0_CH1
**              Pin signal                                 : 
**            Pin3                                         : 
**              Pin                                        : PTC3/LLWU_P7/UART1_RX/TPM0_CH2/CLKOUTa
**              Pin signal                                 : 
**            Pin4                                         : 
**              Pin                                        : PTC4/LLWU_P8/SPI0_PCS0/UART1_TX/TPM0_CH3
**              Pin signal                                 : 
**            Pin5                                         : 
**              Pin                                        : PTC5/LLWU_P9/SPI0_SCK/LPTMR0_ALT2/CMP0_OUT
**              Pin signal                                 : 
**            Pin6                                         : 
**              Pin                                        : CMP0_IN0/PTC6/LLWU_P10/SPI0_MOSI/EXTRG_IN/SPI0_MISO
**              Pin signal                                 : 
**            Pin7                                         : 
**              Pin                                        : CMP0_IN1/PTC7/SPI0_MISO/SPI0_MOSI
**              Pin signal                                 : 
**          Direction                                      : Output
**          Initialization                                 : 
**            Init. direction                              : Output
**            Init. value                                  : 0
**            Auto initialization                          : yes
**          Safe mode                                      : yes
**     Contents    :
**         Init   - LDD_TDeviceData* BitsIoLdd1_Init(LDD_TUserData *UserDataPtr);
**         PutVal - void BitsIoLdd1_PutVal(LDD_TDeviceData *DeviceDataPtr, uint32_t Val);
**
**     (c) 2012 by Freescale
** ###################################################################*/
/*!
** @file BitsIoLdd1.h
** @version 01.05
** @brief
**         The HAL BitsIO component provides a low level API for unified
**         access to general purpose digital input/output 32 pins across
**         various device designs.
**
**         RTOS drivers using HAL BitsIO API are simpler and more
**         portable to various microprocessors.
*/         
/*!
**  @addtogroup BitsIoLdd1_module BitsIoLdd1 module documentation
**  @{
*/         

#ifndef __BitsIoLdd1_H
#define __BitsIoLdd1_H

/* MODULE BitsIoLdd1. */

/* Include shared modules, which are used for whole project */
#include "PE_Types.h"
#include "PE_Error.h"
#include "PE_Const.h"
#include "IO_Map.h"
/* Include inherited beans */
#include "IO_Map.h"
#include "GPIO_PDD.h"

#include "Cpu.h"

#ifdef __cplusplus
extern "C" {
#endif 



/*! Peripheral base address of a device allocated by the component. This constant can be used directly in PDD macros. */
#define BitsIoLdd1_PRPH_BASE_ADDRESS  0x400FF080U
  
/*! Device data structure pointer used when auto initialization property is enabled. This constant can be passed as a first parameter to all component's methods. */
#define BitsIoLdd1_DeviceData  ((LDD_TDeviceData *)PE_LDD_GetDeviceStructure(PE_LDD_COMPONENT_BitsIoLdd1_ID))

/* Methods configuration constants - generated for all enabled component's methods */
#define BitsIoLdd1_Init_METHOD_ENABLED /*!< Init method of the component BitsIoLdd1 is enabled (generated) */
#define BitsIoLdd1_PutVal_METHOD_ENABLED /*!< PutVal method of the component BitsIoLdd1 is enabled (generated) */

/* Definition of implementation constants */
#define BitsIoLdd1_MODULE_BASE_ADDRESS FPTC_BASE_PTR /*!< Name of macro used as the base address */
#define BitsIoLdd1_PORTCONTROL_BASE_ADDRESS PORTC_BASE_PTR /*!< Name of macro used as the base address */
#define BitsIoLdd1_PORT_MASK 0xFFU     /*!< Mask of the allocated pin from the port */
#define BitsIoLdd1_PORT_VALID_VALUE_MASK 0xFF /*!< Mask of the allocated pins from the port as the first pin would be zero i.e. valid bits to be set in method PutVal */
#define BitsIoLdd1_PIN_ALLOC_0_MASK 0x01 /*!< Mask of the first allocated pin from the port */
#define BitsIoLdd1_PIN_ALLOC_0_INDEX 0U /*!< The index of the first allocated pin from the port */



/*
** ===================================================================
**     Method      :  BitsIoLdd1_Init (component BitsIO_LDD)
*/
/*!
**     @brief
**         This method initializes the associated peripheral(s) and the
**         component internal variables. The method is called
**         automatically as a part of the application initialization
**         code.
**     @param
**         UserDataPtr     - Pointer to the RTOS device
**                           structure. This pointer will be passed to
**                           all events as parameter.
**     @return
**                         - Pointer to the dynamically allocated private
**                           structure or NULL if there was an error.
*/
/* ===================================================================*/
LDD_TDeviceData* BitsIoLdd1_Init(LDD_TUserData *UserDataPtr);

/*
** ===================================================================
**     Method      :  BitsIoLdd1_PutVal (component BitsIO_LDD)
*/
/*!
**     @brief
**         Specified value is passed to the Input/Output component. If
**         the direction is [input] saves the value to a memory or a
**         register, this value will be written to the pins after
**         switching to the output mode - using [SetDir(TRUE)] (see
**         [Safe mode] property for limitations). If the direction is
**         [output] it writes the value to the pins. (Method is
**         available only if the Direction = _[output]_ or
**         _[input/output]_).
**     @param
**         DeviceDataPtr   - Device data structure
**                           pointer returned by [Init] method.
**     @param
**         Val             - Output value
*/
/* ===================================================================*/
void BitsIoLdd1_PutVal(LDD_TDeviceData *DeviceDataPtr, uint32_t Val);

/* END BitsIoLdd1. */

#ifdef __cplusplus
}  /* extern "C" */
#endif 

#endif
/* ifndef __BitsIoLdd1_H */
/*!
** @}
*/
/*
** ###################################################################
**
**     This file was created by Processor Expert 10.3 [05.09]
**     for the Freescale Kinetis series of microcontrollers.
**
** ###################################################################
*/