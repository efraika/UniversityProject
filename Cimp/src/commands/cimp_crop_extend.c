#include "cimp.h"

static uint32_t set_color(uint32_t unused, void * color) {
	(void) unused;
	return *((uint32_t *) color);
}

t_rc_cmd cimp_crop_extend(t_cmd * cmd) {
	SDL_Rect rectangle   = cmd->rect;
	SDL_Surface * source = g_cimp->screen[cmd->focus]->buff_screen;

	uint32_t color = SDL_MapRGB(source->format, cmd->color.r, cmd->color.g, cmd->color.b);

	if (rectangle.x > source->w || rectangle.y > source->h) {
		printf("Le rectangle est en dehors de l'image, c'est donc impossible de l'aggrandir\n");
		return (FAIL);
	}
	if (rectangle.x < 0 || rectangle.y < 0 || rectangle.w < 0 || rectangle.h < 0) {
		printf("Des coordonnées negatives, quelle idee ?\n");
		return (FAIL);
	}
	if (rectangle.w - rectangle.x < source->w && rectangle.h - rectangle.y < source->h) {
		printf("Le rectangle est plus petit que l'image : pour rogner l'image utilisez la fonction crop_reduce\n");
		return (FAIL);
	}

	SDL_Surface * copy = SDL_CreateRGBSurface(0, rectangle.w,
			rectangle.h, source->format->BitsPerPixel, source->format->Rmask,
			source->format->Gmask, source->format->Bmask, source->format->Amask);

	SDL_Rect taille_copy;
	taille_copy.x = 0;
	taille_copy.y = 0;
	taille_copy.w = rectangle.w - rectangle.x;
	taille_copy.h = rectangle.h - rectangle.y;

	SDL_Rect position_copy;

	int w_ajout = rectangle.w - (source->w - rectangle.x);
	int h_ajout = rectangle.h - (source->h - rectangle.y);

	if (w_ajout > 0) {
		if (w_ajout % 2 == 0)
			position_copy.x = w_ajout / 2;
		else
			position_copy.x = w_ajout / 2 + 1;
	}
	else {
		position_copy.x = 0;
	}

	if (h_ajout > 0) {
		if (h_ajout % 2 == 0)
			position_copy.y = h_ajout / 2;
		else
			position_copy.y = h_ajout / 2 + 1;
	}
	else {
		position_copy.y = 0;
	}

	position_copy.w = source->w - rectangle.x;
	position_copy.h = source->h - rectangle.y;

	rectangle.w = source->w - rectangle.x;
	rectangle.h = source->h - rectangle.y;

	sdl_surface_mapp(copy, taille_copy, set_color, &color);

	if (SDL_BlitSurface(source, &rectangle, copy, &position_copy) != 0) {
		SDL_FreeSurface(copy);
		return (FAIL);
	}

	SDL_FreeSurface(source);
	g_cimp->screen[cmd->focus]->buff_screen = copy;

	return (OK);
} /* cimp_crop_extend */
