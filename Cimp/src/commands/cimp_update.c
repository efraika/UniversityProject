#include "cimp.h"

t_rc_cmd cimp_update(t_cmd * cmd) {
	char * name;
	int ret;

	if (cmd->name)
		name = cmd->name;
	else
		name = g_cimp->screen[cmd->focus]->path;
	ret = (cimp_screen_set_surface(g_cimp->screen[cmd->focus], name)) == FALSE;
	if (ret == 0 && cmd->name != NULL)
		ret = (cimp_screen_set_path(g_cimp->screen[cmd->focus], name) == FALSE);
	return (ret);

	return (OK);
}
