#include "cimp.h"

void        child_send_ok() {
	int tmp = 0;

	write(g_fd_callback, &tmp, sizeof(tmp));
}

void        child_stop() {
	int tmp = 1;

	write(g_fd_callback, &tmp, sizeof(tmp));
	close(g_fd_readline);
	close(g_fd_callback);
}

char * child_getline() {
	char * line = NULL;
	int len     = 0;

	if (read(g_fd_readline, &len, sizeof(len)) == -1)
		return (NULL);

	if (len == -1) {
		g_cimp->running = 0;
		return (NULL);
	}

	line = (char *) malloc(sizeof(char) * (len + 1));
	if (line == NULL)
		return (NULL);

	memset(line, 0, len + 1);
	read(g_fd_readline, line, len);
	return (line);
}
