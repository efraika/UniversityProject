#include "cimp.h"

/**Une fonction qui change le focus global du programme **/
t_rc_cmd cimp_focus(t_cmd * cmd) {
	if (g_cimp->focus == cmd->focus) {
		printf("Votre focus est actuellement sur la fenêtre %d\n", g_cimp->focus + 1);
		return (OK);
	}
	g_cimp->focus = cmd->focus;
	return (OK);
}
