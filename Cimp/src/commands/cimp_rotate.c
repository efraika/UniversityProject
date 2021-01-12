#include "cimp.h"

// Local to cimp_rotate.c
static t_rc_cmd real_rotate(t_cmd * cmd, int angle);
static t_rc_cmd real_rotate90();

t_rc_cmd cimp_rotate(t_cmd * cmd) {
	return (real_rotate(cmd, cmd->num));

	return (OK);
}

static t_rc_cmd real_rotate(t_cmd * cmd, int angle) {
	t_rc_cmd rc;

	while (angle < 0)
		angle += 360;
	switch (angle % 360) {
		case 0:
			break;
		case 90:
			return (real_rotate90(cmd->focus));

			break;
		case 180:
			rc = cimp_sym_hori(cmd);
			if (rc != OK)
				return (rc);

			return (cimp_sym_verti(cmd));

			break;
		case 270:
			rc = real_rotate(cmd, 180);
			if (rc != OK)
				return (rc);

			return (real_rotate(cmd, 90));

			break;
		default:
			printf("Nothing to do for now, invalid angle value, choose a multiple of 90°\n");
	}
	return (OK);
} /* real_rotate */

static t_rc_cmd real_rotate90(int focus) {
	int new_width  = g_cimp->screen[focus]->buff_screen->h;
	int new_height = g_cimp->screen[focus]->buff_screen->w;

	SDL_Surface * surf = SDL_CreateRGBSurface(0, new_width, new_height, 32, 0, 0, 0, 0);

	if (surf == NULL)
		return (ABORT);

	uint32_t * old = (uint32_t *) g_cimp->screen[focus]->buff_screen->pixels;
	uint32_t * new = (uint32_t *) surf->pixels;

	for (int i = 0; i < new_height; i++) {
		for (int j = 0; j < new_width; j++) {
			new[new_width - 1 - j + i * new_width] = old[i + j * new_height];
		}
	}
	SDL_FreeSurface(g_cimp->screen[focus]->buff_screen);
	g_cimp->screen[focus]->buff_screen = surf;
	return (OK);
}
