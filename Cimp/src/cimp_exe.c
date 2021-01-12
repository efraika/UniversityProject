#include "cimp.h"

int cimp_exe(t_cmd * cmd) {
	for (size_t i = 0; i < g_command_list_size; ++i) {
		if (strcmp(cmd->cmd, g_command_list[i].name) == 0) {
			if (g_command_list[i].func_cmd_ptr == NULL) { // QUIT
				g_cimp->running = 0;
				return (0);
			}
			if ((g_command_list[i].opts_opt & ARG_FOCUS || g_command_list[i].opts & ARG_FOCUS) &&
			  cmd->focus == -1)
			{
				printf("Erreur : aucun focus specifie ou fenetre inexistante\n");
				return (-1);
			}
			return (g_command_list[i].func_cmd_ptr(cmd));
		}
	}
	return (-1);
}
