#include "cimp.h"

t_li_token * li_token_alloc() {
	t_li_token * li;

	if ((li = malloc(sizeof(t_li_token))) == NULL)
		return (NULL);

	memset(li, 0, sizeof(t_li_token));
	li->ok = TRUE;
	return (li);
}

static void         free_li_token_recu(t_token * tok) {
	if (tok != NULL) {
		free_li_token_recu(tok->next);
		if (tok->type == PATH || tok->type == WORD)
			free(tok->u.str);
		free(tok);
	}
}

void        li_token_free(t_li_token * li) {
	free_li_token_recu(li->first);
	free(li);
}

t_bool      li_token_empty(t_li_token * li) {
	return (li->actu == NULL);
}

void        li_token_print(t_li_token * li) {
	int id;
	t_token * tok;

	id  = 1;
	tok = li->first;
	while (tok) {
		printf("%d ", id);
		print_token(tok);
		tok = tok->next;
		id++;
	}
}

void        li_token_add(t_li_token * li, t_token * tok) {
	if (tok == NULL) {
		li->ok = FALSE;
		return;
	}
	if (li->first == NULL) {
		li->first = tok;
		li->last  = tok;
		li->actu  = tok;
	}
	else {
		li->last->next = tok;
		li->last       = tok;
	}
}

t_token * li_token_get_next(t_li_token * li) {
	t_token * actu;

	if (li->actu == NULL)
		return (NULL);

	actu     = li->actu;
	li->actu = li->actu->next;
	return (actu);
}
