int main(void) {

	char message[] = "Userland code.\n";

	write(1,message,15);

	exit(0);

}