#include "cimp.h"

t_rc_cmd cimp_open(t_cmd * cmd) {
	int rc;
	t_bool must_abort;

	rc = get_available_id();
	if (rc == -1) {
		printf("%s\n", TOO_MUCH_SCREENS);
	}
	else if (cmd != NULL) {
		g_cimp->screen[rc] = cimp_screen_init(cmd->name, &must_abort);
		if (g_cimp->screen[rc] == NULL) {
			return (must_abort ? ABORT : FAIL);
		}

		g_cimp->focus = rc;
		return (OK);
	}
	return (FAIL);
}
