#include "cimp.h"

t_cmd * cmd_alloc() {
	t_cmd * cmd;

	if ((cmd = malloc(sizeof(t_cmd))) == NULL) {
		return (NULL);
	}
	cmd->cmd     = NULL;
	cmd->name    = NULL;
	cmd->num     = 0;
	cmd->focus   = -1;
	cmd->rect.x  = -1;
	cmd->rect.y  = -1;
	cmd->rect.w  = -1;
	cmd->rect.h  = -1;
	cmd->point.x = -1;
	cmd->point.y = -1;
	cmd->color.r = 0;
	cmd->color.g = 0;
	cmd->color.b = 0;
	cmd->color.a = 0;
	memset(cmd->color2, 0, sizeof(cmd->color2));
	return (cmd);
}

void cmd_free(t_cmd * cmd) {
	if (cmd->cmd)
		free(cmd->cmd);
	if (cmd->name)
		free(cmd->name);
	free(cmd);
}
