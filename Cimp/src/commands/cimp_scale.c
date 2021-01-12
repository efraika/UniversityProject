#include "cimp.h"

static SDL_Surface * scale_util(SDL_Surface * source, SDL_Rect rectangle) {
	SDL_Surface * copy = SDL_CreateRGBSurface(0, rectangle.w,
			rectangle.h, source->format->BitsPerPixel, source->format->Rmask,
			source->format->Gmask, source->format->Bmask, source->format->Amask);

	SDL_BlendMode oldBlendMode;

	SDL_GetSurfaceBlendMode(source, &oldBlendMode);

	SDL_SetSurfaceBlendMode(source, SDL_BLENDMODE_NONE);


	if (SDL_BlitScaled(source, NULL, copy, &rectangle) != 0) {
		SDL_FreeSurface(copy);
		SDL_SetSurfaceBlendMode(source, oldBlendMode);
		return NULL;
	}

	SDL_SetSurfaceBlendMode(source, oldBlendMode);

	return copy;
}

t_rc_cmd cimp_scale_rect(t_cmd * cmd) {
	if (cmd->point.x < 0 || cmd->point.y < 0) {
		printf("On ne peut pas avoir une image avec des dimensions negatives \n");
		return (FAIL);
	}
	SDL_Surface * source = g_cimp->screen[cmd->focus]->buff_screen;

	SDL_Rect rectangle;

	rectangle.x = 0;
	rectangle.y = 0;
	rectangle.w = cmd->point.x;
	rectangle.h = cmd->point.y;

	SDL_Surface * copy = scale_util(source, rectangle);

	if (copy == NULL)
		return (FAIL);

	SDL_FreeSurface(source);
	g_cimp->screen[cmd->focus]->buff_screen = copy;
	return (OK);
}

t_rc_cmd cimp_scale_ratio(t_cmd * cmd) {
	if (cmd->num < 0) {
		printf("On ne peut pas avoir une image avec des dimensions negatives \n");
		return (FAIL);
	}
	SDL_Surface * source = g_cimp->screen[cmd->focus]->buff_screen;

	SDL_Rect rectangle;

	rectangle.x = 0;
	rectangle.y = 0;
	rectangle.w = source->w * cmd->num;
	rectangle.h = source->h * cmd->num;

	SDL_Surface * copy = scale_util(source, rectangle);

	if (copy == NULL)
		return (FAIL);

	SDL_FreeSurface(source);
	g_cimp->screen[cmd->focus]->buff_screen = copy;
	return (OK);
}
