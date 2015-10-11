#### Gustavo Ciotto Pinton RA 117136 MC404 Turma B
############################# Makefile ##########################
## Compilação - Linguagem de Montagem - Arquivo 1 ##
arq1.s: arq1.c
	gcc -S arq1.c -o arq1.s
## Compilação - Linguagem de Montagem - Arquivo 2 ##
arq2.s: arq2.c
	gcc -S arq2.c -o arq2.s
## Linguagem de Maquina - Arquivo 1 ##
arq1.o: arq1.s
	as arq1.s -o arq1.o
## Linguagem de Maquina - Arquivo 2 ##
arq2.o: arq2.s
	as arq2.s -o arq2.o
## Link ##
saida: arq1.o arq2.o
	ld -dynamic-linker /lib/ld-linux.so.2 /usr/lib/crt1.o /usr/lib/crti.o -L/usr/lib arq1.o arq2.o -lc /usr/lib/crtn.o -o saida
## Limpar arquivos intermediarios
clean:
	rm -rf *.o *.s
## Executar aplicacao
run: saida
	./saida

##### Para compilar os arquivos 1 e 2, chamamos na linha de comando 'make saida' #####
