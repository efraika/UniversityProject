#include "cimp.h"

t_rc_cmd cimp_sym_verti(t_cmd * cmd) {
	(void) cmd;
	SDL_Surface * buff_screen = g_cimp->screen[cmd->focus]->buff_screen;
	if (SDL_MUSTLOCK(buff_screen))
		SDL_LockSurface(buff_screen);

	uint32_t * pixels = (uint32_t *) buff_screen->pixels;
	uint32_t tmp;
	for (int x = 0; x < buff_screen->w / 2; x++) {
		for (int y = 0; y < buff_screen->h; y++) {
			int A = (buff_screen->w * y) + x;
			int B = (buff_screen->w * y) + buff_screen->w - 1 - x;
			tmp       = pixels[A];
			pixels[A] = pixels[B];
			pixels[B] = tmp;
		}
	}

	if (SDL_MUSTLOCK(buff_screen))
		SDL_UnlockSurface(buff_screen);
	return (OK);
}

t_rc_cmd cimp_sym_hori(t_cmd * cmd) {
	(void) cmd;
	SDL_Surface * buff_screen = g_cimp->screen[cmd->focus]->buff_screen;
	if (SDL_MUSTLOCK(buff_screen))
		SDL_LockSurface(buff_screen);

	uint32_t * pixels = (uint32_t *) buff_screen->pixels;
	uint32_t tmp;
	for (int x = 0; x < buff_screen->w; x++) {
		for (int y = 0; y < buff_screen->h / 2; y++) {
			int A = x + y * buff_screen->w;
			int B = x + (buff_screen->h - 1 - y) * buff_screen->w;
			tmp       = pixels[A];
			pixels[A] = pixels[B];
			pixels[B] = tmp;
		}
	}
	if (SDL_MUSTLOCK(buff_screen))
		SDL_UnlockSurface(buff_screen);
	return (OK);
}
