/* ###################################################################
**     THIS COMPONENT MODULE IS GENERATED BY THE TOOL. DO NOT MODIFY IT.
**     Filename    : WAIT3.h
**     Project     : ea076-exp3
**     Processor   : MKL25Z128VLK4
**     Component   : Wait
**     Version     : Component 01.069, Driver 01.00, CPU db: 3.00.000
**     Compiler    : GNU C Compiler
**     Date/Time   : 2016-06-07, 18:03, # CodeGen: 101
**     Abstract    :
**          Implements busy waiting routines.
**     Settings    :
**          Component name                                 : WAIT3
**          Manual Clock Values                            : Disabled
**          Delay100usFunction                             : Delay100US
**          RTOS                                           : Disabled
**          Watchdog                                       : Disabled
**     Contents    :
**         Wait10Cycles   - void WAIT3_Wait10Cycles(void);
**         Wait100Cycles  - void WAIT3_Wait100Cycles(void);
**         WaitCycles     - void WAIT3_WaitCycles(uint16_t cycles);
**         WaitLongCycles - void WAIT3_WaitLongCycles(uint32_t cycles);
**         Waitms         - void WAIT3_Waitms(uint16_t ms);
**         Waitus         - void WAIT3_Waitus(uint16_t us);
**         Waitns         - void WAIT3_Waitns(uint16_t ns);
**         WaitOSms       - void WAIT3_WaitOSms(void);
**
**     License   : Open Source (LGPL)
**     Copyright : Erich Styger, 2013-2016, all rights reserved.
**     Web       : www.mcuoneclipse.com
**     This an open source software implementing waiting routines using Processor Expert.
**     This is a free software and is opened for education,  research  and commercial developments under license policy of following terms:
**     * This is a free software and there is NO WARRANTY.
**     * No restriction on use. You can use, modify and redistribute it for personal, non-profit or commercial product UNDER YOUR RESPONSIBILITY.
**     * Redistributions of source code must retain the above copyright notice.
** ###################################################################*/
/*!
** @file WAIT3.h
** @version 01.00
** @brief
**          Implements busy waiting routines.
*/         
/*!
**  @addtogroup WAIT3_module WAIT3 module documentation
**  @{
*/         

#ifndef __WAIT3_H
#define __WAIT3_H

/* MODULE WAIT3. */

/* Include inherited beans */
#include "KSDK1.h"

#if KSDK1_SDK_VERSION_USED == KSDK1_KSDK1_SDK_VERSION_NONE
/* Include shared modules, which are used for whole project */
  #include "PE_Types.h"
  #include "PE_Error.h"
  #include "PE_Const.h"
  #include "IO_Map.h"
  #include "Cpu.h"
#endif

#ifdef __cplusplus
extern "C" {
#endif


#if KSDK1_SDK_VERSION_USED != KSDK1_KSDK1_SDK_VERSION_NONE
  extern uint32_t SystemCoreClock; /* clock frequency variable defined system_<device>.h of the SDK */
  #define WAIT3_INSTR_CLOCK_HZ       SystemCoreClock  /* core clock frequency in Hz */
#else
  #define WAIT3_INSTR_CLOCK_HZ       CPU_CORE_CLK_HZ /* for Kinetis, use core clock as base for instruction execution */
#endif
#define WAIT3_NofCyclesMs(ms, hz)  ((ms)*((hz)/1000)) /* calculates the needed cycles based on bus clock frequency */
#define WAIT3_NofCyclesUs(us, hz)  ((us)*(((hz)/1000)/1000)) /* calculates the needed cycles based on bus clock frequency */
#define WAIT3_NofCyclesNs(ns, hz)  (((ns)*(((hz)/1000)/1000))/1000) /* calculates the needed cycles based on bus clock frequency */


#define WAIT3_WAIT_C(cycles) \
     ( (cycles)<=10 ? \
          WAIT3_Wait10Cycles() \
        : WAIT3_WaitCycles((uint16_t)cycles) \
      )                                      /*!< wait for some cycles */


void WAIT3_Wait10Cycles(void);
/*
** ===================================================================
**     Method      :  WAIT3_Wait10Cycles (component Wait)
**     Description :
**         Wait for 10 CPU cycles.
**     Parameters  : None
**     Returns     : Nothing
** ===================================================================
*/

void WAIT3_Wait100Cycles(void);
/*
** ===================================================================
**     Method      :  WAIT3_Wait100Cycles (component Wait)
**     Description :
**         Wait for 100 CPU cycles.
**     Parameters  : None
**     Returns     : Nothing
** ===================================================================
*/

void WAIT3_WaitCycles(uint16_t cycles);
/*
** ===================================================================
**     Method      :  WAIT3_WaitCycles (component Wait)
**     Description :
**         Wait for a specified number of CPU cycles (16bit data type).
**     Parameters  :
**         NAME            - DESCRIPTION
**         cycles          - The number of cycles to wait.
**     Returns     : Nothing
** ===================================================================
*/

void WAIT3_Waitms(uint16_t ms);
/*
** ===================================================================
**     Method      :  WAIT3_Waitms (component Wait)
**     Description :
**         Wait for a specified time in milliseconds.
**     Parameters  :
**         NAME            - DESCRIPTION
**         ms              - How many milliseconds the function has to
**                           wait
**     Returns     : Nothing
** ===================================================================
*/

/* we are having a static clock configuration: implement as macro/inlined version */
#define WAIT3_Waitus(us)  \
        /*lint -save -e(505,506,522) Constant value Boolean, Redundant left argument to comma. */\
       (  ((WAIT3_NofCyclesUs((us),WAIT3_INSTR_CLOCK_HZ)==0)||(us)==0) ? \
          (void)0 : \
          ( ((us)/1000)==0 ? (void)0 : WAIT3_Waitms((uint16_t)((us)/1000))) \
          , (WAIT3_NofCyclesUs(((us)%1000), WAIT3_INSTR_CLOCK_HZ)==0) ? (void)0 : \
            WAIT3_WAIT_C(WAIT3_NofCyclesUs(((us)%1000), WAIT3_INSTR_CLOCK_HZ)) \
       /*lint -restore */\
       )
/*
** ===================================================================
**     Method      :  WAIT3_Waitus (component Wait)
**     Description :
**         Wait for a specified time in microseconds.
**     Parameters  :
**         NAME            - DESCRIPTION
**         us              - How many microseconds the function has to
**                           wait
**     Returns     : Nothing
** ===================================================================
*/

/* we are having a static clock configuration: implement as macro/inlined version */
#define WAIT3_Waitns(ns)  \
        /*lint -save -e(505,506,522) Constant value Boolean, Redundant left argument to comma. */\
       (  ((WAIT3_NofCyclesNs((ns), WAIT3_INSTR_CLOCK_HZ)==0)||(ns)==0) ? \
          (void)0 : \
          WAIT3_Waitus((ns)/1000) \
          , (WAIT3_NofCyclesNs((ns)%1000, WAIT3_INSTR_CLOCK_HZ)==0) ? \
              (void)0 : \
              WAIT3_WAIT_C(WAIT3_NofCyclesNs(((ns)%1000), WAIT3_INSTR_CLOCK_HZ)) \
       /*lint -restore */\
       )
/*
** ===================================================================
**     Method      :  WAIT3_Waitns (component Wait)
**     Description :
**         Wait for a specified time in nano seconds.
**     Parameters  :
**         NAME            - DESCRIPTION
**         ns              - How many ns the function has to wait
**     Returns     : Nothing
** ===================================================================
*/

#define WAIT3_WaitOSms(ms) \
  WAIT3_Waitms(ms) /* no RTOS used, so use normal wait */
/*
** ===================================================================
**     Method      :  WAIT3_WaitOSms (component Wait)
**     Description :
**         If an RTOS is enabled, this routine will use a non-blocking
**         wait method. Otherwise it will do a busy/blocking wait.
**     Parameters  : None
**     Returns     : Nothing
** ===================================================================
*/

#ifdef __cplusplus
}  /* extern "C" */
#endif

void WAIT3_WaitLongCycles(uint32_t cycles);
/*
** ===================================================================
**     Method      :  WAIT3_WaitLongCycles (component Wait)
**     Description :
**         Wait for a specified number of CPU cycles (32bit data type).
**     Parameters  :
**         NAME            - DESCRIPTION
**         cycles          - The number of cycles to wait.
**     Returns     : Nothing
** ===================================================================
*/

/* END WAIT3. */

#endif
/* ifndef __WAIT3_H */
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