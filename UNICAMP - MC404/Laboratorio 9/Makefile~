myos.o: myos.s
	arm-elf-as -g myos.s -o myos.o
myos: myos.o
	arm-elf-ld -g -Ttext=0x0 myos.o -o myos
# User
user.o: user.s
	arm-elf-as -g user.s -o user.o
syscalls.o: syscalls.s
	arm-elf-as -g syscalls.s -o syscalls.o
user: user.o syscalls.o
	arm-elf-ld -g user.o syscalls.o -o user