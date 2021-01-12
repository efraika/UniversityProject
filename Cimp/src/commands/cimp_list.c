#include "cimp.h"

t_rc_cmd cimp_list(t_cmd * cmd) {
	(void) *cmd;

	int max_name = 0, tmp_name;
	int max_ID = 0, tmp_ID;
	int max_taille = 0, tmp_taille, w, h;
	int max_path = 0, tmp_path;
	int space_default = 5;
	char * name;

	for (int i = 0; i < NB_SCREENS; i++) {
		if (g_cimp->screen[i]) {
			name = basename(g_cimp->screen[i]->path);

			w = g_cimp->screen[i]->buff_screen->w;
			h = g_cimp->screen[i]->buff_screen->h;
			int w_len = nb_digit(w);
			int h_len = nb_digit(h);

			tmp_name   = strlen(name) + space_default;
			tmp_ID     = strlen("ID") + space_default;
			tmp_taille = w_len + h_len + 1 + space_default;
			tmp_path   = strlen(g_cimp->screen[i]->path);

			if (tmp_taille > max_taille)
				max_taille = tmp_taille;
			if (tmp_name > max_name)
				max_name = tmp_name;
			if (tmp_ID > max_ID)
				max_ID = tmp_ID;
			if (tmp_path > max_path)
				max_path = tmp_path;
		}
	}

	printf("%-*s %-*s %-*s %-*s\n", max_name, "NAME", max_ID, "ID", max_taille, "TAILLE",
	  max_path, "FILEPATH");

	for (int i = 0; i < NB_SCREENS; i++) {
		if (g_cimp->screen[i]) {
			name = basename(g_cimp->screen[i]->path);

			w = g_cimp->screen[i]->buff_screen->w;
			h = g_cimp->screen[i]->buff_screen->h;
			int w_len = nb_digit(w);
			int h_len = nb_digit(h);
			char * width;
			char * height;
			char * taille = malloc(sizeof(char) * (w_len + h_len + 2));
			width  = itoa(w);
			height = itoa(h);

			if (width == NULL || height == NULL || taille == NULL) {
				if (taille)
					free(taille);
				if (width)
					free(width);
				if (height)
					free(height);
				return (1);
			}

			strncpy(taille, width, w_len + 1);
			strncpy(taille + w_len, "x", 2);
			strncpy(taille + w_len + 1, height, h_len + 1);

			printf("%-*s %-*d %-*s %-*s\n", max_name, name, max_ID, i + 1, max_taille, taille,
			  max_path,
			  g_cimp->screen[i]->path);

			free(taille);
			free(width);
			free(height);
		}
	}
	return (OK);
} /* cimp_list */
