#include "cimp.h"

static void print_command_config(int max_name, const t_cmd_config * cmd_cf) {
	printf("%-*s", max_name, cmd_cf->name);
	for (int arg = 1; arg < (1 << NB_ARG_TYPE); arg = arg << 1) {
		if (cmd_cf->opts & arg) {
			printf(" [%s]", arg_type_to_string(arg));
		}
	}
	for (int arg = 1; arg < (1 << NB_ARG_TYPE); arg = arg << 1) {
		if (cmd_cf->opts_opt & arg) {
			printf(" [? %s]", arg_type_to_string(arg));
		}
	}
	printf("\n");
}

t_rc_cmd cimp_help(t_cmd * cmd) {
	int max_name;

	(void) cmd;
	max_name = 0;
	for (size_t i = 0; i < g_command_list_size; i++) {
		int tmp = strlen(g_command_list[i].name);
		if (tmp > max_name)
			max_name = tmp;
	}
	max_name++;
	for (size_t i = 0; i < g_command_list_size; i++) {
		print_command_config(max_name, g_command_list + i);
	}
	return (OK);
}
