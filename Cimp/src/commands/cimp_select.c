#include "cimp.h"


/**Une fonction qui cree la zone de selection associee au rectangle rect.
* Si une selectionexiste deja on met a jour les donnees du rectangle  **/
t_rc_cmd cimp_select(t_cmd * cmd) {
	if (g_cimp->select != NULL) {
		g_cimp->select->surface = cmd->rect;
		return OK;
	}

	t_cimp_select * selection = cimp_init_select(cmd->rect, cmd->focus);

	if (selection == NULL) {
		return FAIL;
	}

	g_cimp->select = selection;

	return OK;
}

/** Une fonction aui libere la memoire associee a une selection,
 * mets a jour le champ correspondant dans g_cimp.**/
t_rc_cmd cimp_unselect(t_cmd * cmd) {
	(void) cmd;

	cimp_end_select(g_cimp->select);
	g_cimp->select = NULL;
	return OK;
}
