#include "cimp.h"

int main(void) {
	if (setup_child()) {
		return (1);
	}
	if (cimp_init()) {
		printf("Something went terribly wrong\n");
		return (1);
	}
	core();
	child_stop();
	cimp_end();
	return (0);
} /* main */
