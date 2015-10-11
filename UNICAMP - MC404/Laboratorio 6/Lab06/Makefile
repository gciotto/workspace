arm-as := arm-elf-as
arm-ld := arm-elf-ld
asflags := -g
ldflags := -g

matrizes: matrizes.o main.o sys.o
	$(arm-ld) $(ldflags) -Ttext=0 sys.o main.o matrizes.o -o matrizes
matrizes.o: matrizes.s
	$(arm-as) $(asflags) matrizes.s -o matrizes.o
main.o: main.s
	$(arm-as) $(asflags) main.s -o main.o
clean:
	-rm matrizes.o main.o matrizes