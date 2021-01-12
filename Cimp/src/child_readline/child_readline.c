#include "cimp.h"

static int core_child_inner(int fd_write, int fd_callback) {
	char * line = NULL;
	int len     = 0;
	int ret     = 0;

	line = readline("cimp>>");
	len  = (line == NULL) ? -1 : (int) strlen(line);
	if (len != 0) {
		write(fd_write, &len, sizeof(len));
		if (len > 0)
			write(fd_write, line, len);
		read(fd_callback, &ret, sizeof(ret));
	}
	add_history(line);
	free(line);
	return (ret);
}

static void core_child(int fd_write, int fd_callback) {
	initialize_readline();
	while (core_child_inner(fd_write, fd_callback) == 0);
	exit(0);
}

int     setup_child() {
	int fds_rl[2];
	int fds_callback[2];
	pid_t pid_child;

	// Setting up connection with pipes
	if (pipe2(fds_rl, O_NONBLOCK) == -1) {
		printf("setup_child failed at pipe2");
		return (1);
	}
	if (pipe(fds_callback) == -1) {
		printf("setup_child failed at pipe2");
		close(fds_rl[0]);
		close(fds_rl[1]);
		return (1);
	}


	pid_child = fork();
	if (pid_child == -1) {
		printf("setup_child failed at fork");
		return (1);
	}

	// Child
	if (pid_child == 0) {
		close(fds_rl[0]);
		close(fds_callback[1]);

		core_child(fds_rl[1], fds_callback[0]);
		// Should never go to this line
		exit(1);
	}
	// Parent
	else {
		close(fds_rl[1]);
		close(fds_callback[0]);

		g_fd_readline = fds_rl[0];
		g_fd_callback = fds_callback[1];
	}
	return (0);
} /* setup_child */
