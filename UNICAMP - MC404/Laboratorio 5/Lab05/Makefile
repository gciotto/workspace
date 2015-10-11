all: lab5

lab5: lab5.o dummyos.o
	arm-elf-ld -Ttext=0x0 dummyos.o lab5.o -o lab5

%.o : %.s
	arm-elf-as -g $< -o $*.o

dump: lab5
	arm-elf-objdump -S lab5

readelf: lab5
	arm-elf-readelf -a lab5

gdbtarget: lab5
	arm-sim --load=lab5 -debug-core -enable-gdb

gdbhost: lab5
	armv5e-elf-gdb lab5

clean: # Limpa arquivos 
	-rm lab5 lab5.o
