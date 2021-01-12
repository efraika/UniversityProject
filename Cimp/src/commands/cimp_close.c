#include "cimp.h"

t_rc_cmd cimp_close(t_cmd * cmd) {
	int focus;

	if (cmd)
		focus = cmd->focus;
	else
		focus = g_cimp->focus;
	if (g_cimp->select != NULL && g_cimp->select->id == focus)
		cimp_unselect(NULL);
	cimp_screen_end(g_cimp->screen[focus]);
	g_cimp->screen[focus] = NULL;
	if (g_cimp->focus == focus) {
		g_cimp->focus = get_next_focus(g_cimp->focus);
	}

	return OK;
}
