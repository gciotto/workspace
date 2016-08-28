/*
 * A new PINTOOL which calculates the routine with the biggest number of instructions
 * for every thread composing the a program.
 *
 * Author: Gustavo CIOTTO PINTON
 * Computer Architecture II MO601B
 */

#include <iostream>
#include <fstream>
#include <string.h>
#include "pin.H"

#define MAX_ROUTINES_PER_THREAD 50

/* Output file handler */
ofstream OutFile;

/* thread counter */
INT32 number_threads = 0;

/* KNOB handles input arguments. According to the reference page,
 * KNOB_MODE_WRITEONCE indicates that, in case of multiple appearances of the argument
 * on the command line, the program must consider just one of them (single value, single write) */
KNOB<string> KnobOutputFile(KNOB_MODE_WRITEONCE, "pintool",
    "o", "most-used.out", "specify output file name");

PIN_LOCK lock;

/* TLS storage for threads  */
static TLS_KEY tls_key;

class Routine_info {


public:
	Routine_info() : _ins_count (0), _next (NULL) {}
	UINT64 _ins_count
	string _name;
	Routine_info* _next;
};


class Thread_info {

public:
	Thread_info(): _routine_count (0) {}

	Routine_info* _routine_head;
	UINT32 _routine_count;
};



/* Function of the 'type' THREAD_START_CALLBACK(THREADID threadIndex, CONTEXT *ctxt, INT32 flags, VOID *v),
 * according to the API reference */
VOID initThreadData (THREADID threadIndex, CONTEXT *ctxt, INT32 flags, VOID *v) {

    GetLock(&lock, threadid+1);
    number_threads++;
    ReleaseLock(&lock);

    Thread_info* tdata = new Thread_info;

    PIN_SetThreadData(tls_key, tdata, threadid);
}

/* Function of the 'type' RTN_INSTRUMENT_CALLBACK(RTN rtn, VOID *v). */
VOID initRoutineCallback(RTN rtn, VOID *v) {

	THREADID _thread_id = PIN_ThreadId();

	Thread_info* _t_info =
	          static_cast<Thread_info*> (PIN_GetThreadData(tls_key, _thread_id));

	Routine_info* _new_routine = new Routine_info;

	strcpy(_new_routine->_name, RTN_Name(rtn));

    /* Adds new routine to the beginning of the linked list */
	_new_routine->_next = _t_info->_routine_head;
	_t_info->_routine_head = _new_routine;

    RTN_Open(rtn);

    // For each instruction of the routine
    for (INS ins = RTN_InsHead(rtn); INS_Valid(ins); ins = INS_Next(ins))
        // Insert a call to docount to increment the instruction counter for this rtn
        INS_InsertCall(ins, IPOINT_BEFORE, (AFUNPTR)incrementCount, IARG_PTR, &(_new_routine->_ins_count), IARG_END);

}

/* Callback function called when application is exiting */
VOID endTool(INT32 code, VOID *v)
{
    // Write to a file since cout and cerr maybe closed by the application
    OutFile << "Total number of threads = " << numThreads << endl;

    /*
     * TO DO: Generate output
     */

    OutFile.close();
}

/* Default help message */
INT32 Usage()
{
    PIN_ERROR( "This Pintool prints a trace of memory addresses\n"
              + KNOB_BASE::StringKnobSummary() + "\n");
    return -1;
}

/* Main function. It opens a file handler and adds an instrument function
 * after all instruction in program  */
int main(int argc, char *argv[])
{
    if (PIN_Init(argc, argv)) return Usage();

    PIN_InitSymbols()

    OutFile.open(KnobOutputFile.Value().c_str());

    /* Initialize the lock */
    InitLock(&lock);

    /* Obtain  a key for TLS storage. */
    tls_key = PIN_CreateThreadDataKey(0);

    PIN_AddThreadStartFunction(initThreadData, 0);

    RTN_AddInstrumentFunction(initRoutineCallback, 0);

    PIN_AddFiniFunction(endTool, 0);

    // Never returns
    PIN_StartProgram();

    return 0;
}

