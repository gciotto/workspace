#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#define ORG 	0
#define ALIGN 	1
#define WFILL	2
#define WORD	3
#define	SET	4

#define M_INSTRUCTION	100
#define NO_M_INSTRUCTION 101
#define LR_INSTRUCTION 102

typedef struct struct_label  {
      char* 	name;
      int 	position;
} label;

typedef struct _set {
      char*	name;
      int	mem;
} set;

typedef struct _label_table {
      label labels[20];
      int count;
} label_table;

typedef struct _label_set {
      set constraint[20];
      int count;
} set_table;

label_table build_labels(char *filename, set_table* constraints);
int get_directive(char* name);
void print_file(char* fileFrom, char* fileTo, label_table t, set_table s);
int get_instruction(char* name);
int get_instruction_opcode(char* name, int right) ;
void free_tables(set_table s, label_table l);
void mount(char* fileFrom, char* fileTo);
void print_arrays (set_table s_table, label_table l_table);