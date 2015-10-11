#include <stdio.h>

void funcao() {
  printf("Estou no arquivo 2!\n");
}
//ld -dynamic-linker /lib/ld-linux.so.2 /usr/lib/crt1.o /usr/lib/crti.o -L/usr/lib arq1.o arq2.o -lc /usr/lib/crtn.o -o saida
