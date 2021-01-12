#include "cimp.h"

void treat_line(char * line) {
	t_cmd * cmd;

	cmd = parser(line);
	if (cmd != NULL) {
		if (cmd->focus != -1) {
			cmd->focus--;
			if (cmd->focus < 0 || cmd->focus >= NB_SCREENS) {
				printf("Ce focus est inexistant\n");
				cmd_free(cmd);
				return;
			}
			if (g_cimp->screen[cmd->focus] == NULL) {
				cmd->focus = -1;
			}
		}
		else {
			if (g_cimp->select != NULL)
				cmd->focus = g_cimp->select->id;
			else
				cmd->focus = g_cimp->focus;
		}
		switch (cimp_exe(cmd)) {
			case OK:
				break;
			case FAIL:
				printf("Fail command...\n");
				break;
			case ABORT:
				printf("Internal program error !!!\n");
				printf("Please contact maintainers !!!\n");
				break;
		}
		cmd_free(cmd);
	}
} /* treat_line */

void handle_line() {
	char * line = NULL;

	line = child_getline();
	if (line != NULL) {
		treat_line(line);
		free(line);
		line = NULL;

		if (g_cimp->running) {
			child_send_ok();
		}
	}
}

void core() {
	while (g_cimp->running) {
		handle_line();
		for (int i = 0; i < NB_SCREENS; i++) {
			if (g_cimp->screen[i])
				cimp_screen_update(g_cimp->screen[i], i);
		}
		// Update events (if any)
		update_event(g_cimp->event);
		usleep(TIMEOUT_USLEEP * 1000);
	}
}
