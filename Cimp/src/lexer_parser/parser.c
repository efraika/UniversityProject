#include "cimp.h"

// flex
extern t_li_token * scan_string(const char * str);

static t_bool   run_parser_with_config(t_cmd * cmd, t_li_token * li_toks,
  const t_cmd_config * cmd_cf);
static const t_cmd_config * get_conf_cmd(char * cmd);
static char * eat_cmd(t_li_token * li_token);

t_cmd * parser(char * line) {
	t_li_token * li_toks;
	t_cmd * res;
	t_bool ok;

	res     = NULL;
	ok      = FALSE;
	li_toks = scan_string(line);
	if (li_toks) {
		// li_token_print(li_toks);

		if (li_token_empty(li_toks) == FALSE && li_toks->ok == TRUE) {
			res      = cmd_alloc();
			res->cmd = eat_cmd(li_toks);
			if (res->cmd == NULL) {
				printf("La commande entrée n'est pas reconnue...\n");
				ok = FALSE;
			}
			else {
				ok = run_parser_with_config(res, li_toks, get_conf_cmd(res->cmd));
			}
		}

		li_token_free(li_toks);
	}
	if (ok == FALSE && res != NULL) {
		cmd_free(res);
		res = NULL;
	}
	return (res);
}

#define	HANDLE_PARAM(name_parm, arg_opt, eat_cmd) \
	if (cmd_cf->opts & (arg_opt) && (opts & (arg_opt)) == 0) { \
		printf("Have already a " name_parm "parameter\n"); \
		return (FALSE); \
	} \
	else if (opts & (arg_opt)) { \
		eat_cmd; \
		opts &= ~(arg_opt); \
	} \
	else if (opts_opt & (arg_opt)) { \
		eat_cmd; \
		opts_opt &= ~(arg_opt); \
	} \
	else { \
		printf("No need a " name_parm " for this command\n"); \
		return (FALSE); \
	}


static t_bool   run_parser_with_config(t_cmd * cmd, t_li_token * li_toks,
  const t_cmd_config * cmd_cf) {
	uint8_t opts;
	uint8_t opts_opt;
	t_token * tok;

	if (cmd_cf == NULL)
		return (FALSE);

	opts     = cmd_cf->opts;
	opts_opt = cmd_cf->opts_opt;
	tok      = NULL;
	while (li_token_empty(li_toks) == FALSE) {
		tok = li_token_get_next(li_toks);
		switch (tok->type) {
			case WORD:
				HANDLE_PARAM("name", (ARG_NAME | ARG_PATH), cmd->name = dupstr(tok->u.str))
				break;
			case PATH:
				HANDLE_PARAM("path", (ARG_PATH), cmd->name = dupstr(tok->u.str))
				break;
			case NUM:
				HANDLE_PARAM("num", (ARG_NUM), cmd->num = tok->u.num)
				break;
			case FOCUS:
				HANDLE_PARAM("focus", (ARG_FOCUS), cmd->focus = tok->u.num)
				break;
			case RECT:
				HANDLE_PARAM("rect", (ARG_RECT), cmd->rect = tok->u.rect)
				break;
			case POINT:
				HANDLE_PARAM("point", (ARG_PT), cmd->point = tok->u.point)
				break;
			case COLOR:
				HANDLE_PARAM("color", (ARG_COLOR), cmd->color = tok->u.color)
				break;
			case COLOR2:
				HANDLE_PARAM("color2", (ARG_COLOR2),
				  memcpy(cmd->color2, tok->u.color2, sizeof(tok->u.color2)))
				break;
		}
	}
	if (opts != 0) {
		printf("Some arguments are missing\n");
		return (FALSE);
	}
	// Some busy stuff to assimilate things
	return (TRUE);
} /* run_parser_with_config */

static const t_cmd_config * get_conf_cmd(char * cmd) {
	for (int i = 0; i < (int) g_command_list_size; i++) {
		if (strcmp(cmd, g_command_list[i].name) == 0) {
			return g_command_list + i;
		}
	}
	return NULL;
}

static char * eat_cmd(t_li_token * li_token) {
	t_token * first;

	first = li_token_get_next(li_token);
	if (first->type != WORD)
		return (NULL);

	return (dupstr(first->u.str));
}
