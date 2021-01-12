#include "cimp.h"

t_rc_cmd cimp_save(t_cmd * cmd) {
	t_type_img timg;
	t_export_img_func_ptr fptr;
	char * namefile;
	int ret;

	if (cmd->name == NULL)
		namefile = g_cimp->screen[cmd->focus]->path;
	else
		namefile = cmd->name;

	if ((timg = get_type_img(namefile)) == NO_SUPPORTED ||
	  (fptr = get_func_img(timg)) == NULL)
		return (ABORT);

	ret = fptr(namefile, g_cimp->screen[cmd->focus]->buff_screen);
	if (ret == 0 && cmd->name != NULL)
		ret = (cimp_screen_set_path(g_cimp->screen[cmd->focus], namefile) == FALSE);
	return (ret);

	return (OK);
}
