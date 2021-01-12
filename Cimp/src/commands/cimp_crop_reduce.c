#include "cimp.h"

t_rc_cmd cimp_crop_reduce(t_cmd * cmd) {
	SDL_Rect rectangle;

	if (cmd->rect.x != -1 && cmd->rect.y != -1 && cmd->rect.w != -1 && cmd->rect.h != -1) {
		rectangle = cmd->rect;
	}
	else if (g_cimp->select == NULL) {
		printf("Pas de selection courrante : impossible de rogner l'image \n");
		return (FAIL);
	}
	else {
		rectangle = g_cimp->select->surface;
	}

	SDL_Surface * source = g_cimp->screen[cmd->focus]->buff_screen;
	if (rectangle.x < 0 || rectangle.y < 0 || rectangle.w < 0 || rectangle.h < 0) {
		printf("Des coordonnées negatives, quelle idee ?\n");
		return (FAIL);
	}
	if (rectangle.w + rectangle.x > source->w || rectangle.x > source->w ||
	  rectangle.h + rectangle.y > source->h || rectangle.y > source->h)
	{
		printf("Le rectangle est plus grand que l'image : pour agrandir l'image utilisez la fonction crop_extend\n");
		return (FAIL);
	}

	SDL_Surface * copy = SDL_CreateRGBSurface(0, rectangle.w,
			rectangle.h, source->format->BitsPerPixel, source->format->Rmask,
			source->format->Gmask, source->format->Bmask, source->format->Amask);

	if (SDL_BlitSurface(source, &rectangle, copy, NULL) != 0) {
		SDL_FreeSurface(copy);
		return (FAIL);
	}

	SDL_FreeSurface(source);
	g_cimp->screen[cmd->focus]->buff_screen = copy;

	return (OK);
} /* cimp_crop_reduce */
